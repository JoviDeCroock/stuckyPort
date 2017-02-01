package projecten3.stuckytoys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import projecten3.stuckytoys.custom.RealmString;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import projecten3.stuckytoys.persistence.PersistenceController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.checkboxStayLogged)
    CheckBox checkboxStayLogged;

    private DomainController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Realm.init(this);
        dc = DomainController.getInstance();

        //remember last email used to log in & set it in email edittext; autologin if staylogged was checked in previous session
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_emailpassword), MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String password = sharedPref.getString("password", "");
        boolean stayLogged = sharedPref.getBoolean("stayLogged", false);

        editEmail.setText(email);
        if (stayLogged)
        {
            checkboxStayLogged.setChecked(true);
            editPassword.setText(password);
            login(btnLogin);
        }
    }

    @OnClick(R.id.btnLogin)
    public void login(View view)
    {
        txtError.setText("");
        btnClickable(false);

        final String email = editEmail.getText().toString();
        final String password = editPassword.getText().toString();

        //alles ingevuld?
        if (email.isEmpty() || password.isEmpty())
        {
            txtError.setText(R.string.fill_all_fields);
            btnClickable(true);
            return;
        }

        try
        {
            Call<User> call = dc.login(email, password);

            call.enqueue(new Callback<User>()
            {

                //Call to API using Retrofit; User as model; Body requires username & password (but username is actually email!);
                //API returns a token; token is used to authenticate. also decoded token = id + exp + iat
                //example token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODE3ODU0YjE0NTRjNDFhODJlNmM3NzgiLCJleHAiOjE0ODMxOTc2NjYsImlhdCI6MTQ3ODAxMzY2Nn0.iDs223_K8SrtQlDHos5k1r8uRh8Pzq4-axjvZRPID4o
                //decoded example token: {"_id":"5817854b1454c41a82e6c778","exp":1483197666,"iat":1478013666}
                @Override
                public void onResponse(Call<User> call, Response<User> response)
                {
                    if (response.isSuccessful())
                    {
                        String token = response.body().getToken();
                        String tokenMiddle = token.split("\\.")[1];
                        String decoded = new String(Base64.decode(tokenMiddle, Base64.DEFAULT));
                        try
                        {
                            JSONObject jObj = new JSONObject(decoded);
                            //"exp" & "iat" also in this jsonobject
                            String id = jObj.getString("_id");
                            String username = jObj.getString("username");
                            dc.updateUser(id, token, username);

                            Log.d("login", "id: " + id + " token: " + token);

                            getUserStories();
                        } catch (JSONException ex)
                        {
                            ex.printStackTrace();
                        }
                    } else
                    {
                        if (response.code() == 401)
                        {
                            txtError.setText(R.string.unauthorized);
                        } else
                        {
                            txtError.setText(R.string.default_error);
                        }
                        Log.e("login", response.code() + " " + response.message());
                    }
                    btnClickable(true);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t)
                {
                    noConnection();
                }
            });

        } catch (IOException ex)
        {
            ex.printStackTrace();
            btnClickable(true);
        }
    }

    private void getUserStories()
    {
        Call<List<String>> call = dc.getUserStories();

        call.enqueue(new Callback<List<String>>()
        {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response)
            {
                if (response.isSuccessful())
                {
                    List<String> stories = response.body();
                    RealmList<RealmString> realmStories = new RealmList<RealmString>();
                    for (String story : stories)
                    {
                        realmStories.add(new RealmString(story));
                    }
                    dc.getUser().setBoughtStories(realmStories);

                    //save email so it gets autofilled next time app is opened; if staylogged is checked: save both email & password
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_emailpassword), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", editEmail.getText().toString());
                    if (checkboxStayLogged.isChecked())
                    {
                        editor.putString("password", editPassword.getText().toString());
                        editor.putBoolean("stayLogged", true);
                        editor.commit();
                    } else
                    {
                        editor.putBoolean("stayLogged", false);
                        editor.commit();
                    }

                    PersistenceController.internetConnection = true;

                    Intent intent = new Intent(MainActivity.this, StoryOverviewActivity.class);
                    startActivity(intent);
                    finish();
                } else
                {
                    if (response.code() == 401)
                    {
                        txtError.setText(R.string.unauthorized);
                    } else
                    {
                        txtError.setText(R.string.default_error);
                    }
                    Log.e("login", response.code() + " " + response.message());
                }
                btnClickable(true);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t)
            {
                noConnection();
            }
        });
    }

    private void noConnection()
    {
        txtError.setText(R.string.connection_error);
        final Context context = this;

        final Realm realm = Realm.getInstance(PersistenceController.CONFIG);
        final User user = realm.where(User.class).equalTo("email", editEmail.getText().toString()).equalTo("password", editPassword.getText().toString()).findFirstAsync();
        user.addChangeListener(new RealmChangeListener<RealmModel>()
        {
            @Override
            public void onChange(RealmModel element)
            {
                if (user.isValid())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(getString(R.string.continue_offline, user.getUsername()))
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dc.setUser(realm.copyFromRealm(user));
                                    PersistenceController.internetConnection = false;

                                    //save email so it gets autofilled next time app is opened; if staylogged is checked: save both email & password
                                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_emailpassword), MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("email", editEmail.getText().toString());
                                    if (checkboxStayLogged.isChecked())
                                    {
                                        editor.putString("password", editPassword.getText().toString());
                                        editor.putBoolean("stayLogged", true);
                                        editor.commit();
                                    } else
                                    {
                                        editor.putBoolean("stayLogged", false);
                                        editor.commit();
                                    }

                                    Intent intent = new Intent(MainActivity.this, StoryOverviewActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                    btnClickable(true);
                                }
                            });
                    builder.show();
                } else
                {
                    txtError.setText(R.string.connection_error_user_unknown);
                    btnClickable(true);
                }
            }
        });
    }

    private void btnClickable(boolean clickable)
    {
        //background green if clickable; gray if not clickable
        //using setBackgroundDrawable because setBackground doesn't work on API 15
        btnLogin.setClickable(clickable);
        if (clickable)
        {
            btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.clickable));
        } else
        {
            btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.unclickable));
        }
    }

    @OnClick(R.id.txtNotRegistered)
    public void register()
    {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}
