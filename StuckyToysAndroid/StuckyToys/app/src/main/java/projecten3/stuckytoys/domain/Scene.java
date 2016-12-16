package projecten3.stuckytoys.domain;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import projecten3.stuckytoys.custom.RealmString;

public class Scene extends RealmObject implements Serializable{
    @PrimaryKey
    private String _id;
    private int sceneNr;
    private int layout;
    private String text;
    private RealmList<RealmString> hints;
    private RealmList<Widget> widgets;

    public Scene() {

    }

    public Scene(String _id, int sceneNr, int layout, String text, RealmList<RealmString> hints, RealmList<Widget> widgets) {
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

    public RealmList<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(RealmList<Widget> widgets) {
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

    public RealmList<RealmString> getHints() {
        return hints;
    }

    public void setHints(RealmList<RealmString> hints) {
        this.hints = hints;
    }

}
