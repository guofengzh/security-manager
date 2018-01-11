package avicit.plm.core.accesscontrol.dispatcher.target;

import avicit.plm.core.accesscontrol.dispatcher.annotation.RunIt;

import java.util.List;

public class PartyTarget {
    @RunIt
    public String exec(List<PlmAcPartyDTO> part) {
        return "1234" ;
    }

    /**
     * BODY:
     *    [{"partId":"1","name":"n","weght":1}]
     *
     * @param part
     * @return
     */
    @RunIt("POST")
    public String execPost(List<PlmAcPartyDTO> part) {
        return "9876" ;
    }
}
