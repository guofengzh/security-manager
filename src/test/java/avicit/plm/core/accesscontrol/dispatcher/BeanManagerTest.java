package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.target.MockTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class BeanManagerTest {
    @Autowired
    BeanManager beanManager ;
    ObjectMapper objectMapper = new ObjectMapper() ;

    String clz = MockTarget.class.getCanonicalName() ;

    @Test
    public void invokeWithoutParamTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        String json = null ;
        Object ret = beanManager.invokeTarget(clz, "NOPARAM", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "5555");
    }

    @Test
    public void invokeWithoutBodyTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        queryParams.put("a","1") ;
        String json = null ;
        Object ret = beanManager.invokeTarget(clz, "NOBODY", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "6661");
    }

    @Test
    public void invokeWithNullBodyTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        String json = null ;
        Object ret = beanManager.invokeTarget(clz, "NULLBODY", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "666" + null);
    }

    @Test
    public void invokeWithNULLPriBODYTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        String json = null ;
        Object ret = beanManager.invokeTarget(clz, "NULLPriBODY", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "777" + null);
    }

    @Test
    public void invokeTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        String json = "{\"partId\":\"1\",\"name\":\"n\",\"weght\":1}" ;
        Object ret = beanManager.invokeTarget(clz, "GET", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "1234");
    }

    @Test
    public void invokeWithListParamTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        String json = "[{\"partId\":\"1\",\"name\":\"n\",\"weght\":1}]" ;
        Object ret = beanManager.invokeTarget(clz, "POST", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "9876");
    }

    @Test
    public void invokeWithQueryParamTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        queryParams.put("a","1") ;
        String json = "{\"partId\":\"1\",\"name\":\"n\",\"weght\":1}" ;
        Object ret = beanManager.invokeTarget(clz, "execPostQueryParams", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "qqqq" + "1");
    }

    @Test
    public void invokeWithHeadParamTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        Map<String, String> headers = new HashMap<>() ;
        Map<String, String> queryParams = new HashMap<>() ;
        headers.put("a","1") ;
        String json = "{\"partId\":\"1\",\"name\":\"n\",\"weght\":1}" ;
        Object ret = beanManager.invokeTarget(clz, "execPostHeaderParams", json,  queryParams, headers) ;
        Assert.assertEquals(ret, "hhhh" + "1");
    }
}
