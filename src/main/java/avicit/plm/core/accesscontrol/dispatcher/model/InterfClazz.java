package avicit.plm.core.accesscontrol.dispatcher.model;

import avicit.plm.core.accesscontrol.dispatcher.annotation.RunIt;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class InterfClazz {
    private String name ;
    private String description ;
    private Set<InterfMethod> methods = new TreeSet<InterfMethod>(new Comparator<InterfMethod>() {
        @Override
        public int compare(InterfMethod o1, InterfMethod o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    }) ;

    public InterfClazz(String name, String description) {
        this.name = name;
        this.description = description;
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

    public Set<InterfMethod> getMethods() {
        return methods;
    }

    public void setMethods(Set<InterfMethod> methods) {
        this.methods = methods;
    }

    public InterfMethod addMethod(Method method) {
        String methodName = method.getName() ;
        RunIt runIt = method.getAnnotation(RunIt.class) ;
        String methodDescription = runIt.description() ;
        if (methodDescription.isEmpty())
            methodDescription = methodName;
        InterfMethod interfMethod = new InterfMethod(methodName, methodDescription) ;
        methods.add(interfMethod) ;
        return  interfMethod ;
    }

    @Override
    public String toString() {
        return "InterfClazz{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", methods=" + methods +
                '}';
    }
}
