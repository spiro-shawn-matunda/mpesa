package com.mpesa.controller;

import com.mpesa.service.MpesaService;
import com.mpesa.service.mpesamapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mpesa")
public class MpesaController {

    @Autowired
    private MpesaService mpesaService;

    // Endpoint to initiate STK Push
    @PostMapping("/stkpush")
    public String stkPush(@RequestBody mpesamapping request) {
        try {
            return mpesaService.initiateStkPush(request);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Callback endpoint Safaricom will call after STK Push
    @PostMapping("/callback")
    public String handleCallback(@RequestBody String payload) {
        System.out.println("Received STK Callback: " + payload);
        // You could parse and persist this response here
        return "Callback received";
    }
}

