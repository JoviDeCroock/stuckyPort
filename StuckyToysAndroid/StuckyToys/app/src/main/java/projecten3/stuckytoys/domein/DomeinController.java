package projecten3.stuckytoys.domein;

import projecten3.stuckytoys.persistentie.PersistentieController;

public class DomeinController {

    private static DomeinController dc;
    private PersistentieController pc;

    private DomeinController() {
        pc = new PersistentieController();
    }

    public static DomeinController getInstance() {
        if (dc == null) {
            dc = new DomeinController();
        }
        return dc;
    }

}
