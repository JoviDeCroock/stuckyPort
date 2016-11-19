package projecten3.stuckytoys.domain;

/**
 * Created by Jeroen on 11/13/2016.
 */
public class Theme {
    private String description;
    private String name;

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
