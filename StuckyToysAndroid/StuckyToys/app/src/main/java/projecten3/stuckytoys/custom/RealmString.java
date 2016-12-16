package projecten3.stuckytoys.custom;

import io.realm.RealmObject;

public class RealmString extends RealmObject {
    private String name;

    public RealmString() {
    }

    public RealmString(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
