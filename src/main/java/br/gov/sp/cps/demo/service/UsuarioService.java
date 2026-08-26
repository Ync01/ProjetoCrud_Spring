package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.model.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    String criar(UsuarioDTO usuario);

    List<UsuarioDTO> listarTodos();

    UsuarioDTO buscarPorId(Long id);

    String atualizar(Long id, UsuarioDTO usuario);

    String deletar(Long id);

}
