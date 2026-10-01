package br.gov.sp.cps.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

// CSRF = impedir que outro site faça uma requisição POST usando a sessão do usuário.
// Cada sessão tem um token secreto; todo formulário POST envia esse token no campo "_csrf".
// Se o token do formulário não for igual ao da sessão, a requisição é recusada.
public class CsrfInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        HttpSession session = request.getSession(false);
        String tokenSessao = session == null ? null : (String) session.getAttribute("csrfToken");
        String tokenFormulario = request.getParameter("_csrf");

        if (tokenSessao != null && tokenSessao.equals(tokenFormulario)) {
            return true;
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF inválido.");
        return false;
    }
}
