package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.exception.RegraNegocioException;
import br.gov.sp.cps.demo.model.UsuarioDTO;
import br.gov.sp.cps.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Gerenciamento de usuários. Todas as rotas /admin/** só chegam aqui
// se o usuário logado for ADMIN (ver WebConfig e AdminInterceptor).
@Controller
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/admin", "/admin/usuarios"})
    public String listar(Model model) {
        if (!model.containsAttribute("usuario")) {
            UsuarioDTO novo = new UsuarioDTO();
            novo.setTipo("USER");
            model.addAttribute("usuario", novo);
        }
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", usuarioService.buscarPorId(id));
        }
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @PostMapping("/admin/usuarios")
    public String criar(@ModelAttribute UsuarioDTO usuarioDTO, RedirectAttributes redirect) {
        try {
            redirect.addFlashAttribute("sucesso", usuarioService.criarPorAdmin(usuarioDTO));
        } catch (RegraNegocioException e) {
            usuarioDTO.setSenha(null);
            redirect.addFlashAttribute("erro", e.getMessage());
            redirect.addFlashAttribute("usuario", usuarioDTO);
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/admin/usuarios/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute UsuarioDTO usuarioDTO, RedirectAttributes redirect) {
        try {
            redirect.addFlashAttribute("sucesso", usuarioService.atualizar(id, usuarioDTO));
            return "redirect:/admin/usuarios";
        } catch (RegraNegocioException e) {
            usuarioDTO.setId(id);
            usuarioDTO.setSenha(null);
            redirect.addFlashAttribute("erro", e.getMessage());
            redirect.addFlashAttribute("usuario", usuarioDTO);
            return "redirect:/admin/usuarios/editar/" + id;
        }
    }

    @PostMapping("/admin/usuarios/deletar/{id}")
    public String deletar(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        if (id.equals(session.getAttribute("usuarioId"))) {
            redirect.addFlashAttribute("erro", "Você não pode excluir o próprio usuário.");
            return "redirect:/admin/usuarios";
        }
        try {
            redirect.addFlashAttribute("sucesso", usuarioService.deletar(id));
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
