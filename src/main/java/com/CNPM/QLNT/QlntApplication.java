package com.CNPM.QLNT;

import com.CNPM.QLNT.services.Inter.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
