package avicit.plm.core.accesscontrol.dispatcher;

import org.springframework.stereotype.Component;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.exception.JclException;

import java.util.*;

@Component
public class ClassManager {
    private JclObjectFactory factory = JclObjectFactory.getInstance();
    private static final Map<String, JarClassLoader> loaders = Collections
            .synchronizedMap( new HashMap<String, JarClassLoader>() );

    private static final Map<String, JarClassLoader> classesNLoader = Collections
            .synchronizedMap( new HashMap<String, JarClassLoader>() );

    private static final Map<JarClassLoader, Set<String>> loaderNClasses = Collections
            .synchronizedMap( new HashMap<JarClassLoader, Set<String>>() );

    /**
     * load classes from a directory or jar file
     *
     * @param pathOrJsr
     */
    public void addPath(String pathOrJsr) {
        JarClassLoader jcl = null ;
        if (!loaders.containsKey(pathOrJsr)) {
            jcl = new JarClassLoader();
            jcl.add(pathOrJsr);
            loaders.put(pathOrJsr, jcl) ;
        }
    }

    /**
     * remove class path
     *
     * @param pathOrJsr
     */
    public void removePath(String pathOrJsr) {
        // remove the loader
        JarClassLoader jcl = loaders.remove(pathOrJsr) ;
        if ( jcl == null )
            return ;

        // remove cached loadded classes map
        Set<String> loadedClasses = loaderNClasses.remove(jcl) ;
        if (loadedClasses == null )
            return;
        for (String c : loadedClasses ) {
            classesNLoader.remove(c) ;
        }
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
        // see it this class loaded before
        if (classesNLoader.containsKey(className) ) {
            JarClassLoader jarClassLoader = classesNLoader.get(className) ;
            return factory.create(jarClassLoader, className, args) ;
        }

        // iterate all loaders to find a loader
        JclException ex = null ;
        for (JarClassLoader jcl : loaders.values()) {
            try {
                Object o = factory.create(jcl, className, args);
                cacheLoader(jcl, className) ;
                return o ;
            } catch (JclException e ) {
                ex = e ;
            }
        }
        throw new ClassNotFoundException(ex.getMessage(), ex) ;
    }

    /**
     * get the class loader
     *
     * @param clz
     * @return
     */
    public JarClassLoader loadedBy(String clz) {
        return classesNLoader.get(clz) ;
    }

    /**
     * get locaded classes by the loader
     *
     * @param jcl
     * @return
     */
    public Set<String> getLoadedClasses(JarClassLoader jcl) {
        return loaderNClasses.get(jcl) ;
    }

    /**
     * set up the cache
     * 
     * @param jcl
     * @param className
     */
    private void cacheLoader(JarClassLoader jcl, String className) {
        // class and its loader
        classesNLoader.put(className, jcl) ;

        // loader and tis loaded classes
        Set<String> classes = null ;
        if (loaderNClasses.containsKey(jcl)) {
            classes = loaderNClasses.get(jcl) ;
        } else {
            classes = new HashSet<String>() ;
            loaderNClasses.put(jcl, classes) ;
        }
        classes.add(className) ;
    }
}
