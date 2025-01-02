package com.exciting.vvue.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.exciting.vvue.common.interceptor.AccessTokenInterceptor;
import com.exciting.vvue.common.interceptor.RefreshTokenInterceptor;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

	// private final List<String> JWT_PATTERNS = Arrays.asList("/users/**", "/places-favorite/**", "/married-code/**"
	//         , "/married/**", "/memory/**", "/notify", "/notify/not-read", "/notify/read"
	//         , "/pictures/**", "/places/favorites", "/places/recommend", "/schedules/**"
	//         , "/notify/subscribe", "/notify/unsubscribe"
	// );
	private final List<String> JWT_EXCLUDE_PATTERNS = Arrays.asList(
		"/notify/users", "/notify/users/all", "/places", "/places/{placeId}", "/auth", "/auth/refresh-access-token"
	);
	private final List<String> SWAGGER_URL_PATTERNS = Arrays.asList(
		"/swagger-ui.html",   // Swagger UI 경로
		"/swagger-ui/**",
		"/v3/api-docs/**",     // Swagger JSON 경로
		"/swagger-resources/**",  // Swagger 자원 경로
		"/webjars/**"       // Swagger webjar 경로
	);

	private final AccessTokenInterceptor accessTokenInterceptor;
	private final RefreshTokenInterceptor refreshTokenInterceptor;

	public WebMvcConfiguration(AccessTokenInterceptor accessTokenInterceptor,
		RefreshTokenInterceptor refreshTokenInterceptor) {
		this.accessTokenInterceptor = accessTokenInterceptor;
		this.refreshTokenInterceptor = refreshTokenInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessTokenInterceptor)
			.excludePathPatterns(SWAGGER_URL_PATTERNS)
			.excludePathPatterns(JWT_EXCLUDE_PATTERNS);

		registry.addInterceptor(refreshTokenInterceptor)
			.excludePathPatterns(SWAGGER_URL_PATTERNS)
			.addPathPatterns("/auth/refresh-access-token");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("*") // Development environment
			.allowedMethods("*")
			.allowedHeaders("*")
			.exposedHeaders("Authorization", "refresh-token") // 필요한 경우 추가
			.allowCredentials(true);
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
