package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfPackageDescription;
import avicit.plm.core.accesscontrol.dispatcher.model.InterfClazz;
import avicit.plm.core.accesscontrol.dispatcher.model.InterfPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InterfDescription {
    private Map<String, InterfPackage> packages = new HashMap<String, InterfPackage>();

    public Collection<InterfPackage> getInterfDescription() {
        return packages.values() ;
    }

    public InterfPackage addPackage(Package pakkage) {
        String packgeName = pakkage.getName() ;
        String description = packgeName ;
        InterfPackageDescription interfPackageDescription = pakkage.getAnnotation(InterfPackageDescription.class) ;
        if (interfPackageDescription != null ) {
            description = interfPackageDescription.value() ;
            if (description.isEmpty())
                description = packgeName ;
        }
        if (!packages.containsKey(packgeName)) {
            InterfPackage pakage = new InterfPackage(packgeName, description) ;
            packages.put(packgeName, pakage) ;
        }
        return packages.get(packgeName) ;
    }

    public InterfClazz addClass(Class<?> clazz) {
        Package pack = clazz.getPackage() ;
        InterfPackage interfPackage = packages.get(pack.getName()) ;
        if (interfPackage == null ) {
            interfPackage = addPackage(pack) ;
        }
        return interfPackage.addClazz(clazz) ;
    }
}
