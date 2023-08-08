package ru.taratonov.deal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Deal REST Api",
                description = "2 MVP level deal",
                version = "1.0.0",
                contact = @Contact(
                        name = "Taratonov Vadim",
                        email = "taratonovv8@bk.ru",
                        url = "https://github.com/VadimKiKi/deal"
                )
        )
)
public class DealApplication {

    public static void main(String[] args) {
        SpringApplication.run(DealApplication.class, args);
    }

}
