package projecten3.stuckytoys.domain;

import java.io.Serializable;
import java.util.List;

public class Widget implements Serializable{
    private String id;
    private List<WidgetFile> widgetFiles;

    public Widget() {

    }

    public Widget(String id, List<WidgetFile> widgetFiles) {
        this.id = id;
        this.widgetFiles = widgetFiles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<WidgetFile> getWidgetFiles() {
        return widgetFiles;
    }

    public void setWidgetFiles(List<WidgetFile> widgetFiles) {
        this.widgetFiles = widgetFiles;
    }
}
