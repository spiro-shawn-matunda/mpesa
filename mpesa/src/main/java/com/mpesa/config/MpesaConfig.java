package com.mpesa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class MpesaConfig {

    @Value("${mpesa.consumerKey}")
    private String consumerKey;

    @Value("${mpesa.consumerSecret}")
    private String consumerSecret;

    @Value("${mpesa.shortCode}")
    private String shortCode;


    @Value("${mpesa.passkey}")
    private String passkey;

    @Value("${mpesa.callbackUrl}")
    private String callbackUrl;

}
