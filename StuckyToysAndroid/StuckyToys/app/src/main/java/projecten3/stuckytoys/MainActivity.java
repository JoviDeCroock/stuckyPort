package projecten3.stuckytoys;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.custom.ResourceHelper;
import projecten3.stuckytoys.custom.ServerOfflineHelper;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Member;
import projecten3.stuckytoys.domain.Story;
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

        editEmail.setText("jeroen@gmail.com");
        editPassword.setText("jeroen");
    }

    @OnClick(R.id.btnLogin)
    public void login(View view) {

        //for testing when server is offline; creates a user to work with
        if (ServerOfflineHelper.SERVEROFFLINE) {
            serverOffline();
        }

        //once server is online 24/7 this is all we'll need & if-structure above can be deleted
        else {
            txtError.setText("");
            btnLogin.setClickable(false);

            final String email = editEmail.getText().toString();
            final String password = editPassword.getText().toString();

            //alles ingevuld?
            if( email.isEmpty() || password.isEmpty()) {
                txtError.setText(R.string.fill_all_fields);
                return;
            }

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
                                String username = jObj.getString("username");
                                dc.updateUser(id, token, username);

                                Log.d("login", "id: " + id + " token: " + token);

                                getUserStories();
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
                        txtError.setText(R.string.connection_error);
                        t.printStackTrace();
                        btnLogin.setClickable(true);
                    }
                });

            } catch (IOException ex) {
                ex.printStackTrace();
                btnLogin.setClickable(true);
            }
        }
    }

    private void getUserStories() {
        Call<List<String>> call = dc.getUserStories();

        call.enqueue(new Callback<List<String>>() {

            //Call to API using Retrofit; User as model; Body requires username & password (for now, should be email);
            //API returns a token; token is used to authenticate. also decoded token = id + exp + iat
            //example token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODE3ODU0YjE0NTRjNDFhODJlNmM3NzgiLCJleHAiOjE0ODMxOTc2NjYsImlhdCI6MTQ3ODAxMzY2Nn0.iDs223_K8SrtQlDHos5k1r8uRh8Pzq4-axjvZRPID4o
            //decoded example token: {"_id":"5817854b1454c41a82e6c778","exp":1483197666,"iat":1478013666}
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> stories = response.body();
                    dc.getUser().setBoughtStories(stories);
                } else {
                    txtError.setText(response.message());
                    Log.e("login", response.code() + " " + response.message());
                }
                btnLogin.setClickable(true);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                txtError.setText(R.string.connection_error);
                t.printStackTrace();
                btnLogin.setClickable(true);
            }
        });

        Intent intent = new Intent(MainActivity.this, StoryOverviewActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.txtNotRegistered)
    public void register() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    //for testing while server is offline
    private void serverOffline() {
        dc.setUser(new User("5817854b1454c41a82e6c778", "jeroen@gmail.com", "jeroen", "jeroen",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODE3ODU0YjE0NTRjNDFhODJlNmM3NzgiLCJleHAiOjE0ODMxOTc2NjYsImlhdCI6MTQ3ODAxMzY2Nn0.iDs223_K8SrtQlDHos5k1r8uRh8Pzq4-axjvZRPID4o",
                new ArrayList<Story>(), new ArrayList<String>(Arrays.asList("58285b7070f1457fcf7f3bc4"))));

        User user = dc.getUser();
        Intent intent = new Intent(MainActivity.this, StoryOverviewActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}
