package uz.md.shopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@SpringBootApplication
@EnableScheduling
public class ShopAppApplication {
    public static void main(String[] args)  {
        SpringApplication.run(ShopAppApplication.class, args);
    }
}
