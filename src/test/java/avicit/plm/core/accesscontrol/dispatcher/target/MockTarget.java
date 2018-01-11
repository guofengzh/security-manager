package avicit.plm.core.accesscontrol.dispatcher.target;

import avicit.plm.core.accesscontrol.dispatcher.annotation.*;

import java.util.List;

@InterfClassDescription("The Interf Class Description")
public class MockTarget {
    /**
     * Body:
     *   {"partId":"1","name":"n","weght":1}
     *
     * @param part
     * @return
     */
    @RunIt
    public String execGet(PlmAcPartyDTO part) {
        return "1234" ;
    }

    /**
     * Body:
     *   [{"partId":"1","name":"n","weght":1}]
     *
     * @param part
     * @return
     */
    @RunIt("POST")
    public String execPost(List<PlmAcPartyDTO> part) {
        return "9876" ;
    }

    @RunIt("NOPARAM")
    public String execNoParam() {
        return "5555" ;
    }

    @RunIt("NOBODY")
    public String execNoBodyt(@QueryParam("a") Integer  a) {
        return "666" + a ;
    }

    @RunIt("NULLBODY")
    public String execNullBodyt(PlmAcPartyDTO  a) {
        return "666" + a ;
    }

    @RunIt("NULLPriBODY")
    public String execNullPriBodyt(Integer  a) {
        return "777" + a ;
    }

    @RunIt("execPostQueryParams")
    public String execPostQueryParams(PlmAcPartyDTO part, @QueryParam("a") Integer  a) {
        return "qqqq" + a ;
    }

    @RunIt("execPostHeaderParams")
    public String execPostHeaderParams(@RequestBody PlmAcPartyDTO part, @RequestHeader("a") int  a) {
        return "hhhh" + a;
    }
}
