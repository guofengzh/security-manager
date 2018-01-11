package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfClassDescription;
import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfPackageDescription;
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

import java.lang.reflect.Method;
import java.util.*;

@Component
public class InterfScanner {

    public InterfDescription scan(String...rootPackage) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections  = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
               .filterInputsBy(new FilterBuilder().includePackage(rootPackage)));

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
}
