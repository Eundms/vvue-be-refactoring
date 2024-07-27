package com.exciting.vvue.notification.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "cloud.gcp.firebase")
@Getter
@Setter
public class FirebaseProperties {
    private Resource serviceAccount;

}