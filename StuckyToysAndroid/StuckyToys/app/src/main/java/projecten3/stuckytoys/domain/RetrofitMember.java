package projecten3.stuckytoys.domain;

public class RetrofitMember {
    //RetrofitMember is only used when a new member is added, see DOService: @POST("profile/users/{user}/addMember")
    //when a new member is added the backend asks for a birthdate string (ex. "12 06 1998")
    //while in all other cases (also when you GET members from the server) this is a Date, not a String

    String firstName, nickname, role, dateOfBirth, picture;
    boolean authority;

    public RetrofitMember(String firstName, String nickname, String role, boolean authority, String dateOfBirth, String picture) {
        this.firstName = firstName;
        this.nickname = nickname;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.picture = picture;
        this.authority = authority;
    }
}
