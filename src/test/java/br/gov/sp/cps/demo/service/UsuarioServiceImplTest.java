package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.exception.RegraNegocioException;
import br.gov.sp.cps.demo.model.UsuarioDTO;
import br.gov.sp.cps.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void preparar() {
        usuarioService = new UsuarioServiceImpl(usuarioRepository, encoder);
    }

    private UsuarioDTO criarDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Maria");
        dto.setEmail("Maria@Email.com");
        dto.setCpf("529.982.247-25");
        dto.setSenha("senha1234");
        dto.setDataNascimento(LocalDate.of(2000, 5, 10));
        return dto;
    }

    @Test
    void criarSalvaSenhaComBCryptEDadosNormalizados() {
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        usuarioService.criar(criarDTO());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario salvo = captor.getValue();

        assertNotEquals("senha1234", salvo.getSenha());
        assertTrue(encoder.matches("senha1234", salvo.getSenha()));
        assertEquals("maria@email.com", salvo.getEmail());
        assertEquals("52998224725", salvo.getCpf());
    }

    @Test
    void criarImpedeEmailDuplicado() {
        when(usuarioRepository.existsByEmail("maria@email.com")).thenReturn(true);

        RegraNegocioException erro = assertThrows(RegraNegocioException.class, () -> usuarioService.criar(criarDTO()));

        assertEquals("Email já cadastrado.", erro.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void criarImpedeCpfDuplicado() {
        when(usuarioRepository.existsByCpf("52998224725")).thenReturn(true);

        RegraNegocioException erro = assertThrows(RegraNegocioException.class, () -> usuarioService.criar(criarDTO()));

        assertEquals("CPF já cadastrado.", erro.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void criarImpedeCpfInvalido() {
        UsuarioDTO dto = criarDTO();
        dto.setCpf("111.111.111-11");

        assertThrows(RegraNegocioException.class, () -> usuarioService.criar(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void autenticarComSenhaCorreta() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setSenha(encoder.encode("senha1234"));
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.autenticar("Maria@Email.com", "senha1234");

        assertEquals(1L, resultado.getId());
        assertEquals("Maria", resultado.getNome());
    }

    @Test
    void autenticarComSenhaIncorretaLancaErro() {
        Usuario usuario = new Usuario();
        usuario.setEmail("maria@email.com");
        usuario.setSenha(encoder.encode("senha1234"));
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        RegraNegocioException erro = assertThrows(RegraNegocioException.class,
                () -> usuarioService.autenticar("maria@email.com", "outraSenha"));

        assertEquals("Login inválido.", erro.getMessage());
    }

    @Test
    void autenticarComEmailInexistenteLancaErro() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> usuarioService.autenticar("naoexiste@email.com", "senha1234"));
    }
}
