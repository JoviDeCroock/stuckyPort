package projecten3.stuckytoys.domain;

import java.util.List;

public class User {

    private String _id;
    private String email;
    private String username;
    private String password;
    private List<Story> stories;
    private String token;
    private List<String> boughtStories;

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

    public User(String id, String email, String username, String password, String token, List<Story> stories, List<String> boughtStories) {
        this(username, email, password);
        this._id = id;
        this.token = token;
        this.stories = stories;
        this.boughtStories = boughtStories;
    }

    public void addBoughtStory(String storyId) {
        boughtStories.add(storyId);
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
    public List<Story> getStories() {
        return stories;
    }
    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
    public String getToken() { return token; }
    public void setToken(String token) {
        this.token = token;
    }
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }
    public List<String> getBoughtStories() { return boughtStories; }
    public void setBoughtStories(List<String> boughtStories) { this.boughtStories = boughtStories; }
}
