package com.example.SecureAndBox.login.api;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import com.example.SecureAndBox.SecureAndBox;

@Configuration
@EnableFeignClients(basePackageClasses = SecureAndBox.class)
public class FeignClientConfig {
}