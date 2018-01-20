package avicit.plm.core.accesscontrol.dispatcher;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.context.DefaultContextLoader;
import org.xeustechnologies.jcl.context.JclContext;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class LoaderTest {
    @Autowired
    ClassManager classManager ;

    @Test
    public void loaderTest() throws ClassNotFoundException {
        String clz = MockClass.class.getCanonicalName();
        classManager.addPath("target/test-classes");
        Object o = classManager.newInstance(clz);

        JarClassLoader jcl = classManager.loadedBy(clz) ;
        Assert.assertNotNull(jcl);

        Set<String> loadedClasses = classManager.getLoadedClasses(jcl) ;
        Assert.assertTrue(loadedClasses.contains(clz));

        classManager.removePath("target/test-classes") ;
        Assert.assertNull(classManager.loadedBy(clz));
        Assert.assertNull(classManager.getLoadedClasses(jcl));
    }
}
