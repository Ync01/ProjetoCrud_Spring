package br.gov.sp.cps.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

// CSRF = impedir que outro site faça uma requisição POST usando a sessão do usuário.
// Cada sessão tem um token secreto; todo formulário POST envia esse token no campo "_csrf".
// Se o token do formulário não for igual ao da sessão, a requisição é recusada
// (a página templates/error.html mostra a mensagem para o usuário).
public class CsrfInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        HttpSession session = request.getSession(false);
        String tokenSessao = session == null ? null : (String) session.getAttribute("csrfToken");
        String tokenFormulario = request.getParameter("_csrf");

        // MessageDigest.isEqual compara em tempo constante (não "vaza" quantos caracteres acertou)
        if (tokenSessao != null && tokenFormulario != null
                && MessageDigest.isEqual(tokenSessao.getBytes(StandardCharsets.UTF_8),
                                         tokenFormulario.getBytes(StandardCharsets.UTF_8))) {
            return true;
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF inválido.");
        return false;
    }
}
