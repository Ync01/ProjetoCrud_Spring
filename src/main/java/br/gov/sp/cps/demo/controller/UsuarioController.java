package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.exception.RegraNegocioException;
import br.gov.sp.cps.demo.model.UsuarioDTO;
import br.gov.sp.cps.demo.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Rotas públicas (login, cadastro, logout) e a página inicial do usuário logado
@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String inicio(HttpSession session) {
        return session.getAttribute("usuarioId") != null ? "redirect:/home" : "redirect:/login";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new UsuarioDTO());
        }
        return "cadastro";
    }

    // Cadastro público. O campo "tipo" é ignorado: o service sempre grava USER.
    @PostMapping("/usuarios")
    public String criarUsuario(@ModelAttribute UsuarioDTO usuarioDTO, RedirectAttributes redirect) {
        try {
            redirect.addFlashAttribute("sucesso", usuarioService.criar(usuarioDTO));
            return "redirect:/login";
        } catch (RegraNegocioException e) {
            usuarioDTO.setSenha(null);
            redirect.addFlashAttribute("erro", e.getMessage());
            redirect.addFlashAttribute("usuario", usuarioDTO);
            return "redirect:/cadastro";
        }
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String email, @RequestParam String senha,
                             HttpServletRequest request, RedirectAttributes redirect) {
        UsuarioDTO usuario;
        try {
            usuario = usuarioService.autenticar(email, senha);
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", "Email ou senha inválidos.");
            return "redirect:/login";
        }

        // Cria uma sessão nova no login (evita reaproveitar um ID de sessão anterior)
        HttpSession antiga = request.getSession(false);
        if (antiga != null) {
            antiga.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNome", usuario.getNome());
        session.setAttribute("usuarioTipo", usuario.getTipo());

        return "redirect:/home";
    }

    // POST + CSRF: um link ou imagem em outro site não consegue deslogar o usuário
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirect) {
        session.invalidate();
        redirect.addFlashAttribute("sucesso", "Você saiu do sistema.");
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // Endereços antigos do projeto, mantidos para não quebrar links salvos
    @GetMapping({"/Index", "/ParkFlow", "/LoginP"})
    public String rotasAntigas() {
        return "redirect:/";
    }
}
