package avicit.plm.core.accesscontrol.dispatcher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.exception.JclException;

import java.util.*;

@Component
public class ClassManager {
    private JclObjectFactory factory = JclObjectFactory.getInstance();
    private JarClassLoader defaultClassLoader = new JarClassLoader() ;
    private final Map<String, JarClassLoader> loaders = Collections
            .synchronizedMap( new HashMap<String, JarClassLoader>() );

    private final Map<String, JarClassLoader> classesNLoader = Collections
            .synchronizedMap( new HashMap<String, JarClassLoader>() );

    private final Map<JarClassLoader, Set<String>> loaderNClasses = Collections
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
            jcl.unloadClass(c);
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
            JarClassLoader jcl = classesNLoader.get(className) ;
            return factory.create(jcl, className, args) ;
        }

        if (loaders.isEmpty()) {
            // no class loader customized - this also means that no user-defined component implentations are used.
            // use default class loader
            Object o =  factory.create(defaultClassLoader, className, args) ;
            // so this class will always be loaded by defaultClassLoader
            cacheLoader(defaultClassLoader, className) ;
            return o ;
        }

        // iterate all loaders to find a loader
        Exception ex = new IllegalStateException("No class loader configured");
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
     * get the classloader
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
     * the current class loaders
     *
     * @return
     */
    public Collection<? extends ClassLoader> getClassLoaders() {
        return loaders.values() ;
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

    /**
     * TODO - sample here
     *
     * @param context
     * @param classLoader
     */
    public void createSubContext(ApplicationContext context, JarClassLoader classLoader) {
        AnnotationConfigApplicationContext subContext = new AnnotationConfigApplicationContext();
        subContext.setClassLoader(classLoader);
        subContext.setParent(context);

        /*
        Properties properties = new Properties();
        properties.setProperty("dbName", dbName);

        PropertiesPropertySource propertySource = new PropertiesPropertySource("dbProperties", properties);

        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addLast(propertySource);

        subContext.setEnvironment(env);
        subContext.register(JpaConfig.class);
        subContext.refresh();
        */
    }

    /*
        @Configuration
        @ComponentScan
        @EnableJpaRepositories
        @EnableAutoConfiguration
        public class JpaConfig {

            @Value("${dbName}")
            private String dbName;

            @Bean
            DataSource dataSource() {
                return DataSourceBuilder.create()
                        .url("jdbc:h2:mem:" + dbName)
                        .username("sa")
                        .password("")
                        .build();
            }
        }
     */
}
