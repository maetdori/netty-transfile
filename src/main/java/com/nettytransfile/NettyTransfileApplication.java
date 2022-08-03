package com.nettytransfile;

import com.nettytransfile.server.TransFileServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class NettyTransfileApplication implements CommandLineRunner {

	private final TransFileServer transFileServer;

	public static void main(String[] args) {
		SpringApplication.run(NettyTransfileApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("번호를 선택해주세요. client=1, server=2");
		int read = br.read() - '0';

		switch (read) {
			case 1:
				log.info("client start.");
				// client
				break;
			case 2:
				// server
				log.info("server start.");
				transFileServer.start();
				break;
			default:
				break;
		}
	}
}
