package avicit.plm.core.accesscontrol.dispatcher.spring.java.config.PDM;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * A Spring JavaConfig
 */
@Configuration
public class JavaConfig {

    @Bean
    CompanyRepository companyRepository() {
        return new CompanyRepository() ;
    }
}
