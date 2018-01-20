package avicit.plm.core.accesscontrol.dispatcher.spring.java.config.CUT;

import avicit.plm.core.accesscontrol.dispatcher.annotation.RunIt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * a component UI implementation that use Spring IoC
 */
public class SpringTarget {
    /** the parent spring context -- the context created by PDM */
    @Autowired
    private ApplicationContext applicationContext;

    @RunIt("runIt")
    public String runIt() throws ClassNotFoundException {
        AnnotationConfigApplicationContext subContext = new AnnotationConfigApplicationContext();
        subContext.setParent(applicationContext);
        subContext.setClassLoader(SpringTarget.class.getClassLoader());
        subContext.register(AppJavaConfig.class);  // initialize our spring context
        subContext.refresh();
        CompanyService companyService = (CompanyService)subContext.getBean(CompanyService.class) ;
        return companyService.getCompnayName() ;
    }
}
