// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.azure.spring.keyvault.secrets.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
//import com.azure.spring.cloud.autoconfigure.keyvault.secrets.AzureKeyVaultSecretAutoConfiguration;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

    // add a space between the $ and the {
    // https://stackoverflow.com/questions/20244696/could-not-resolve-placeholder-in-string-value
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${logging.level.org.springframework}")
    private String logLevelSpring;

    @Value("${MYSQL-SERVER-FULL-NAME}")
    private String MYSQL_SERVER_FULL_NAME;

    @Value("${titi}")
    private String titi;

    @Autowired
    private YAMLConfig myConfig;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    public void run(String[] args) {
        // System.out.println("FOO Fake Property in application-az.yml : " + fooFakeProp);
        System.out.println("Secret MYSQL-SERVER-FULL-NAME in Azure Key Vault: " + MYSQL_SERVER_FULL_NAME);
        System.out.println("JDBC URL from config file: " + url);
        System.out.println("Spring log level from config file: " + logLevelSpring);

        System.out.println("Titi:" + myConfig.getTiti());
        System.out.println("Titi Val:" + titi);

    }

}