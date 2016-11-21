package projecten3.stuckytoys.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import projecten3.stuckytoys.domain.Member;
import projecten3.stuckytoys.domain.RetrofitMember;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersistenceController {

    private Retrofit retrofit;
    private DOService dOService;

    public PersistenceController() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://188.166.173.147:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dOService = retrofit.create(DOService.class);


    }

    //can't remember why "throws IOException" is here...
    public Call<User> login(User user) throws IOException {
        Call<User> call = dOService.login(user);

        return call;
    }

    public Call<List<Member>> getAllMembers(String userId, String token) {
        Call<List<Member>> call = dOService.getAllMembers(userId, token);

        return call;
    }

    //TODO: doesn't use userId atm but should in the future
    public Call<List<Story>> getAllStories(String userId, String token) {
        Call<List<Story>> call = dOService.getAllStories(token);

        return call;
    }

    public Call<User> register(User user) {
        Call<User> call = dOService.register(user);

        return call;
    }

    public Call<Member> addMember(String userId, String token, RetrofitMember member) {
        Call<Member> call = dOService.addMember(userId, token, member);

        return call;
    }
}
