package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.entities.Veiculo;
import br.gov.sp.cps.demo.repository.UsuarioRepository;
import br.gov.sp.cps.demo.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PermissoesIntegracaoTest {

    private static final String SEM_PERMISSAO = "Você não possui permissão para acessar esta página.";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void limpar() {
        veiculoRepository.deleteAll();
        usuarioRepository.findAll().stream()
                .filter(u -> !u.getEmail().equals("admin@parkflow.com"))
                .forEach(usuarioRepository::delete);
    }

    // Abre a página de login para criar a sessão e o token CSRF, como o navegador faz
    private MockHttpSession novaSessao() throws Exception {
        return (MockHttpSession) mvc.perform(get("/login")).andReturn().getRequest().getSession();
    }

    private String csrf(MockHttpSession session) {
        return (String) session.getAttribute("csrfToken");
    }

    private MockHttpSession logar(String email, String senha) throws Exception {
        MockHttpSession session = novaSessao();
        MockHttpSession logada = (MockHttpSession) mvc.perform(post("/autenticar").session(session)
                        .param("_csrf", csrf(session)).param("email", email).param("senha", senha))
                .andExpect(redirectedUrl("/home"))
                .andReturn().getRequest().getSession();
        // O login cria uma sessão nova; carrega uma página para gerar o token CSRF dela
        mvc.perform(get("/home").session(logada)).andExpect(status().isOk());
        return logada;
    }

    private Usuario criarUser(String email) {
        Usuario usuario = new Usuario();
        usuario.setNome("Comum");
        usuario.setEmail(email);
        usuario.setSenha(encoder.encode("senha1234"));
        usuario.setTipo(Usuario.USER);
        return usuarioRepository.save(usuario);
    }

    private Veiculo criarVeiculo() {
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca("ABC1D23");
        veiculo.setModelo("Gol");
        veiculo.setCor("Prata");
        veiculo.setDataEntrada(LocalDateTime.now());
        return veiculoRepository.save(veiculo);
    }

    @Test
    void rotasProtegidasSemLoginVaoParaLogin() throws Exception {
        mvc.perform(get("/home")).andExpect(redirectedUrl("/login"));
        mvc.perform(get("/veiculos")).andExpect(redirectedUrl("/login"));
        mvc.perform(get("/admin/usuarios")).andExpect(redirectedUrl("/login"));
    }

    @Test
    void postSemTokenCsrfERecusado() throws Exception {
        mvc.perform(post("/autenticar").param("email", "a@a.com").param("senha", "x"))
                .andExpect(status().isForbidden());
    }

    @Test
    void cadastroPublicoIgnoraTipoAdminESalvaSenhaComHash() throws Exception {
        MockHttpSession session = novaSessao();
        mvc.perform(post("/usuarios").session(session).param("_csrf", csrf(session))
                        .param("nome", "Invasor").param("email", "invasor@email.com")
                        .param("cpf", "529.982.247-25").param("senha", "senha1234")
                        .param("dataNascimento", "2000-01-01").param("tipo", "ADMIN"))
                .andExpect(redirectedUrl("/login"));

        Usuario salvo = usuarioRepository.findByEmail("invasor@email.com").orElseThrow();
        assertEquals(Usuario.USER, salvo.getTipo());
        assertTrue(encoder.matches("senha1234", salvo.getSenha()));
    }

    @Test
    void userConsultaMasNaoAlteraVeiculos() throws Exception {
        criarUser("user@email.com");
        Veiculo veiculo = criarVeiculo();
        MockHttpSession session = logar("user@email.com", "senha1234");

        mvc.perform(get("/veiculos").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ABC1D23")))
                .andExpect(content().string(not(containsString("Registrar entrada"))))
                .andExpect(content().string(not(containsString("/veiculos/deletar/"))));
        mvc.perform(get("/veiculos").param("placa", "abc").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ABC1D23")));

        mvc.perform(post("/veiculos").session(session).param("_csrf", csrf(session))
                        .param("placa", "XYZ1234").param("modelo", "Uno").param("cor", "Azul"))
                .andExpect(redirectedUrl("/home"))
                .andExpect(flash().attribute("erro", SEM_PERMISSAO));
        mvc.perform(post("/veiculos/saida/" + veiculo.getId()).session(session).param("_csrf", csrf(session)))
                .andExpect(redirectedUrl("/home"));
        mvc.perform(post("/veiculos/deletar/" + veiculo.getId()).session(session).param("_csrf", csrf(session)))
                .andExpect(redirectedUrl("/home"));
        mvc.perform(get("/veiculos/editar/" + veiculo.getId()).session(session))
                .andExpect(redirectedUrl("/home"));

        assertEquals(1, veiculoRepository.count());
        assertTrue(veiculoRepository.findById(veiculo.getId()).orElseThrow().getDataSaida() == null);
    }

    @Test
    void userNaoAcessaGerenciamentoDeUsuarios() throws Exception {
        Usuario user = criarUser("user@email.com");
        MockHttpSession session = logar("user@email.com", "senha1234");

        mvc.perform(get("/admin/usuarios").session(session))
                .andExpect(redirectedUrl("/home"))
                .andExpect(flash().attribute("erro", SEM_PERMISSAO));
        mvc.perform(post("/admin/usuarios/atualizar/" + user.getId()).session(session).param("_csrf", csrf(session))
                        .param("nome", "Comum").param("email", "user@email.com").param("tipo", "ADMIN"))
                .andExpect(redirectedUrl("/home"));

        assertEquals(Usuario.USER, usuarioRepository.findById(user.getId()).orElseThrow().getTipo());
    }

    @Test
    void adminGerenciaVeiculos() throws Exception {
        MockHttpSession session = logar("admin@parkflow.com", "admin1234");

        mvc.perform(post("/veiculos").session(session).param("_csrf", csrf(session))
                        .param("placa", "abc-1d23").param("modelo", "Gol").param("cor", "Prata"))
                .andExpect(redirectedUrl("/veiculos"))
                .andExpect(flash().attribute("sucesso", "Entrada registrada com sucesso!"));
        Veiculo veiculo = veiculoRepository.findByPlacaAndDataSaidaIsNull("ABC1D23").orElseThrow();

        mvc.perform(post("/veiculos").session(session).param("_csrf", csrf(session))
                        .param("placa", "12").param("modelo", "Gol").param("cor", "Prata"))
                .andExpect(flash().attribute("erro", "Placa inválida. Use o formato ABC1234 ou ABC1D23."));

        mvc.perform(post("/veiculos/atualizar/" + veiculo.getId()).session(session).param("_csrf", csrf(session))
                        .param("placa", "ABC1D23").param("modelo", "Polo").param("cor", "Preto"))
                .andExpect(redirectedUrl("/veiculos"));
        mvc.perform(post("/veiculos/saida/" + veiculo.getId()).session(session).param("_csrf", csrf(session)))
                .andExpect(flash().attribute("sucesso", "Saída registrada com sucesso!"));
        assertNotNull(veiculoRepository.findById(veiculo.getId()).orElseThrow().getDataSaida());

        mvc.perform(get("/veiculos/editar/999999").session(session)).andExpect(status().isNotFound());
        mvc.perform(post("/veiculos/deletar/" + veiculo.getId()).session(session).param("_csrf", csrf(session)))
                .andExpect(redirectedUrl("/veiculos"));
        assertFalse(veiculoRepository.existsById(veiculo.getId()));
    }

    @Test
    void operacoesDestrutivasNaoAceitamGet() throws Exception {
        Veiculo veiculo = criarVeiculo();
        MockHttpSession session = logar("admin@parkflow.com", "admin1234");

        mvc.perform(get("/veiculos/deletar/" + veiculo.getId()).session(session))
                .andExpect(status().isMethodNotAllowed());
        mvc.perform(get("/veiculos/saida/" + veiculo.getId()).session(session))
                .andExpect(status().isMethodNotAllowed());
        assertTrue(veiculoRepository.existsById(veiculo.getId()));
    }

    @Test
    void adminCriaPromoveEExcluiUsuarios() throws Exception {
        MockHttpSession session = logar("admin@parkflow.com", "admin1234");

        mvc.perform(post("/admin/usuarios").session(session).param("_csrf", csrf(session))
                        .param("nome", "Novo").param("email", "novo@email.com").param("cpf", "52998224725")
                        .param("senha", "senha1234").param("tipo", "USER"))
                .andExpect(flash().attribute("sucesso", "Usuário cadastrado com sucesso!"));
        Usuario novo = usuarioRepository.findByEmail("novo@email.com").orElseThrow();

        mvc.perform(post("/admin/usuarios").session(session).param("_csrf", csrf(session))
                        .param("nome", "Outro").param("email", "novo@email.com").param("cpf", "11144477735")
                        .param("senha", "senha1234").param("tipo", "USER"))
                .andExpect(flash().attribute("erro", "Email já cadastrado."));

        mvc.perform(post("/admin/usuarios/atualizar/" + novo.getId()).session(session).param("_csrf", csrf(session))
                        .param("nome", "Novo").param("email", "novo@email.com").param("cpf", "52998224725")
                        .param("tipo", "ADMIN"))
                .andExpect(redirectedUrl("/admin/usuarios"));
        Usuario promovido = usuarioRepository.findById(novo.getId()).orElseThrow();
        assertEquals(Usuario.ADMIN, promovido.getTipo());
        assertTrue(encoder.matches("senha1234", promovido.getSenha()));

        mvc.perform(get("/admin/usuarios").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("novo@email.com")))
                .andExpect(content().string(not(containsString(promovido.getSenha()))));

        mvc.perform(post("/admin/usuarios/deletar/" + novo.getId()).session(session).param("_csrf", csrf(session)))
                .andExpect(flash().attribute("sucesso", "Usuário excluído com sucesso!"));
        assertFalse(usuarioRepository.existsById(novo.getId()));
    }

    @Test
    void adminRebaixadoPerdeAcessoNaHora() throws Exception {
        Usuario outro = criarUser("outro@email.com");
        outro.setTipo(Usuario.ADMIN);
        usuarioRepository.save(outro);
        MockHttpSession session = logar("outro@email.com", "senha1234");
        mvc.perform(get("/admin/usuarios").session(session)).andExpect(status().isOk());

        outro.setTipo(Usuario.USER);
        usuarioRepository.save(outro);

        mvc.perform(get("/admin/usuarios").session(session)).andExpect(redirectedUrl("/home"));
    }

    @Test
    void logoutEncerraSessao() throws Exception {
        criarUser("user@email.com");
        MockHttpSession session = logar("user@email.com", "senha1234");

        mvc.perform(post("/logout").session(session).param("_csrf", csrf(session)))
                .andExpect(redirectedUrl("/login"));

        assertTrue(session.isInvalid());
        mvc.perform(get("/home")).andExpect(redirectedUrl("/login"));
    }

    @Test
    void loginComSenhaErradaFalha() throws Exception {
        criarUser("user@email.com");
        MockHttpSession session = novaSessao();

        mvc.perform(post("/autenticar").session(session).param("_csrf", csrf(session))
                        .param("email", "user@email.com").param("senha", "errada123"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("erro", "Email ou senha inválidos."));
    }
}
