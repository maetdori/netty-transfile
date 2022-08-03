package com.nettytransfile.config;

import com.nettytransfile.server.TransFileServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

@Configuration
public class TransFileServerConfig {

	@Value("${trans.server.charset}")
	private Charset charset;

	@Bean(initMethod = "serverInit", destroyMethod = "serverDestroy")
	public TransFileServer transFileServer() {
		return new TransFileServer(charset);
	}
}
