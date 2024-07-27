package com.exciting.vvue.married.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MarriedCode {
	private String marriedCode;

	@Builder
	public MarriedCode(String marriedCode){
		this.marriedCode = marriedCode;
	}
}
