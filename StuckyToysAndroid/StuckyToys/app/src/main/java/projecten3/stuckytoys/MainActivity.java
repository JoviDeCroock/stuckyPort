package projecten3.stuckytoys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Member;
import projecten3.stuckytoys.domain.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editEmail) EditText editEmail;
    @BindView(R.id.editPassword) EditText editPassword;
    @BindView(R.id.txtError) TextView txtError;
    @BindView(R.id.btnLogin) Button btnLogin;

    private DomainController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        editEmail.setText("swifties");
        editPassword.setText("swifty");
    }

    @OnClick(R.id.btnLogin)
    public void login(View view) {

        //for testing when server is offline; creates a user to work with
        //serverOffline();

        btnLogin.setClickable(false);

        final String email = editEmail.getText().toString();
        final String password = editPassword.getText().toString();

        try {
            Call<User> call = dc.login(email, password);

            call.enqueue(new Callback<User>() {

                //Call to API using Retrofit; User as model; Body requires username & password (for now, should be email);
                //API returns a token; token is used to authenticate. also decoded token = id + exp + iat
                //example token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODE3ODU0YjE0NTRjNDFhODJlNmM3NzgiLCJleHAiOjE0ODMxOTc2NjYsImlhdCI6MTQ3ODAxMzY2Nn0.iDs223_K8SrtQlDHos5k1r8uRh8Pzq4-axjvZRPID4o
                //decoded example token: {"_id":"5817854b1454c41a82e6c778","exp":1483197666,"iat":1478013666}
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        String token = response.body().getToken();
                        String tokenMiddle = token.split("\\.")[1];
                        String decoded = new String(Base64.decode(tokenMiddle, Base64.DEFAULT));
                        try {
                            JSONObject jObj = new JSONObject(decoded);
                            //"exp" & "iat" also in this jsonobject
                            String id = jObj.getString("_id");
                            dc.updateUser(id, email, token);

                            Log.d("login", "id: " + id + " token: " + token);

                            Intent intent = new Intent(MainActivity.this, SelectMemberActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        txtError.setText(response.message());
                        Log.e("login", response.code() + " " + response.message());
                    }
                    btnLogin.setClickable(true);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    txtError.setText("a");
                    t.printStackTrace();
                    btnLogin.setClickable(true);
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            btnLogin.setClickable(true);
        }
    }

    @OnClick(R.id.txtNotRegistered)
    public void register() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    //for testing while server is offline
    private void serverOffline() {
        dc.setUser(new User("5817854b1454c41a82e6c778", "swifties", "swifties", "swifty",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODE3ODU0YjE0NTRjNDFhODJlNmM3NzgiLCJleHAiOjE0ODMxOTc2NjYsImlhdCI6MTQ3ODAxMzY2Nn0.iDs223_K8SrtQlDHos5k1r8uRh8Pzq4-axjvZRPID4o",
                new ArrayList<Member>()));
            Intent intent = new Intent(MainActivity.this, SelectMemberActivity.class);

            startActivity(intent);

            finish();
    }
}
