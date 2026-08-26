package br.gov.sp.cps.demo.service;

import br.gov.sp.cps.demo.entities.UsuariosEntities;
import br.gov.sp.cps.demo.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImp implements UsuarioServices {

    @Autowired
    @Override
    public String createUser(UserModel usuarioCreate) {
        UsuariosEntities usuario = new UsuariosEntities();

        usuario.setName(usuarioCreate.getName());
        usuario.setEmail(usuarioCreate.getEmail());
        usuario.setCpf(usuarioCreate.getCpf());
        usuario.setPassword(usuarioCreate.getPassword());
        usuario.setBirthDate(usuarioCreate.getBirthDate());
        return "";
    }

    @Override
    public List<UsuariosEntities> listaUser() {
        return List.of();
    }

    @Override
    public String updateUser(UserModel UsuarioUpdate) {
        return "";
    }

    @Override
    public String deleteUser(String id) {
        return "";
    }
}
