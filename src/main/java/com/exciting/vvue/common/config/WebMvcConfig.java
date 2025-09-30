package com.exciting.vvue.common.config;

import com.exciting.vvue.common.resolver.AuthenticatedMarriedArgumentResolver;
import com.exciting.vvue.common.resolver.AuthenticatedUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
    private final AuthenticatedMarriedArgumentResolver authenticatedMarriedArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
        resolvers.add(authenticatedMarriedArgumentResolver);
    }
}