package com.exciting.vvue.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "cloud.gcp.firebase")
@Getter
@Setter
public class FirebaseProperties {
	private Resource serviceAccount;

}