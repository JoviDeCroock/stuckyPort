package projecten3.stuckytoys.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Theme extends RealmObject {

    @PrimaryKey
    private String name;
    private String description;

    public Theme() {

    }

    public Theme(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
