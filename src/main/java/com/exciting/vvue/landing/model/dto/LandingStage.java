package com.exciting.vvue.landing.model.dto;


import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LandingStage {
	private String stage;

	public LandingStage(String stage) {
		this.stage = stage;
	}
}
