package com.btc.bootcamp.ludo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.btc.bootcamp.ludo.business.SetupService;
import com.btc.bootcamp.ludo.presentation.PresentationService;

@SpringBootApplication
public class ApplicationMain implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationMain.class);

    @Autowired
    private SetupService setupService;

    @Autowired
    private PresentationService presentationService;

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(ApplicationMain.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        // Hier gehts los...

        setupService.setup();

        // Irgendwas...
    }
}
