package projecten3.stuckytoys.domain;

import java.util.List;

public class User {

    private String _id;
    private String email;
    private String username;
    private String password;
    private List<Member> members;
    private String token;

    //Constructor used for logging in (retrofit)
    public User(String email, String password) {
        this.email = email;
        this.password = password;

        //TEMPORARY (login should be with email but uses username atm
        username = email;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //TODO: username = email (will change!!)
    public User(String id, String email, String username, String password, String token, List<Member> members) {
        this(email, password);
        this._id = id;
        this.token = token;
    }


    public String getId() {
        return _id;
    }
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
    public List<Member> getMembers() {
        return members;
    }
    public void setMembers(List<Member> members) {
        this.members = members;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}
