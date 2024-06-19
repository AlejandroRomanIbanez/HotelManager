package com.entseeker.Hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelApplication {

	public static void main(String[] args) {
		DotenvConfig dotenvConfig = new DotenvConfig();
		dotenvConfig.dotenv();

		SpringApplication.run(HotelApplication.class, args);
	}

}
