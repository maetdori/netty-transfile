package com.nettytransfile;

import com.nettytransfile.server.TransFileServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class TransFileServerApplication implements CommandLineRunner {

	private final TransFileServer transFileServer;

	public static void main(String[] args) {
		SpringApplication.run(TransFileServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("server start.");
		transFileServer.start();
	}
}
