package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.model.UsuarioDTO;
import br.gov.sp.cps.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/Index")
    public String index() {
        return "Index";
    }

    @GetMapping("/ParkFlow")
    public String parkFlow() {
        return "ParkFlow";
    }

    @GetMapping("/LoginP")
    public String loginP(){
        return "LoginP";
    }

    @PostMapping("/btn")
    public String handlePost() {
        return "resultView";
    }

    @GetMapping("/auth")
    @ResponseBody
    public String testAuth() {
        return "Controller funcionando";
    }

    @PostMapping("/usuarios")
    public String criarUsuario(@ModelAttribute UsuarioDTO usuarioDTO) {

        usuarioService.criar(usuarioDTO);

        return "redirect:/ParkFlow";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String email, @RequestParam String password) {
        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return "redirect:/ParkFlow";
        }

        return "redirect:/LoginP?erro";
    }
}