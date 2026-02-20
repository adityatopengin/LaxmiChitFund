package com.laxmichitfund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enables background tasks (like refreshing prices every few seconds)
public class LaxmiChitFundApp {

    public static void main(String[] args) {
        SpringApplication.run(LaxmiChitFundApp.class, args);
        System.out.println("=================================================");
        System.out.println("💰 LAXMI CHIT FUND PLATFORM SUCCESSFULLY STARTED 💰");
        System.out.println("=================================================");
    }
}

