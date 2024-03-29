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

import io.realm.RealmList;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Scene;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.fragments.SceneFragment;
import projecten3.stuckytoys.persistence.PersistenceController;
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

        Story story = null;
        RealmList<Story> stories = dc.getUser().getStories();
        for (Story s : stories) {
            if (s.get_id().equals(story_id)) {
                story = s;
            }
        }
        if (story != null) {
            numPages = story.getScenes().size();

            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), story);
            mPager.setAdapter(mPagerAdapter);
        }

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
        private String storyId;

        public ScreenSlidePagerAdapter(FragmentManager fm, Story story) {
            super(fm);
            this.scenes = story.getScenes();
            this.storyId = story.get_id();
        }

        public void setScenes(List<Scene> scenes)
        {
            this.scenes = scenes;
        }

        @Override
        public Fragment getItem(int position) {
            SceneFragment sf = new SceneFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("scene", scenes.get(position).get_id());
            bundle.putSerializable("story", storyId);
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
