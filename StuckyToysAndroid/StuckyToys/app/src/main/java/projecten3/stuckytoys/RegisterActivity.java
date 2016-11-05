package projecten3.stuckytoys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends Activity {

    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editPassword)
    EditText editPassword;

    @BindView(R.id.editPasswordRepeat)
    EditText editPasswordRepeat;

    @BindView(R.id.txtError)
    TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        //alles ingevuld?
        if( editEmail.getText().toString().isEmpty() ||
                editPassword.getText().toString().isEmpty() ||
                editPasswordRepeat.getText().toString().isEmpty() )
        {
            txtError.setText("Gelieve alle gegevens in te vullen.");
            return;
        }

        if(!isEmailValid(editEmail.getText().toString()))
        {
            txtError.setText("e-mailadres is niet geldig.");
            return;
        }

        if(!editPassword.getText().toString().equals(editPasswordRepeat.getText().toString()))
        {
            txtError.setText("Wachtwoorden komen niet overeen.");
            return;
        }

        //registreer actie komt hier.
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
}
