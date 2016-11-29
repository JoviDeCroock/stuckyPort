package projecten3.stuckytoys.domain;

import java.util.Date;
import java.util.List;

import projecten3.stuckytoys.retrofithelpers.ProfilePicture;

public class Story {

    private String _id;
    private String name;
    private Date date;
    private ProfilePicture picture;
    private List<Scene> scenes;
    private Theme[] themes;
    private boolean purchased = false;
    private double price;

    public Story(String _id, String name, Date date, ProfilePicture picture, List<Scene> scenes, Theme[] themes, boolean purchased) {
        this._id = _id;
        this.name = name;
        this.date = date;
        this.picture = picture;
        this.scenes = scenes;
        this.themes = themes;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPicture() {
        return picture.toString();
    }

    public void setPicture(String picture) {
        this.picture.setBase64(picture);
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

    public Theme[] getThemes() {
        return themes;
    }

    public void setThemes(Theme[] themes) {
        this.themes = themes;
    }

    public boolean isPurchased() { return purchased; }

    public void setPurchased(boolean purchased) { this.purchased = purchased; }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
