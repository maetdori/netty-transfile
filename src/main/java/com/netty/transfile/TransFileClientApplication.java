package com.netty.transfile;

import com.netty.transfile.client.TransFileClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class TransFileClientApplication implements CommandLineRunner {
	private final TransFileClient transFileClient;

	public static void main(String[] args) {
		SpringApplication.run(TransFileClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		char input;

		log.info("client start.");

		while (true) {
			System.out.println("Insert 'C' to create file, and 'D' to delete file.");
			input = scanner.next().charAt(0);
			if (input == 'C' || input == 'D') {
				break;
			}

			System.out.println("Invalid request. Try again.\n");
		}

		transFileClient.connect();
		transFileClient.send(input);
	}
}
