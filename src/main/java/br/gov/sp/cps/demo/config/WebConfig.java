package br.gov.sp.cps.demo.config;

import br.gov.sp.cps.demo.interceptor.AdminInterceptor;
import br.gov.sp.cps.demo.interceptor.AutenticacaoInterceptor;
import br.gov.sp.cps.demo.interceptor.CsrfInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AutenticacaoInterceptor autenticacaoInterceptor;

    public WebConfig(AutenticacaoInterceptor autenticacaoInterceptor) {
        this.autenticacaoInterceptor = autenticacaoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Os interceptors rodam na ordem em que são registrados.

        // 1. Todos os POSTs precisam do token CSRF
        registry.addInterceptor(new CsrfInterceptor())
                .addPathPatterns("/**");

        // 2. Estas rotas exigem login. As públicas (/, /login, /cadastro,
        // /usuarios, /autenticar, /logout) ficam fora da lista e passam direto.
        registry.addInterceptor(autenticacaoInterceptor)
                .addPathPatterns("/home", "/veiculos", "/veiculos/**", "/admin", "/admin/**");

        // 3. Estas rotas exigem perfil ADMIN (exceto GET /veiculos, liberado dentro do AdminInterceptor)
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/veiculos", "/veiculos/**", "/admin", "/admin/**");
    }
}
