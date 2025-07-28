package com.mpesa.service;

import lombok.Getter;

public class mpesamapping {


        private String phone;
        private int amount;

        // Getter and Setter for phone
        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        // Getter and Setter for amount
        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
