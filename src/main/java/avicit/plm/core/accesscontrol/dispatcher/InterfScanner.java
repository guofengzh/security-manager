package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfClassDescription;
import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfPackageDescription;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        System.out.println("++++++++++++++:"+clazzes.size());

        InterfDescription interfDescription = new InterfDescription() ;
        for(Class<?> clazz : clazzes ) {
            Package pack = clazz.getPackage();
            interfDescription.addClass(clazz);
        }
        return interfDescription ;
    }
}
