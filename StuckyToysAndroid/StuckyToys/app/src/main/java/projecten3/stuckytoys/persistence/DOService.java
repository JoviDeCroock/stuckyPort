package projecten3.stuckytoys.persistence;

import java.util.List;

import projecten3.stuckytoys.domain.Member;
import projecten3.stuckytoys.custom.member_related_stuff.RetrofitMember;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DOService {

    @GET("users")
    Call<List<User>> listAllUsers();

    @POST("login")
    Call<User> login(@Body User user);

    @POST("register")
    Call<User> register(@Body User user);

    @GET("profile/users/{user}/getAllMembers")
    Call<List<Member>> getAllMembers(@Path("user") String userId, @Header("Authorization") String token);

    @GET("story/getPublishedStories")
    Call<List<Story>> getPublishedStories(@Header("Authorization") String token);

    @GET("story/{user}/getAllStories")
    Call<List<String>> getUserStories(@Path("user") String userId, @Header("Authorization") String token);

    @POST("story/{user}/buyStory/{story}")
    Call<List<String>> buyStory(@Path("user") String userId, @Path("story") String storyId, @Header("Authorization") String token);

    @POST("profile/users/{user}/addMember")
    Call<Member> addMember(@Path("user") String userId, @Header("Authorization") String token, @Body RetrofitMember member);

}
