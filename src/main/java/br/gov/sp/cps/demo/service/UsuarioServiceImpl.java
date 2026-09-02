package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.model.UsuarioDTO;
import br.gov.sp.cps.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public String criar(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();

        usuario.setName(usuarioDTO.getName());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setBirthDate(usuarioDTO.getBirthDate());

        usuarioRepository.save(usuario);

        return "Usuário criado com sucesso!";
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        return List.of();
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        return null;
    }

    @Override
    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return null;
        }

        return new UsuarioDTO(
                usuario.getName(),
                usuario.getPassword(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getBirthDate()
        );
    }

    @Override
    public String atualizar(Long id, UsuarioDTO usuarioDTO) {
        return "";
    }

    @Override
    public String deletar(Long id){
        return "";
    }
}