package by.bogda.jtafinally;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class JtaFinallyApplication {

    public static final String DESTINATION = "messages";

    public static void main(String[] args) {
        SpringApplication.run(JtaFinallyApplication.class, args);
    }
}
