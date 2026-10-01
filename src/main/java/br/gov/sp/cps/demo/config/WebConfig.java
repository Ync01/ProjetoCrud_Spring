package br.gov.sp.cps.demo.config;

import br.gov.sp.cps.demo.interceptor.AutenticacaoInterceptor;
import br.gov.sp.cps.demo.interceptor.CsrfInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Todos os POSTs precisam do token CSRF
        registry.addInterceptor(new CsrfInterceptor())
                .addPathPatterns("/**");

        // Só estas rotas exigem login. As públicas (/, /login, /cadastro,
        // /usuarios, /autenticar) ficam fora da lista e passam direto.
        registry.addInterceptor(new AutenticacaoInterceptor())
                .addPathPatterns("/home", "/veiculos", "/veiculos/**");
    }
}
