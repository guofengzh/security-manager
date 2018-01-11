package avicit.plm.core.accesscontrol.dispatcher.model;

import avicit.plm.core.accesscontrol.dispatcher.annotation.InterfClassDescription;

import java.util.*;

public class InterfPackage {
    private String name ;
    private String description ;
    private Set<InterfClazz> clazzes = new TreeSet<InterfClazz>(new Comparator<InterfClazz>() {
        @Override
        public int compare(InterfClazz o1, InterfClazz o2) {
            return o1.getDescription().compareTo(o2.getDescription()) ;
        }
    });

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InterfClazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(Set<InterfClazz> clazzes) {
        this.clazzes = clazzes;
    }

    /**
     * record a class for this package
     *
     * @param clazz
     */
    public InterfClazz addClazz(Class<?> clazz) {
        String className = clazz.getName() ;
        String description = className ;
        InterfClassDescription interfClassDescription = clazz.getAnnotation(InterfClassDescription.class) ;
        if ( interfClassDescription != null) {
            description = interfClassDescription.value() ;
            if (description.isEmpty())
                description = className ;
        }
        InterfClazz interfClazz = new InterfClazz(className, description) ;
        clazzes.add(interfClazz) ;
        return interfClazz ;
    }

    @Override
    public String toString() {
        return "InterfPackage{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", clazzes=" + clazzes +
                '}';
    }
}
