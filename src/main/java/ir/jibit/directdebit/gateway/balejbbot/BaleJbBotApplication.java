package ir.jibit.directdebit.gateway.balejbbot;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Getter
public class BaleJbBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaleJbBotApplication.class, args);
    }

}
