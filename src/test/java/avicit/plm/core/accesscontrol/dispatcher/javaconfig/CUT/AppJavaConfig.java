package avicit.plm.core.accesscontrol.dispatcher.javaconfig.CUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan
public class AppJavaConfig {

    @Bean("companyService")
    public CompanyService companyService() {
        return new CompanyService() ;
    }
}
