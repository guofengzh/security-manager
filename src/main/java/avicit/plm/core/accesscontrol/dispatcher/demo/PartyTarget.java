package avicit.plm.core.accesscontrol.dispatcher.demo;

import avicit.plm.core.accesscontrol.dispatcher.RunIt;

import java.util.List;

public class PartyTarget {
    @RunIt
    public String exec(List<PlmAcPartyDTO> part) {
        return "1234" ;
    }

    @RunIt("POST")
    public String execPost(List<PlmAcPartyDTO> part) {
        return "9876" ;
    }
}
