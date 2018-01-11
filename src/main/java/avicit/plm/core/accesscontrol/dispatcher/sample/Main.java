package avicit.plm.core.accesscontrol.dispatcher.sample;

import avicit.plm.core.accesscontrol.dispatcher.InterfDescription;
import avicit.plm.core.accesscontrol.dispatcher.InterfScanner;
import avicit.plm.core.accesscontrol.dispatcher.model.InterfPackage;

import java.util.Collection;

/**
 *  mvn exec:java -Dexec.mainClass="avicit.plm.core.accesscontrol.dispatcher.Main"
 */
public class Main {
    public static void main(String[] args) {
        InterfDescription interfDescription = new InterfScanner().scan("avicit.plm.core.accesscontrol.dispatcher.sample");
        Collection<InterfPackage> packages = interfDescription.getInterfDescription() ;
        System.out.println(packages);
    }
}
