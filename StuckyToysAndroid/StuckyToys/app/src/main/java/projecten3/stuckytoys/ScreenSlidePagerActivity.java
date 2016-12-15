package projecten3.stuckytoys;

/**
 * Created by Jeroen on 11/22/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;

import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Scene;
import projecten3.stuckytoys.fragments.SceneFragment;
import projecten3.stuckytoys.retrofithelpers.StoryHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenSlidePagerActivity extends FragmentActivity {
    private int numPages = 0;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private StoryHelper storyHelper;
    private DomainController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_screen_slide);

        dc = DomainController.getInstance();

        Intent intent = getIntent();
        String story_id = intent.getStringExtra("STORY_ID");

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);

        getStoryFromDb(story_id);

    }

    private void getStoryFromDb(String story_id) {
        Call<StoryHelper> call = dc.getStory(story_id);
        call.enqueue(new Callback<StoryHelper>() {

            //TODO: document
            @Override
            public void onResponse(Call<StoryHelper> call, Response<StoryHelper> response) {
                if (response.isSuccessful()) {
                    storyHelper = response.body();

                    numPages = storyHelper.getScenes().size();

                    mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), storyHelper.getScenes());
                    mPager.setAdapter(mPagerAdapter);

                    //mAdapter = new StoryAdapter(context, stories);
                    //gridView.setAdapter(mAdapter);
                } else {
                    //txtError.setText(response.message());
                    Log.e("stories", response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<StoryHelper> call, Throwable t) {
                //txtError.setText(R.string.connection_error);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Scene> scenes;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Scene> scenes) {
            super(fm);
            this.scenes = scenes;
        }

        public void setScenes(List<Scene> scenes)
        {
            this.scenes = scenes;
        }

        @Override
        public Fragment getItem(int position) {
            SceneFragment sf = new SceneFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("scene", scenes.get(position));
            sf.setArguments(bundle);
            //sf.setScene(scenes.get(position));
            return sf;
        }



        @Override
        public int getCount() {
            return numPages;
        }
    }
}
