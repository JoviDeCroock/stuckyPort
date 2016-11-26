package projecten3.stuckytoys;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editPassword)
    EditText editPassword;

    @BindView(R.id.editPasswordRepeat)
    EditText editPasswordRepeat;

    @BindView(R.id.editUsername)
    EditText editUsername;

    @BindView(R.id.txtError)
    TextView txtError;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    private DomainController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dc = DomainController.getInstance();

        ButterKnife.bind(this);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //inflate the menu, this adds items to the action bar if it is present
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/


    @OnClick(R.id.btnRegister)
    public void register()
    {
        //resetten error
        txtError.setText("");

        btnRegister.setClickable(false);

        final String email = editEmail.getText().toString();
        final String password = editPassword.getText().toString();
        final String passwordRepeat = editPasswordRepeat.getText().toString();
        final String username = editUsername.getText().toString();

        //alles ingevuld?
        if( email.isEmpty() ||
                password.isEmpty() ||
                passwordRepeat.isEmpty() ||
                username.isEmpty()) {
            txtError.setText(R.string.fill_all_fields);
        }
        else if(!isEmailValid(email)) {
            txtError.setText(R.string.invalid_email);
        }
        else if (!password.equals(passwordRepeat)) {
            txtError.setText(R.string.different_passwords);
        } else {

            Call<User> call = dc.register(username, email, password);

            call.enqueue(new Callback<User>() {

                //Call to API using Retrofit; User as model; Body requires username, email & password;
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
                            dc.updateUser(id, token);

                            Log.d("register", "id: " + id + " token: " + token);

                            Intent intent = new Intent(RegisterActivity.this, StoryOverviewActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        txtError.setText(response.message());
                        Log.e("register", response.code() + " " + response.message());
                    }
                    btnRegister.setClickable(true);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    txtError.setText("a");
                    t.printStackTrace();
                    btnRegister.setClickable(true);
                }
            });
        }
        btnRegister.setClickable(true);
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid, false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.register_cancel)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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
