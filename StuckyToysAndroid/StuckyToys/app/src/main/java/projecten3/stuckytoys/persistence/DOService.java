package projecten3.stuckytoys.persistence;

import java.util.List;

import projecten3.stuckytoys.domain.Member;
import projecten3.stuckytoys.domain.RetrofitMember;
import projecten3.stuckytoys.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
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

    @POST("profile/users/{user}/addMember")
    Call<Member> addMember(@Path("user") String userId, @Header("Authorization") String token, @Body RetrofitMember member);

}
