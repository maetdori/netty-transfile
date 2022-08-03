package com.nettytransfile;

import com.nettytransfile.client.Client;
import com.nettytransfile.define.RequestCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class TransFileClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TransFileClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("client start.");
		Client client = new Client("127.0.0.1", 8081);
		client.connect();
		client.send(RequestCmd.CREATE.getCode());
	}
}
