package de.mwillkomm.vatid.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("de.mwillkomm.vatid")
public class VatIdValidatorApplication {

	private static final Logger log = LoggerFactory.getLogger(VatIdValidatorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(VatIdValidatorApplication.class, args);
		log.info("application {} ready", VatIdValidatorApplication.class);
	}

}
