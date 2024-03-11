package com.CNPM.QLNT;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class QlntApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(QlntApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
