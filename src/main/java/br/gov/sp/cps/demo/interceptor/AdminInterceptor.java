package br.gov.sp.cps.demo.interceptor;

import br.gov.sp.cps.demo.entities.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

// AUTORIZAÇÃO: roda depois do AutenticacaoInterceptor (ver WebConfig), então o
// usuário já está logado e o perfil na sessão já foi conferido com o banco.
// Só ADMIN passa. USER é mandado para /home com uma mensagem.
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // GET /veiculos (listar, pesquisar e histórico) é a única rota de veículos liberada para USER
        if ("GET".equals(request.getMethod()) && (request.getContextPath() + "/veiculos").equals(request.getRequestURI())) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session != null && Usuario.ADMIN.equals(session.getAttribute("usuarioTipo"))) {
            return true;
        }

        FlashMap flash = RequestContextUtils.getOutputFlashMap(request);
        flash.put("erro", "Você não possui permissão para acessar esta página.");
        RequestContextUtils.saveOutputFlashMap("/home", request, response);

        response.sendRedirect(request.getContextPath() + "/home");
        return false;
    }
}
