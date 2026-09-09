package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.model.UsuarioDTO;
import br.gov.sp.cps.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String parkFlow(HttpSession session, Model model) {
        model.addAttribute("usuarioNome", session.getAttribute("usuarioNome"));
        return "ParkFlow";
    }

    @GetMapping("/LoginP")
    public String loginP() {
        return "LoginP";
    }

    @GetMapping("/CadrastoVeiculo")
    public String CadrastoVeiculo(){
        return "CadrastoVeiculo";
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
    public String criarUsuario(@ModelAttribute UsuarioDTO usuarioDTO, HttpSession session) {
        usuarioService.criar(usuarioDTO);
        session.setAttribute("usuarioNome", usuarioDTO.getName());
        return "redirect:/ParkFlow";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String email, @RequestParam String password, HttpSession session) {
        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {
            session.setAttribute("usuarioNome", usuario.getName());
            return "redirect:/ParkFlow";
        }

        return "redirect:/LoginP?erro";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/ParkFlow";
    }
}