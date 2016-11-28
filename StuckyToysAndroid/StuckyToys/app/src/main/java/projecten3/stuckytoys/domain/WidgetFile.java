package projecten3.stuckytoys.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kevin on 27/11/2016.
 */

public class WidgetFile implements Serializable{
    private String _id;
    private String type;
    private String fileName;

    public WidgetFile() {

    }

    public WidgetFile(String _id, String type, String fileName) {
        this._id = _id;
        this.type = type;
        this.fileName = fileName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
