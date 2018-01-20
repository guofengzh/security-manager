package avicit.plm.core.accesscontrol.dispatcher.spring.java.config;

import avicit.plm.core.accesscontrol.dispatcher.BeanManager;
import avicit.plm.core.accesscontrol.dispatcher.ClassManager;
import avicit.plm.core.accesscontrol.dispatcher.TestConfig;
import avicit.plm.core.accesscontrol.dispatcher.spring.java.config.CUT.SpringTarget;
import avicit.plm.core.accesscontrol.dispatcher.spring.java.config.PDM.JavaConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class,JavaConfig.class})
public class JavaConfigTest {
    @Autowired
    BeanManager beanManager ;
    @Autowired
    ClassManager classManager ;

    /**
     * We test that spring can work well when bean is loaded by standard classloader and
     * our curstomer class loaded - see
     *
     * @throws Exception
     */
    @Test
    public void testJavaConfig() throws Exception {
        classManager.addPath("target/test-classes");

        Map<String, String> headers = new HashMap<String, String>() ;
        Map<String, String> queryParams = new HashMap<String, String>() ;
        String json = null ;
        Object ret = beanManager.invokeTarget(SpringTarget.class.getCanonicalName(), "runIt", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "my-company");
    }
}
