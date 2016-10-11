package projecten3.stuckytoys.domain;

import projecten3.stuckytoys.persistence.PersistenceController;

public class DomainController {

    private static DomainController dc;
    private PersistenceController pc;

    private DomainController() {
        pc = new PersistenceController();
    }

    public static DomainController getInstance() {
        if (dc == null) {
            dc = new DomainController();
        }
        return dc;
    }

}
