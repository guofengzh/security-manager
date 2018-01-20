package avicit.plm.core.accesscontrol.dispatcher.javaconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

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
