package com.exciting.vvue.auth.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.exciting.vvue.auth.dto.OAuthUserInfoDto;
import com.exciting.vvue.auth.model.strategy.OAuthUserInfoStrategy;

@Component
public class OAuthUserInfoFactory {

	private final Map<String, OAuthUserInfoStrategy> strategyMap;

	public OAuthUserInfoFactory(List<OAuthUserInfoStrategy> strategies) {
		this.strategyMap = strategies.stream()
			.collect(Collectors.toMap(
				s -> s.getClass().getAnnotation(Component.class).value().toUpperCase(),
				Function.identity()
			));
	}

	public OAuthUserInfo create(OAuthUserInfoDto dto) {
		OAuthUserInfoStrategy strategy = strategyMap.get(dto.getProvider().toUpperCase());
		if (strategy == null) {
			throw new IllegalArgumentException("지원하지 않는 provider: " + dto.getProvider());
		}
		return strategy.create(dto);
	}
}
