package avicit.plm.core.accesscontrol.dispatcher;

import avicit.plm.core.accesscontrol.dispatcher.demo.PlmAcPartyDTO;

import java.util.List;

public class MockTarget {
    @RunIt
    public String execGet(PlmAcPartyDTO part) {
        return "1234" ;
    }

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
