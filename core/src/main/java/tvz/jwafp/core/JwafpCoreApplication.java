package tvz.jwafp.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JwafpCoreApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JwafpCoreApplication.class);
        app.setAdditionalProfiles("jwafp");
        app.run(args);
    }
}
