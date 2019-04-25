/*

 */

package org.gmu.chess.components;

import ca.watier.echechess.common.interfaces.WebSocketService;

import org.gmu.chess.server.UiSessionHandlerInterceptor;
import org.gmu.chess.services.UiSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.concurrent.TimeUnit;

/**
 *
 */

@EnableWebMvc
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    private final UiSessionService uiSessionService;
    private final WebSocketService webSocketService;

    @Autowired
    public MvcConfiguration(UiSessionService uiSessionService, WebSocketService webSocketService) {
        this.uiSessionService = uiSessionService;
        this.webSocketService = webSocketService;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor()).addPathPatterns("/api/game/**");
    }

    @Bean
    public UiSessionHandlerInterceptor interceptor() {
        return new UiSessionHandlerInterceptor(uiSessionService, webSocketService);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        CacheControl cacheControl = CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic();
        registry.addResourceHandler("/scripts/**").addResourceLocations("/static/scripts/").setCacheControl(cacheControl);
        registry.addResourceHandler("/style/**").addResourceLocations("/static/style/").setCacheControl(cacheControl);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/game").setViewName("game");
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
