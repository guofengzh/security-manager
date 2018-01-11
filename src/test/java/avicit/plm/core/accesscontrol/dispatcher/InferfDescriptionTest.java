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
    InterfScanner interfScanner ;;

    @Test
    public void interfDescriTest() {
        InterfDescription interfDescription = interfScanner.scan("avicit.plm.core.accesscontrol.dispatcher.target");
        Collection<InterfPackage> packages = interfDescription.getInterfDescription() ;
        Assert.assertTrue(packages.size() == 1) ;
        InterfPackage p = packages.iterator().next() ;
        Assert.assertEquals(p.getName(), MockTarget.class.getPackage().getName());
        Assert.assertEquals(p.getDescription(),MockTarget.class.getPackage().getAnnotation(InterfPackageDescription.class).value());
        InterfClazz interfClazz = p.getClazzes().iterator().next() ;
        Assert.assertEquals(interfClazz.getName(), MockTarget.class.getName());
        Assert.assertEquals(interfClazz.getDescription(), MockTarget.class.getAnnotation(InterfClassDescription.class).value());

        Assert.assertTrue(interfClazz.getMethods().size() == 8);
    }
}
