package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Veiculo;
import br.gov.sp.cps.demo.exception.RecursoNaoEncontradoException;
import br.gov.sp.cps.demo.exception.RegraNegocioException;
import br.gov.sp.cps.demo.model.VeiculoDTO;
import br.gov.sp.cps.demo.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Mockito cria um repository "de mentira", assim o teste não usa banco de verdade
@ExtendWith(MockitoExtension.class)
class VeiculoServiceImplTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    private VeiculoServiceImpl veiculoService;

    @BeforeEach
    void preparar() {
        veiculoService = new VeiculoServiceImpl(veiculoRepository);
    }

    private VeiculoDTO criarDTO(String placa) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setPlaca(placa);
        dto.setModelo("Gol");
        dto.setCor("Prata");
        dto.setObservacao("Teste");
        return dto;
    }

    private Veiculo criarVeiculo(Long id, String placa) {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(id);
        veiculo.setPlaca(placa);
        veiculo.setModelo("Gol");
        veiculo.setCor("Prata");
        veiculo.setDataEntrada(LocalDateTime.of(2026, 1, 1, 8, 0));
        return veiculo;
    }

    @Test
    void cadastrarSalvaVeiculoComPlacaNormalizada() {
        veiculoService.cadastrarVeiculo(criarDTO(" abc1d23 "));

        ArgumentCaptor<Veiculo> captor = ArgumentCaptor.forClass(Veiculo.class);
        verify(veiculoRepository).save(captor.capture());

        assertEquals("ABC1D23", captor.getValue().getPlaca());
        assertNotNull(captor.getValue().getDataEntrada());
    }

    @Test
    void cadastrarImpedePlacaAtivaDuplicada() {
        when(veiculoRepository.existsByPlacaAndDataSaidaIsNull("ABC1D23")).thenReturn(true);

        RegraNegocioException erro = assertThrows(RegraNegocioException.class,
                () -> veiculoService.cadastrarVeiculo(criarDTO("ABC1D23")));

        assertEquals("Este veículo já está no estacionamento.", erro.getMessage());
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void buscarPorIdMantemDataSaida() {
        Veiculo veiculo = criarVeiculo(1L, "ABC1D23");
        veiculo.setDataSaida(LocalDateTime.of(2026, 1, 1, 10, 0));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoDTO dto = veiculoService.buscarPorId(1L);

        assertEquals("ABC1D23", dto.getPlaca());
        assertEquals(LocalDateTime.of(2026, 1, 1, 10, 0), dto.getDataSaida());
    }

    @Test
    void buscarPorIdInexistenteLancaErro() {
        when(veiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> veiculoService.buscarPorId(99L));
    }

    @Test
    void buscarPorPlacaInexistenteLancaErro() {
        when(veiculoRepository.findFirstByPlacaOrderByDataEntradaDesc("XXX0X00")).thenReturn(Optional.empty());

        RecursoNaoEncontradoException erro = assertThrows(RecursoNaoEncontradoException.class,
                () -> veiculoService.buscarPorPlaca("xxx0x00"));

        assertEquals("Veículo não encontrado.", erro.getMessage());
    }

    @Test
    void atualizarNaoAlteraDataEntrada() {
        Veiculo veiculo = criarVeiculo(1L, "AAA1A11");
        LocalDateTime entradaOriginal = veiculo.getDataEntrada();
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        veiculoService.atualizar(1L, criarDTO("bbb2b22"));

        assertEquals("BBB2B22", veiculo.getPlaca());
        assertEquals(entradaOriginal, veiculo.getDataEntrada());
        verify(veiculoRepository).save(veiculo);
    }

    @Test
    void atualizarImpedePlacaDeOutroVeiculoAtivo() {
        Veiculo veiculo = criarVeiculo(1L, "AAA1A11");
        Veiculo outro = criarVeiculo(2L, "BBB2B22");
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.findByPlacaAndDataSaidaIsNull("BBB2B22")).thenReturn(Optional.of(outro));

        assertThrows(RegraNegocioException.class, () -> veiculoService.atualizar(1L, criarDTO("BBB2B22")));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void darSaidaRegistraHorario() {
        Veiculo veiculo = criarVeiculo(1L, "ABC1D23");
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        veiculoService.darSaida(1L);

        assertNotNull(veiculo.getDataSaida());
        verify(veiculoRepository).save(veiculo);
    }

    @Test
    void darSaidaDuasVezesNaoSobrescreve() {
        Veiculo veiculo = criarVeiculo(1L, "ABC1D23");
        LocalDateTime saida = LocalDateTime.of(2026, 1, 1, 10, 0);
        veiculo.setDataSaida(saida);
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        RegraNegocioException erro = assertThrows(RegraNegocioException.class, () -> veiculoService.darSaida(1L));

        assertEquals("A saída deste veículo já foi registrada.", erro.getMessage());
        assertEquals(saida, veiculo.getDataSaida());
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void deletarVeiculoExistente() {
        when(veiculoRepository.existsById(1L)).thenReturn(true);

        veiculoService.deletar(1L);

        verify(veiculoRepository).deleteById(1L);
    }

    @Test
    void deletarVeiculoInexistenteLancaErro() {
        when(veiculoRepository.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNaoEncontradoException.class, () -> veiculoService.deletar(99L));
        verify(veiculoRepository, never()).deleteById(any());
    }
}
