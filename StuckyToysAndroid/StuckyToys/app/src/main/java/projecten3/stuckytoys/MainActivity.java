package projecten3.stuckytoys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.domein.DomeinController;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editUsername) EditText editUsername;
    @BindView(R.id.editPassword) EditText editPassword;
    @BindView(R.id.txtError) TextView txtError;

    private DomeinController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dc = DomeinController.getInstance();
    }

    @OnClick (R.id.btnLogin)
    private void login() {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();

        try{



        } catch (Exception ex) {

        }

        //test
        txtError.setText("error");
    }
}
