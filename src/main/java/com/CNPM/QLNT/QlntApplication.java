package com.CNPM.QLNT;

import com.CNPM.QLNT.controller.AdminController;
import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Inter.IRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class QlntApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(QlntApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println(LocalDate.now().getMonth());
	}
}
