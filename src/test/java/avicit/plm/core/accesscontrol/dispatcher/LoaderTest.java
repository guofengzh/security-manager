package avicit.plm.core.accesscontrol.dispatcher;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class LoaderTest {
    @Autowired
    ClassManager classManager ;

    @Test
    @Ignore
    public void loaderTest() {
        String clz = "com.avicit.mybatis.tutorial.App";
        try {
            Object o = classManager.newInstance(clz);
            System.out.println("Load success1");
        } catch (Exception e) {
            // should throw exception
            e.printStackTrace();
        }

        System.out.println("Please copy the jar to lib");
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        classManager.addPath("D:/projects/PDM/security-component/lib/");

        JclObjectFactory factory = JclObjectFactory.getInstance();

        System.out.println("Load again");
        try {
            Object o = classManager.newInstance(clz);
            System.out.println("Load success2");
        } catch (Exception e) {
            // should throw exception
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
