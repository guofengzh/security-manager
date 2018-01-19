package avicit.plm.core.accesscontrol.dispatcher;

import org.springframework.stereotype.Component;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.exception.JclException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClassManager {
    private JclObjectFactory factory = JclObjectFactory.getInstance();
    private static final Map<String, JarClassLoader> loaders = Collections
            .synchronizedMap( new HashMap<String, JarClassLoader>() );

    /**
     * load classes from a directory or jar file
     *
     * @param pathOrJsr
     */
    public synchronized void addPath(String pathOrJsr) {
        JarClassLoader jcl = null ;
        if (!loaders.containsKey(pathOrJsr)) {
            jcl = new JarClassLoader();
            jcl.add(pathOrJsr);
            loaders.put(pathOrJsr, jcl) ;
        }
    }

    public synchronized void removePath(String pathOrJsr) {
        loaders.remove(pathOrJsr) ;
    }

    /**
     * instantiate a instance
     *
     * @param className
     * @return the object
     */
    public Object newInstance(String className) throws ClassNotFoundException {
        return newInstance( className, (Object[]) null );
    }

    /**
     * instantiate a instance
     *
     * @param className
     * @param args
     * @return
     */
    public Object newInstance(String className, Object... args) throws ClassNotFoundException {
        JclException ex = null ;
        for (JarClassLoader jcl : loaders.values()) {
            try {
                return factory.create(jcl, className, args);
            } catch (JclException e ) {
                ex = e ;
            }
        }
        throw new ClassNotFoundException(ex.getMessage(), ex) ;
    }
}
