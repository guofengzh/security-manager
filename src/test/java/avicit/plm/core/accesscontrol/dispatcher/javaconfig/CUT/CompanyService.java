package avicit.plm.core.accesscontrol.dispatcher.javaconfig.CUT;

import avicit.plm.core.accesscontrol.dispatcher.javaconfig.CompanyRepository;
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
