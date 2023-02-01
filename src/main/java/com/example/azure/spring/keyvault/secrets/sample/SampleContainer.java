// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.azure.spring.keyvault.secrets.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
//import com.azure.spring.cloud.autoconfigure.keyvault.secrets.AzureKeyVaultSecretAutoConfiguration;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SampleContainer {
    
    @Value("${logging.level.org.springframework}")
    private String logLevelSpring;

    @Value("${SPRING-DATASOURCE-PASSWORD}")
    private String SPRING_DATASOURCE_PASSWORD;

    @Value("${titi}")
    private String titi;

    @Autowired
    private YAMLConfig myConfig;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SampleContainer.class);
        app.run(args);
    }

    public String getLogLevelSpring() {
        return logLevelSpring;
    }

    public void setLogLevelSpring(String logLevelSpring) {
        this.logLevelSpring = logLevelSpring;
    }

    public String getSPRING_DATASOURCE_PASSWORD() {
        return SPRING_DATASOURCE_PASSWORD;
    }

    public void setSPRING_DATASOURCE_PASSWORD(String sPRING_DATASOURCE_PASSWORD) {
        SPRING_DATASOURCE_PASSWORD = sPRING_DATASOURCE_PASSWORD;
    }

    public String getTiti() {
        return titi;
    }

    public void setTiti(String titi) {
        this.titi = titi;
    }

    public YAMLConfig getMyConfig() {
        return myConfig;
    }

    public void setMyConfig(YAMLConfig myConfig) {
        this.myConfig = myConfig;
    }

    
}