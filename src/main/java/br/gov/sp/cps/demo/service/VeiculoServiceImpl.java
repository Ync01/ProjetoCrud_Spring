package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Veiculo;
import br.gov.sp.cps.demo.exception.RecursoNaoEncontradoException;
import br.gov.sp.cps.demo.exception.RegraNegocioException;
import br.gov.sp.cps.demo.model.VeiculoDTO;
import br.gov.sp.cps.demo.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    // Formato antigo (ABC1234) e Mercosul (ABC1D23)
    private static final String FORMATO_PLACA = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$";

    private final VeiculoRepository veiculoRepository;

    public VeiculoServiceImpl(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public String cadastrarVeiculo(VeiculoDTO veiculoDTO) {
        String placa = normalizarPlaca(veiculoDTO.getPlaca());
        validar(placa, veiculoDTO);

        if (veiculoRepository.existsByPlacaAndDataSaidaIsNull(placa)) {
            throw new RegraNegocioException("Este veículo já está no estacionamento.");
        }

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(placa);
        veiculo.setModelo(veiculoDTO.getModelo().trim());
        veiculo.setCor(veiculoDTO.getCor().trim());
        veiculo.setObservacao(veiculoDTO.getObservacao());
        veiculo.setDataEntrada(LocalDateTime.now());

        veiculoRepository.save(veiculo);

        return "Entrada registrada com sucesso!";
    }

    @Override
    public List<VeiculoDTO> listarVeiculos() {
        return paraDTOs(veiculoRepository.findAll());
    }

    @Override
    public List<VeiculoDTO> listarAtivos() {
        return paraDTOs(veiculoRepository.findByDataSaidaIsNullOrderByDataEntradaDesc());
    }

    @Override
    public List<VeiculoDTO> listarHistorico() {
        return paraDTOs(veiculoRepository.findByDataSaidaIsNotNullOrderByDataSaidaDesc());
    }

    @Override
    public VeiculoDTO buscarPorId(Long id) {
        return paraDTO(buscarEntidade(id));
    }

    @Override
    public VeiculoDTO buscarPorPlaca(String placa) {
        Veiculo veiculo = veiculoRepository.findFirstByPlacaOrderByDataEntradaDesc(normalizarPlaca(placa))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado."));
        return paraDTO(veiculo);
    }

    @Override
    public String atualizar(Long id, VeiculoDTO veiculoDTO) {
        Veiculo veiculo = buscarEntidade(id);

        String placa = normalizarPlaca(veiculoDTO.getPlaca());
        validar(placa, veiculoDTO);

        Optional<Veiculo> outroNoPatio = veiculoRepository.findByPlacaAndDataSaidaIsNull(placa);
        if (outroNoPatio.isPresent() && !outroNoPatio.get().getId().equals(id)) {
            throw new RegraNegocioException("Já existe outro veículo com esta placa no estacionamento.");
        }

        // A data de entrada não muda na edição
        veiculo.setPlaca(placa);
        veiculo.setModelo(veiculoDTO.getModelo().trim());
        veiculo.setCor(veiculoDTO.getCor().trim());
        veiculo.setObservacao(veiculoDTO.getObservacao());

        veiculoRepository.save(veiculo);

        return "Veículo atualizado com sucesso!";
    }

    @Override
    public String deletar(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Veículo não encontrado.");
        }

        veiculoRepository.deleteById(id);
        return "Veículo excluído com sucesso!";
    }

    @Override
    public String darSaida(Long id) {
        Veiculo veiculo = buscarEntidade(id);

        if (veiculo.getDataSaida() != null) {
            throw new RegraNegocioException("A saída deste veículo já foi registrada.");
        }

        veiculo.setDataSaida(LocalDateTime.now());
        veiculoRepository.save(veiculo);

        return "Saída registrada com sucesso!";
    }

    private Veiculo buscarEntidade(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado."));
    }

    private void validar(String placa, VeiculoDTO dto) {
        if (!placa.matches(FORMATO_PLACA)) {
            throw new RegraNegocioException("Placa inválida. Use o formato ABC1234 ou ABC1D23.");
        }
        if (dto.getModelo() == null || dto.getModelo().isBlank()) {
            throw new RegraNegocioException("Informe o modelo.");
        }
        if (dto.getCor() == null || dto.getCor().isBlank()) {
            throw new RegraNegocioException("Informe a cor.");
        }
    }

    // " abc-1d23 " -> "ABC1D23"
    private String normalizarPlaca(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase().replace("-", "");
    }

    private List<VeiculoDTO> paraDTOs(List<Veiculo> veiculos) {
        List<VeiculoDTO> lista = new ArrayList<>();
        for (Veiculo veiculo : veiculos) {
            lista.add(paraDTO(veiculo));
        }
        return lista;
    }

    private VeiculoDTO paraDTO(Veiculo v) {
        return new VeiculoDTO(v.getId(), v.getPlaca(), v.getModelo(), v.getCor(),
                v.getObservacao(), v.getDataEntrada(), v.getDataSaida());
    }
}
