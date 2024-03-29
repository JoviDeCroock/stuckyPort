package projecten3.stuckytoys.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import io.realm.RealmConfiguration;
import io.realm.RealmList;
import projecten3.stuckytoys.custom.RealmString;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import projecten3.stuckytoys.retrofithelpers.StoryHelper;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersistenceController {

    private Retrofit retrofit;
    private DOService dOService;
    public final static String BASEURL = "http://188.166.173.147:3000/";
    public static boolean internetConnection = true;
    public final static RealmConfiguration CONFIG = new RealmConfiguration
            .Builder()
            .deleteRealmIfMigrationNeeded()
            .build();

    public PersistenceController() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>(){}.getType(),
                        RealmStringListTypeAdapter.INSTANCE)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        dOService = retrofit.create(DOService.class);


    }

    //can't remember why "throws IOException" is here...
    public Call<User> login(User user) throws IOException {
        Call<User> call = dOService.login(user);

        return call;
    }

    public Call<List<Story>> getPublishedStories(String token) {
        Call<List<Story>> call = dOService.getPublishedStories(token);
        return call;
    }

    public Call<List<String>> getUserStories(String userId, String token) {
        Call<List<String>> call = dOService.getUserStories(userId, token);
        return call;
    }

    public Call<User> register(User user) {
        Call<User> call = dOService.register(user);
        return call;
    }

    public Call<List<String>> buyStory(String userId, String storyId, String token) {
        Call<List<String>> call = dOService.buyStory(userId, storyId, token);
        return call;
    }

    public Call<StoryHelper> getStory(String storyId, String token)
    {
        return dOService.getStory(storyId, token);
    }

    /* MEMBERS
    public Call<Member> addMember(String userId, String token, RetrofitMember member) {
        Call<Member> call = dOService.addMember(userId, token, member);
        return call;
    }
    */

}
