package avicit.plm.core.accesscontrol.dispatcher.model;

import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfClassDescription;

import java.util.ArrayList;
import java.util.List;

public class InterfPackage {
    private String name ;
    private String parent ;
    private String description ;
    private List<InterfClazz> clazzes = new ArrayList<InterfClazz>();

    public InterfPackage(String name, String description) {
        this.name = name;
        this.description = (description==null||description.isEmpty()) ? name : description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<InterfClazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(List<InterfClazz> clazzes) {
        this.clazzes = clazzes;
    }

    /**
     * record a class for this package
     *
     * @param clazz
     */
    public void addClazz(Class<?> clazz) {
        String className = clazz.getName() ;
        String description = className ;
        InterfClassDescription interfClassDescription = clazz.getAnnotation(InterfClassDescription.class) ;
        if ( interfClassDescription != null) {
            description = interfClassDescription.value() ;
            if (description.isEmpty())
                description = className ;
        }
        clazzes.add(new InterfClazz(className, description)) ;
    }
}
