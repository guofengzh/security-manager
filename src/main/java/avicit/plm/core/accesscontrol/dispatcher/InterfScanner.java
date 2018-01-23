package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfClassDescription;
import avicit.plm.core.accesscontrol.dispatcher.annotation.RunIt;
import avicit.plm.core.accesscontrol.dispatcher.model.InterfClazz;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xeustechnologies.jcl.JarClassLoader;

import java.lang.reflect.Method;
import java.util.*;

@Component
public class InterfScanner {
    @Autowired
    ClassManager classManager ;

    public InterfDescription scan(String...rootPackage) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Collection<? extends ClassLoader> classLoaders = classManager.getClassLoaders() ;
        Reflections reflections  = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
                //.filterInputsBy(new FilterBuilder().includePackage(rootPackage)) // refolections:0.9.9 or above
                .filterInputsBy(createPackageFilter(rootPackage)) // reflections:0.9.8
                .addClassLoaders(new ArrayList<>(classLoaders)));

        Set<Class<?>> clazzes = reflections.getTypesAnnotatedWith(InterfClassDescription.class);

        InterfDescription interfDescription = new InterfDescription() ;
        for(Class<?> clazz : clazzes ) {
            Package pack = clazz.getPackage();
            InterfClazz interfClazz = interfDescription.addClass(clazz);
            for (Method method : clazz.getDeclaredMethods()) {
                RunIt runIt = method.getAnnotation(RunIt.class);
                if ( runIt == null)
                    continue;
                interfClazz.addMethod(method) ;
            }
        }
        return interfDescription ;
    }

    private FilterBuilder createPackageFilter(String...rootPackage) {
        FilterBuilder packageFilters = new FilterBuilder() ;
        for (String p : rootPackage) {
            String pRegex = (p + ".").replace(".", "\\.") + ".*";
            FilterBuilder.Include include = new FilterBuilder.Include(pRegex) ;
            packageFilters.add(include) ;
        }
        return  packageFilters ;
    }
}
