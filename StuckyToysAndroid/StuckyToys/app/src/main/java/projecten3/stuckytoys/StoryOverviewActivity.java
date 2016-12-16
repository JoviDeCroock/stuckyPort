package projecten3.stuckytoys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import projecten3.stuckytoys.adapters.StoryAdapter;
import projecten3.stuckytoys.custom.RealmString;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import projecten3.stuckytoys.fragments.StoryDetailsFragment;
import projecten3.stuckytoys.fragments.StoryListFragment;
import projecten3.stuckytoys.persistence.PersistenceController;
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

                            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_emailpassword), MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("stayLogged", false);
                            editor.commit();

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

    public void start(String storyId) {
        Intent i = new Intent(StoryOverviewActivity.this, ScreenSlidePagerActivity.class);
        i.putExtra("STORY_ID", storyId);
        startActivity(i);
    }

    public void purchaseStory(final Story story, final StoryDetailsFragment fragment) {
        Call<List<String>> call = dc.buyStory(story.get_id());
        call.enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {

                    Realm realm = Realm.getInstance(PersistenceController.CONFIG);

                    realm.beginTransaction();
                    story.setPurchased(true);
                    dc.getUser().getBoughtStories().add(new RealmString(story.get_id()));
                    final User user = realm.copyToRealmOrUpdate(dc.getUser()); // Persist unmanaged objects
                    realm.commitTransaction();

                    fragment.onResponsePurchaseStory(true);
                } else {
                    fragment.onResponsePurchaseStory(false);
                    Log.e("Buy Story", response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                fragment.onResponsePurchaseStory(false);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
