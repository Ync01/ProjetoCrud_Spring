package br.gov.sp.cps.demo.controller;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.exception.RecursoNaoEncontradoException;
import br.gov.sp.cps.demo.exception.RegraNegocioException;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

// Código que vale para TODOS os controllers
@ControllerAdvice
public class ControllerAdviceGlobal {

    // Disponibiliza "csrfToken" em todas as páginas para os formulários usarem
    @ModelAttribute("csrfToken")
    public String csrfToken(HttpSession session) {
        String token = (String) session.getAttribute("csrfToken");

        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
        }

        return token;
    }

    // Dados do usuário logado para as páginas (o AutenticacaoInterceptor já os atualizou a partir do banco)
    @ModelAttribute("usuarioNome")
    public Object usuarioNome(HttpSession session) {
        return session.getAttribute("usuarioNome");
    }

    @ModelAttribute("usuarioTipo")
    public Object usuarioTipo(HttpSession session) {
        return session.getAttribute("usuarioTipo");
    }

    // Usado nos templates só para esconder botões. A proteção real fica no AdminInterceptor.
    @ModelAttribute("ehAdmin")
    public boolean ehAdmin(HttpSession session) {
        return Usuario.ADMIN.equals(session.getAttribute("usuarioTipo"));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String recursoNaoEncontrado(RecursoNaoEncontradoException e, Model model) {
        model.addAttribute("mensagem", e.getMessage());
        return "erro";
    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String regraNegocio(RegraNegocioException e, Model model) {
        model.addAttribute("mensagem", e.getMessage());
        return "erro";
    }

    // Acontece quando o banco recusa (ex.: email ou CPF repetido que passou pela checagem do service)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String integridade(DataIntegrityViolationException e, Model model) {
        model.addAttribute("mensagem", "Já existe um registro com esses dados.");
        return "erro";
    }
}
