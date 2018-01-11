package avicit.plm.core.accesscontrol.dispatcher.model;

public class InterfMethod {
    private String name ;
    private String description ;

    public InterfMethod(String name, String description) {
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

    @Override
    public String toString() {
        return "InterfMethod{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
