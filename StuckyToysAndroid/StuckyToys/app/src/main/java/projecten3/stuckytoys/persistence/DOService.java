package projecten3.stuckytoys.persistence;

import java.util.List;

import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import projecten3.stuckytoys.retrofithelpers.StoryHelper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DOService {

    @POST("login")
    Call<User> login(@Body User user);

    @POST("register")
    Call<User> register(@Body User user);

    @GET("story/getPublishedStories")
    Call<List<Story>> getPublishedStories(@Header("Authorization") String token);

    @GET("story/{user}/getAllStories")
    Call<List<String>> getUserStories(@Path("user") String userId, @Header("Authorization") String token);

    @POST("story/{user}/buyStory/{story}")
    Call<List<String>> buyStory(@Path("user") String userId, @Path("story") String storyId, @Header("Authorization") String token);

    @GET("story/getStory/{storyId}")
    Call<StoryHelper> getStory(@Path("storyId") String storyId, @Header("Authorization") String token);

}
