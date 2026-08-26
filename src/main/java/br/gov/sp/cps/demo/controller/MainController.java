package br.gov.sp.cps.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "Index";
    }

    @GetMapping("/Index.html")
    public String indexHtml() {
        return "Index";
    }

    @GetMapping("/auth")
    @ResponseBody
    public String testAuth() {
        return "Controller funcionando";
    }


    @PostMapping("/auth")
    public String auth(
            @RequestParam(value = "username", required = false) String email,
            @RequestParam(value = "password", required = false) String senha) {

        // Credenciais de teste
        if ("test@test.com".equals(email) && "123".equals(senha)) {
            return "rAcessoP"
        }

        // Login incorreto
        return "Index";
    }
}