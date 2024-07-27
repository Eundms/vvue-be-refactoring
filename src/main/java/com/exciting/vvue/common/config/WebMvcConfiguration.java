package com.exciting.vvue.common.config;


import com.exciting.vvue.common.interceptor.Jwt2Interceptor;
import com.exciting.vvue.common.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    //TODO: http method check
    private final List<String> JWT_PATTERNS = Arrays.asList("/users/**", "/places-favorite/**", "/married-code/**"
            , "/married/**", "/memory/**", "/notify", "/notify/not-read", "/notify/read"
            , "/pictures/**", "/places/favorites", "/places/recommend", "/schedules/**"
            , "/notify/subscribe", "/notify/unsubscribe"
    );
    private final List<String> PATTERNS = Arrays.asList(
            "/notify/users", "/notify/users/all", "/places","/places/{placeId}");
    private JwtInterceptor jwtInterceptor;
    private Jwt2Interceptor jwt2Interceptor;

    public WebMvcConfiguration(JwtInterceptor jwtInterceptor, Jwt2Interceptor jwt2Interceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.jwt2Interceptor = jwt2Interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns(JWT_PATTERNS);
        registry.addInterceptor(jwt2Interceptor).addPathPatterns(PATTERNS);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedOrigins("http://localhost:3000", "https://vvue.site")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
