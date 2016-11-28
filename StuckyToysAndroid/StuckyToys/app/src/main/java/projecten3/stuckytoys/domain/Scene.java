package projecten3.stuckytoys.domain;

import java.io.Serializable;
import java.util.List;

public class Scene implements Serializable{
    private String _id;
    private int sceneNr;
    private int layout;
    private String text;
    private List<String> hints;
    private List<Widget> widgets;

    public Scene() {

    }

    public Scene(String _id, int sceneNr, int layout, String text, List<String> hints, List<Widget> widgets) {
        this._id = _id;
        this.sceneNr = sceneNr;
        this.layout = layout;
        this.text = text;
        this.hints = hints;
        this.widgets = widgets;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getSceneNr() {
        return sceneNr;
    }

    public void setSceneNr(int sceneNr) {
        this.sceneNr = sceneNr;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

}
