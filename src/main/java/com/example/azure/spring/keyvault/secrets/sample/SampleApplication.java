// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.azure.spring.keyvault.secrets.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    @Value("${FOO}") // ${MYSQL-SERVER-FULL-NAME}"
    private String springDataSourceUrl;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run(String[] args) {
        System.out.println("Secret MYSQL-SERVER-FULL-NAME in Azure Key Vault: " + springDataSourceUrl);
    }

}