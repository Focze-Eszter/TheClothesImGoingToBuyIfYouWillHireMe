package com.EszterFocze.TCIGTB.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan({"com.EszterFocze.TCIGTB.common.entity", "com.EszterFocze.TCIGTB.admin.user"})
public class TCIGTBBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(TCIGTBBackEndApplication.class, args);
	}

}
