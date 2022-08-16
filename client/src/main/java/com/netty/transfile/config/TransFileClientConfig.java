package com.netty.transfile.config;

import com.netty.transfile.client.TransFileClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

@Configuration
public class TransFileClientConfig {

    @Value("${trans.client.charset}")
    private Charset charset;

    @Bean
    public TransFileClient transFileClient() {
        return new TransFileClient(charset);
    }
}
