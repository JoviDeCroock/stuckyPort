package projecten3.stuckytoys.domain;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import projecten3.stuckytoys.custom.RealmString;

public class User extends RealmObject{

    @PrimaryKey
    private String _id;
    private String email;
    private String username;
    private String password;
    private RealmList<Story> stories;
    private String token;
    private RealmList<RealmString> boughtStories;

    public User() {

    }
    //CONSTRUCTOR ONLY USED FOR LOGGING IN (RETROFIT); DON'T USE THIS ANYWHERE ELSE!!!
    public User(String email, String password) {
        this.email = email;
        this.password = password;

        //backend requires the field username to be entered, but it's actually email...
        this.username = email;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String id, String email, String username, String password, String token, RealmList<Story> stories, RealmList<RealmString> boughtStories) {
        this(username, email, password);
        this._id = id;
        this.token = token;
        this.stories = stories;
        this.boughtStories = boughtStories;
    }

    public void addBoughtStory(String storyId) {
        boughtStories.add(new RealmString(storyId));
    }

    public String getId() { return _id; }
    public void setId(String id) {
        this._id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public RealmList<Story> getStories() {
        return stories;
    }
    public void setStories(RealmList<Story> stories) {
        this.stories = stories;
    }
    public String getToken() { return token; }
    public void setToken(String token) {
        this.token = token;
    }
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }
    public RealmList<RealmString> getBoughtStories() { return boughtStories; }
    public void setBoughtStories(RealmList<RealmString> boughtStories) { this.boughtStories = boughtStories; }
}
