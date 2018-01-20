package avicit.plm.core.accesscontrol.dispatcher.spring.java.config.CUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * the component implementation Spring context
 */
@Configuration
//@ComponentScan
public class AppJavaConfig {

    @Bean("companyService")
    public CompanyService companyService() {
        return new CompanyService() ;
    }
}
