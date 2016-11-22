package projecten3.stuckytoys;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.adapters.StoryAdapter;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.fragments.StoryDetailsFragment;
import projecten3.stuckytoys.fragments.StoryListFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryOverviewActivity extends AppCompatActivity {

    @BindView(R.id.stories) LinearLayout storiesLayout;

    private DomainController dc;
    private StoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_overview);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        StoryListFragment storyList = new StoryListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.stories, storyList);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        StoryDetailsFragment details = (StoryDetailsFragment) getSupportFragmentManager().findFragmentByTag("DETAILS");
        if (details != null && details.isVisible()) {
            //if in singlepane view & in storydetails view -> go back to storylist view
            StoryListFragment storyList = new StoryListFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.stay700ticks, R.anim.exit_to_bot);
            ft.replace(R.id.stories, storyList);
            ft.commit();

        } else {
            //if in dualpane view OR in singlepane & in storydetails view -> confirm that you want to go back
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.logout)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dc.setUser(null);
                            Intent i = new Intent(StoryOverviewActivity.this, MainActivity.class);
                            startActivity(i);
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

    public void startOrBuy() {
        Intent i = new Intent(StoryOverviewActivity.this, SceneActivity.class);
        i.putExtra("SCENE_NUMBER", 1);
        startActivity(i);
    }

    public void purchaseStory(String _id) {
        dc.getUser().getBoughtStories().add(_id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
