package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.exception.RecursoNaoEncontradoException;
import br.gov.sp.cps.demo.exception.RegraNegocioException;
import br.gov.sp.cps.demo.model.UsuarioDTO;
import br.gov.sp.cps.demo.repository.UsuarioRepository;
import br.gov.sp.cps.demo.util.CpfValidador;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final int TAMANHO_MINIMO_SENHA = 8;

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String criar(UsuarioDTO usuarioDTO) {
        // Mesmo que alguém envie tipo=ADMIN no formulário, o cadastro público sempre cria USER
        salvarNovo(usuarioDTO, Usuario.USER);
        return "Usuário cadastrado com sucesso! Faça login.";
    }

    @Override
    public String criarPorAdmin(UsuarioDTO usuarioDTO) {
        salvarNovo(usuarioDTO, validarTipo(usuarioDTO.getTipo()));
        return "Usuário cadastrado com sucesso!";
    }

    private void salvarNovo(UsuarioDTO dto, String tipo) {
        String email = normalizarEmail(dto.getEmail());
        String cpf = normalizarCpf(dto.getCpf());

        validarDados(dto.getNome(), email, cpf, dto.getDataNascimento());
        validarSenha(dto.getSenha());

        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Email já cadastrado.");
        }
        if (usuarioRepository.existsByCpf(cpf)) {
            throw new RegraNegocioException("CPF já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome().trim());
        usuario.setEmail(email);
        usuario.setCpf(cpf);
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setTipo(tipo);

        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioDTO autenticar(String email, String senha) {
        // A mesma mensagem para email inexistente e senha errada, para não revelar quais emails existem
        Usuario usuario = usuarioRepository.findByEmail(normalizarEmail(email))
                .orElseThrow(() -> new RegraNegocioException("Login inválido."));

        if (senha == null || usuario.getSenha() == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RegraNegocioException("Login inválido.");
        }

        return paraDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        for (Usuario usuario : usuarioRepository.findAll()) {
            usuarios.add(paraDTO(usuario));
        }
        return usuarios;
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        return paraDTO(buscarEntidade(id));
    }

    @Override
    public String atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarEntidade(id);

        String email = normalizarEmail(dto.getEmail());
        String cpf = normalizarCpf(dto.getCpf());
        String tipo = validarTipo(dto.getTipo());

        // Usuários antigos (ou o ADMIN inicial) podem não ter CPF; nesse caso o campo pode ficar vazio
        boolean semCpf = cpf.isEmpty() && usuario.getCpf() == null;
        validarDados(dto.getNome(), email, semCpf ? null : cpf, dto.getDataNascimento());
        if (semCpf) {
            cpf = null;
        }

        if (!email.equals(usuario.getEmail()) && usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Email já cadastrado.");
        }
        if (cpf != null && !cpf.equals(usuario.getCpf()) && usuarioRepository.existsByCpf(cpf)) {
            throw new RegraNegocioException("CPF já cadastrado.");
        }
        if (Usuario.ADMIN.equals(usuario.getTipo()) && Usuario.USER.equals(tipo) && ehUltimoAdmin()) {
            throw new RegraNegocioException("Não é possível remover o perfil ADMIN do último administrador.");
        }

        usuario.setNome(dto.getNome().trim());
        usuario.setEmail(email);
        usuario.setCpf(cpf);
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setTipo(tipo);

        // Senha em branco na edição = manter a senha atual
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            validarSenha(dto.getSenha());
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        usuarioRepository.save(usuario);
        return "Usuário atualizado com sucesso!";
    }

    @Override
    public String deletar(Long id) {
        Usuario usuario = buscarEntidade(id);

        if (Usuario.ADMIN.equals(usuario.getTipo()) && ehUltimoAdmin()) {
            throw new RegraNegocioException("Não é possível excluir o último administrador.");
        }

        usuarioRepository.delete(usuario);
        return "Usuário excluído com sucesso!";
    }

    private Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    private boolean ehUltimoAdmin() {
        return usuarioRepository.countByTipo(Usuario.ADMIN) <= 1;
    }

    // cpf == null significa "sem CPF" (só aceito na edição de usuário que já não tinha CPF)
    private void validarDados(String nome, String email, String cpf, LocalDate dataNascimento) {
        if (nome == null || nome.isBlank()) {
            throw new RegraNegocioException("Informe o nome.");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new RegraNegocioException("Email inválido.");
        }
        if (cpf != null && !CpfValidador.valido(cpf)) {
            throw new RegraNegocioException("CPF inválido.");
        }
        if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
            throw new RegraNegocioException("Data de nascimento inválida.");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < TAMANHO_MINIMO_SENHA) {
            throw new RegraNegocioException("A senha deve ter no mínimo " + TAMANHO_MINIMO_SENHA + " caracteres.");
        }
    }

    private String validarTipo(String tipo) {
        if (Usuario.ADMIN.equals(tipo) || Usuario.USER.equals(tipo)) {
            return tipo;
        }
        throw new RegraNegocioException("Perfil inválido. Use ADMIN ou USER.");
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    // "529.982.247-25" -> "52998224725"
    private String normalizarCpf(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    // Nunca copia a senha (nem o hash) para o DTO
    private UsuarioDTO paraDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setCpf(usuario.getCpf());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setTipo(usuario.getTipo());
        return dto;
    }
}
