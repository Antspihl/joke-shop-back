package ee.veebiprojekt.veebiprojekttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        //ManagementWebSecurityAutoConfiguration.class
})
public class VeebiprojektTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(VeebiprojektTestApplication.class, args);
    }

}
