package avicit.plm.core.accesscontrol.dispatcher;

import com.google.common.base.Splitter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "components")
public class PlmInterfTargetRest {

    private static final Log logger = LogFactory.getLog(PlmInterfTargetRest.class);

    @Autowired
    private BeanManager beanManager ;

    @RequestMapping(value = "invokation", method = RequestMethod.GET)
    public ResponseEntity<?> invokationOnGet(HttpServletRequest request) {
        // got the component component class
        String clz = "avicit.plm.core.accesscontrol.dispatcher.demo.PartyTarget" ;

        Map<String, String> headerMap = getHeaderMap(request) ;
        String queryString = request.getQueryString() ;
        Map<String, String> queryStringMap = getQueryMap(queryString) ;
        try {
            Object retObj = beanManager.invokeTarget(clz,RequestMethod.GET.name(), null, queryStringMap, headerMap) ;
            return new ResponseEntity<Object>(retObj, HttpStatus.OK);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getTargetException().getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e ) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "invokation", method = RequestMethod.POST)
    public ResponseEntity<?> invokationOnPost(@RequestBody String body, HttpServletRequest request) {
        // got the component component class
        String clz = "avicit.plm.core.accesscontrol.dispatcher.demo.PartyTarget" ;

        Map<String, String> headerMap = getHeaderMap(request) ;
        String queryString = request.getQueryString() ;
        Map<String, String> queryStringMap = getQueryMap(queryString) ;
        try {
            Object retObj = beanManager.invokeTarget(clz,RequestMethod.POST.name(), body, queryStringMap, headerMap) ;
            return new ResponseEntity<Object>(retObj, HttpStatus.OK);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getTargetException().getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e ) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>() ;
        Enumeration<String> names = request.getHeaderNames() ;
        while (names.hasMoreElements()) {
            String name = names.nextElement() ;
            headerMap.put(name, request.getHeader(name)) ;
        }
        return headerMap ;
    }

    private static Map<String, String> getQueryMap(String in){
        if ( in == null || in.isEmpty() )
            return new HashMap<>() ;
        Map<String, String> selectorMap = Splitter.on("&").withKeyValueSeparator("=").split(in);
        Map<String, String> resultMap = new HashMap<>() ;
        for (Map.Entry<String, String> entry : selectorMap.entrySet()) {
            String key = entry.getKey().trim() ;
            String value = null;
            try {
                value = URLDecoder.decode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            resultMap.put(key, value) ;
        }
        return resultMap ;
    }
}
