package projecten3.stuckytoys.domain;

import java.io.IOException;
import java.util.List;

import projecten3.stuckytoys.persistence.PersistenceController;
import retrofit2.Call;

public class DomainController {

    private static DomainController dc;
    private PersistenceController pc;
    private User user;

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

    public Call<List<Member>> getAllMembers() {
        return pc.getAllMembers(user.getId(), "Bearer " + user.getToken());
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void updateUser(String id, String password, String token) {
        user.setId(id);
        user.setPassword(password);
        user.setToken(token);
    }
}
