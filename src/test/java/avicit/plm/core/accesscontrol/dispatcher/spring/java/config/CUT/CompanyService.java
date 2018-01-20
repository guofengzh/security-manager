package avicit.plm.core.accesscontrol.dispatcher.spring.java.config.CUT;

import avicit.plm.core.accesscontrol.dispatcher.spring.java.config.PDM.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class CompanyService {
    /** Inject a bean in the parent context */
    @Autowired
    private CompanyRepository companyRepository;

    public String getCompnayName() {
        return companyRepository.getName() ;
    }

    @PostConstruct
    public void postConstruct() {
    }
}
