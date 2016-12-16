package projecten3.stuckytoys.domain;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Widget extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;
    private RealmList<WidgetFile> widgetFiles;

    public Widget() {

    }

    public Widget(String id, RealmList<WidgetFile> widgetFiles) {
        this.id = id;
        this.widgetFiles = widgetFiles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<WidgetFile> getWidgetFiles() {
        return widgetFiles;
    }

    public void setWidgetFiles(RealmList<WidgetFile> widgetFiles) {
        this.widgetFiles = widgetFiles;
    }
}
