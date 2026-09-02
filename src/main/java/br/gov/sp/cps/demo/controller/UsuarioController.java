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

    @GetMapping("/FloWork")
    public String floWork() {
        return "FloWork";
    }

    @GetMapping("/auth")
    @ResponseBody
    public String testAuth() {
        return "Controller funcionando";
    }

    @GetMapping({"/login", "/Login.html"})
    public String login() {
        return "Login";
    }

    @PostMapping("/usuarios")
    public String criarUsuario(@ModelAttribute UsuarioDTO usuarioDTO) {

        usuarioService.criar(usuarioDTO);

        return "redirect:/FloWork";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String email, @RequestParam String password) {
        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return "AcessoP";
        }

        return "redirect:/login?erro";
    }
}