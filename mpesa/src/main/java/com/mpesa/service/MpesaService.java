package com.mpesa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpesa.config.MpesaConfig;
import com.mpesa.dto.StkPushRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Service
public class MpesaService {

    final Environment environment;

    @Autowired
    private MpesaConfig config;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public MpesaService(Environment environment) {
        this.environment = environment;
    }

    public String getAccessToken() throws Exception {
        String credentials = config.getConsumerKey() + ":" + config.getConsumerSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials"))
                .header("Authorization", "Basic " + encodedCredentials)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(response.body()).get("access_token").asText();
    }

    public String initiateStkPush(mpesamapping mapping ) throws Exception {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        String password = Base64.getEncoder().encodeToString(
                (config.getShortCode() + config.getPasskey() + timestamp).getBytes(StandardCharsets.UTF_8)
        );


        //environment.getProperty("");

        StkPushRequest stkRequest = new StkPushRequest(
                config.getShortCode(),
                password,
                timestamp,
                "CustomerPayBillOnline",
                mapping.getAmount(),
                mapping.getPhone(),
                config.getShortCode(),
                mapping.getPhone(),
                config.getCallbackUrl(),
                "Account123",
                "Payment for goods"
        );



        String jsonBody = objectMapper.writeValueAsString(stkRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://sandbox.safaricom.co.ke/mpesa/b2c/v3/paymentrequest"))
                .header("Authorization", "Bearer " + getAccessToken())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
