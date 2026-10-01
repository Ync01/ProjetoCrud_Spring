package br.gov.sp.cps.demo.interceptor;

import br.gov.sp.cps.demo.entities.Usuario;
import br.gov.sp.cps.demo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

// AUTENTICAÇÃO: roda ANTES do controller nas rotas protegidas (ver WebConfig).
// Sem usuarioId na sessão (ou se o usuário foi excluído), manda para /login.
// Também recarrega nome e perfil do banco, para a sessão refletir alterações feitas pelo ADMIN.
@Component
public class AutenticacaoInterceptor implements HandlerInterceptor {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoInterceptor(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Impede que o navegador mostre páginas protegidas do cache (botão "voltar" depois do logout)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);
        Long usuarioId = session == null ? null : (Long) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            if (usuario.isPresent()) {
                session.setAttribute("usuarioNome", usuario.get().getNome());
                session.setAttribute("usuarioTipo", usuario.get().getTipo());
                return true;
            }
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
