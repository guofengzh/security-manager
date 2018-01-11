package avicit.plm.core.accesscontrol.dispatcher.target;

/**
 * [{"partId":"1","name":"n","weght":1}]
 */
public class PlmAcPartyDTO {
    private String partId ;
    private String name ;
    private int weght ;

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeght() {
        return weght;
    }

    public void setWeght(int weght) {
        this.weght = weght;
    }
}
