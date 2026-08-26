package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.Usuarios;
import br.gov.sp.cps.demo.model.UserModel;

import java.util.List;

public interface UsuarioServices{

    String createUser(UserModel usuarioCreate);
    List<Usuarios> listaUser();
    String UpdateBank(UserModel UsuarioUpdate);
    String DeleteBank(String id);

}
