package com.example.azure.spring.keyvault.secrets.sample;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {
 
    private String titi;

    private String loggingLevelRoot;
    private String springDatasourceUrl;
    // private List<String> servers = new ArrayList<>();

    public String getLoggingLevelRoot() {
        return loggingLevelRoot;
    }
    public void setLoggingLevelRoot(String loggingLevelRoot) {
        this.loggingLevelRoot = loggingLevelRoot;
    }
    public String getSpringDatasourceUrl() {
        return springDatasourceUrl;
    }
    public void setSpringDatasourceUrl(String springDatasourceUrl) {
        this.springDatasourceUrl = springDatasourceUrl;
    }

    public String getTiti() {
        return titi;
    }
    public void setTiti(String titi) {
        this.titi = titi;
    }
    
}