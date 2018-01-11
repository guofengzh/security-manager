package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfClassDescription;
import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfPackageDescription;
import avicit.plm.core.accesscontrol.dispatcher.model.InterfClazz;
import avicit.plm.core.accesscontrol.dispatcher.model.InterfPackage;
import avicit.plm.core.accesscontrol.dispatcher.target.MockTarget;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class InferfDescriptionTest {

    @Autowired
    InterfScanner interfScanner ;

    InterfDescription interfDescription ;

    @Test
    public void interfDescriTest() {
        interfDescription = interfScanner.scan("avicit.plm.core.accesscontrol.dispatcher.target");
        Collection<InterfPackage> packages = interfDescription.getInterfDescription() ;
        System.out.println("**********:" + packages.size()) ;
        Assert.assertTrue(packages.size() == 1) ;
        InterfPackage p = packages.iterator().next() ;
        Assert.assertEquals(p.getName(), MockTarget.class.getPackage().getName());
        Assert.assertEquals(p.getDescription(),MockTarget.class.getPackage().getAnnotation(InterfPackageDescription.class).value());
        InterfClazz interfClazz = p.getClazzes().get(0) ;
        Assert.assertEquals(interfClazz.getName(), MockTarget.class.getName());
        Assert.assertEquals(interfClazz.getDescription(), MockTarget.class.getAnnotation(InterfClassDescription.class).value());
    }
}
