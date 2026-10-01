package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.model.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    // Cadastro público: o usuário criado é sempre USER
    String criar(UsuarioDTO usuario);

    // Cadastro feito pelo ADMIN: pode escolher ADMIN ou USER
    String criarPorAdmin(UsuarioDTO usuario);

    UsuarioDTO autenticar(String email, String senha);

    List<UsuarioDTO> listarTodos();

    UsuarioDTO buscarPorId(Long id);

    String atualizar(Long id, UsuarioDTO usuario);

    String deletar(Long id);

}
