package projecten3.stuckytoys.domain;

import java.util.Date;
import java.util.List;

public class Member {

    private String _id;
    private String firstName;
    private String nickname;
    private String role;
    private boolean authority;
    private Date dateOfBirth;
    private String picture;
    private List<Story> stories;

    //int might change into a string (Base64 image)
    //private int pictureId;

    public Member() {

    }

    public Member(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;
    }

    public Member(String id, String firstName, String nickname, String role, boolean authority, Date dateOfBirth, String picture, List<Story> stories) {
        this._id = id;
        this.firstName = firstName;
        this.nickname = nickname;
        this.role = role;
        this.authority = authority;
        this.dateOfBirth = dateOfBirth;
        this.picture = picture;
        this.stories = stories;
    }


    public String getId() { return _id; }
    public void setId(String id) {
        this._id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isAuthority() {
        return authority;
    }
    public void setAuthority(boolean authority) {
        this.authority = authority;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture.split("\\.")[0]; }
    public List<Story> getStories() { return stories; }
    public void setStories(List<Story> stories) { this.stories = stories; }
}
