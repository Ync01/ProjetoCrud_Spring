package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.UsuariosEntities;
import br.gov.sp.cps.demo.model.UserModel;

import java.util.List;

public interface UsuarioServices{

    String createUser(UserModel usuarioCreate);
    List<UsuariosEntities> listaUser();
    String updateUser(UserModel UsuarioUpdate);
    String deleteUser(String id);

}
