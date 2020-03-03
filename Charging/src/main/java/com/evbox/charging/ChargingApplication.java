package com.evbox.charging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  This class is the entry point for the application
 */

@SpringBootApplication
public class ChargingApplication {

    /**
     * Starts the spring boot application
     * @param args the args passed during start up
     */
    public static void main(String args[]) {
        SpringApplication.run(ChargingApplication.class, args);
    }
}
