package projecten3.stuckytoys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.txtMyStories)
    TextView txtMyStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnMyStories)
    public void goToMyStories()
    {
        //do something
        txtMyStories.setText("veranderd");
    }

    @OnClick(R.id.btnMyPictures)
    public void goToMyPictures()
    {
        //do something
    }

    @OnClick(R.id.btnAgenda)
    public void goToMyAgenda()
    {
        //do something
    }

    @OnClick(R.id.btnMessages)
    public void goToMessages()
    {
        //do something
    }
}
