package br.gov.sp.cps.demo.config;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// Roda uma vez sempre que a aplicação inicia:
// 1. Ajusta usuários antigos (sem perfil ou com senha em texto puro) sem apagar dados.
// 2. Garante que exista pelo menos um ADMIN, já que o cadastro público só cria USER.
@Component
public class DadosIniciais implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DadosIniciais.class);

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${parkflow.admin.nome}")
    private String adminNome;

    @Value("${parkflow.admin.email}")
    private String adminEmail;

    @Value("${parkflow.admin.senha}")
    private String adminSenha;

    public DadosIniciais(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        for (Usuario usuario : usuarioRepository.findAll()) {
            boolean alterado = false;

            if (!Usuario.ADMIN.equals(usuario.getTipo()) && !Usuario.USER.equals(usuario.getTipo())) {
                usuario.setTipo(Usuario.USER);
                alterado = true;
            }

            // Hash BCrypt sempre começa com "$2" e tem 60 caracteres. Se não for assim, é texto puro.
            String senha = usuario.getSenha();
            if (senha != null && !(senha.startsWith("$2") && senha.length() == 60)) {
                usuario.setSenha(passwordEncoder.encode(senha));
                alterado = true;
            }

            if (usuario.getEmail() != null && !usuario.getEmail().equals(usuario.getEmail().trim().toLowerCase())) {
                usuario.setEmail(usuario.getEmail().trim().toLowerCase());
                alterado = true;
            }

            if (alterado) {
                usuarioRepository.save(usuario);
                log.info("Usuário {} ajustado (perfil/senha/email).", usuario.getId());
            }
        }

        String email = adminEmail.trim().toLowerCase();
        Usuario admin = usuarioRepository.findByEmail(email).orElseGet(Usuario::new);
        admin.setNome(adminNome);
        admin.setEmail(email);
        admin.setSenha(passwordEncoder.encode(adminSenha));
        admin.setTipo(Usuario.ADMIN);
        usuarioRepository.save(admin);


    }
}
