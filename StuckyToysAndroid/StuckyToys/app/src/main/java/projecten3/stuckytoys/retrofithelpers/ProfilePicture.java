package projecten3.stuckytoys.retrofithelpers;

/**
 * Created by Jeroen on 11/13/2016.
 */
public class ProfilePicture {
    private String base64;

    public ProfilePicture(String base64) {
        this.base64 = base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public String toString() {
        return base64;
    }

}
