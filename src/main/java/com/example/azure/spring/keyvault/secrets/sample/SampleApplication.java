// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.azure.spring.keyvault.secrets.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
//import com.azure.spring.cloud.autoconfigure.keyvault.secrets.AzureKeyVaultSecretAutoConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    @Value("${FOO}")
    private String fooFakeProp;

    @Value("${MYSQL-SERVER-FULL-NAME}")
    private String MYSQL_SERVER_FULL_NAME;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run(String[] args) {
        System.out.println("FOO Fake Property in application-az.yml : " + fooFakeProp);
        System.out.println("Secret MYSQL-SERVER-FULL-NAME in Azure Key Vault: " + MYSQL_SERVER_FULL_NAME);
    }

}