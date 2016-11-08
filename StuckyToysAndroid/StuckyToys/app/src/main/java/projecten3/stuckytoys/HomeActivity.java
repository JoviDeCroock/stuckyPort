package projecten3.stuckytoys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Member;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.txtMyStories)
    TextView txtMyStories;

    private DomainController dc;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        member = dc.getMember();

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
