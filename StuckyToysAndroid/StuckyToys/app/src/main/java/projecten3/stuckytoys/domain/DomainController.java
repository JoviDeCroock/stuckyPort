package projecten3.stuckytoys.domain;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import projecten3.stuckytoys.persistence.PersistenceController;
import retrofit2.Call;

public class DomainController {

    private static DomainController dc;
    private PersistenceController pc;
    private User user;
    private Member member;

    private DomainController() {
        pc = new PersistenceController();
    }

    public static DomainController getInstance() {
        if (dc == null) {
            dc = new DomainController();
        }
        return dc;
    }

    public Call<User> login(String email, String password) throws IOException {
        user = new User(email, password);
        return pc.login(user);
    }

    public Call<User> register(String username, String email, String password) {
        user = new User(username, email, password);

        return pc.register(user);
    }

    public Call<List<Member>> getAllMembers() {
        return pc.getAllMembers(user.getId(), "Bearer " + user.getToken());
    }

    public Call<List<Story>> getAllStories() {
        return pc.getAllStories(user.getId(), "Bearer " + user.getToken());
    }

    public Call<Member> addMember(String firstName, String nickname, String role, boolean authority, String dateOfBirth, String picture) {
        RetrofitMember member = new RetrofitMember(firstName, nickname, role, authority, dateOfBirth, picture);

        return pc.addMember(user.getId(), "Bearer " + user.getToken(), member);
    }

    public User getUser() { return user; }
    public void setUser(User user) {
        this.user = user;
    }

    public Member getMember() {return member;}
    public void setMember(Member member){this.member = member;}

    //called after registering
    public void updateUser(String id, String token) {
        user.setId(id);
        user.setToken(token);
    }

    //called after logging in
    public void updateUser(String id, String token, String username) {
        user.setId(id);
        user.setToken(token);
        user.setUsername(username);
    }
}
