package projecten3.stuckytoys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.domain.DomainController;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.editEmail) EditText editEmail;
    @Bind(R.id.editPassword) EditText editPassword;
    @Bind(R.id.txtError) TextView txtError;

    private DomainController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dc = DomainController.getInstance();
    }

    @OnClick(R.id.btnLogin)
    public void login(View view) {

        String username = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        //test
        txtError.setText("error test");
    }

    @OnClick(R.id.txtNotRegistered)
    public void register() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
