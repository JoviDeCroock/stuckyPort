package projecten3.stuckytoys.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.adapters.StoryAdapter;
import projecten3.stuckytoys.custom.DownloadImageTask;
import projecten3.stuckytoys.custom.DownloadSoundTask;
import projecten3.stuckytoys.custom.DownloadWidgetFileImageTask;
import projecten3.stuckytoys.custom.RealmString;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Scene;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import projecten3.stuckytoys.domain.Widget;
import projecten3.stuckytoys.domain.WidgetFile;
import projecten3.stuckytoys.persistence.PersistenceController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListFragment extends Fragment {

    @BindView(R.id.gridStories) GridView gridView;
    @BindView(R.id.txtError) TextView txtError;
    @BindView(R.id.txtSelectStory) TextView txtSelectStory;
    @BindView(R.id.sortSpinner) Spinner sortSpinner;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    boolean dualPane;
    int selectedStoryPosition = 0;
    int sortMode = 0;
    private Bundle savedState = null;
    private DomainController dc;
    private StoryAdapter mAdapter;
    private Context context;

    public StoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dc = DomainController.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_list,
                container, false);
        ButterKnife.bind(this, view);
        txtSelectStory.setText(String.format(getString(R.string.select_story), dc.getUser().getUsername()));

        View detailsFragment = getActivity().findViewById(R.id.storyDetail);
        dualPane = detailsFragment != null && detailsFragment.getVisibility() == View.VISIBLE;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);

        //if layout mode: 3 colums instead of 2
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(3);
        }
        //if restarting fragment: restore selected story (doesn't seem to work...)
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("savedState");
        }
        if (savedState != null) {
            selectedStoryPosition = savedState.getInt("selectedStoryPosition");
        }
        //if tablet: make selected story appear
        if (dualPane) {
            itemClicked(selectedStoryPosition);
        }

        if (PersistenceController.internetConnection) {
            fillStories();
        } else {
            fillStoriesOffline();
        }

        return view;
    }

    public void itemClicked(int index) {
        selectedStoryPosition = index;

        //tablet mode
        if(dualPane) {
            StoryDetailsFragment details = (StoryDetailsFragment) getFragmentManager().findFragmentById(R.id.storyDetail);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = StoryDetailsFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.storyDetail, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            } else { }

        //smartphone mode
        } else {
            StoryDetailsFragment details = StoryDetailsFragment.newInstance(index);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_bot, R.anim.stay700ticks);
            ft.replace(R.id.stories, details, "DETAILS");
            ft.commit();
        }
    }

    @OnItemSelected(R.id.sortSpinner)
    public void spinnerItemSelected(Spinner spinner, int position) {
        //0 = alfabetical; 1 = date; 2 = purchased
        List<Story> stories = dc.getUser().getStories();
        if (sortMode != position) {
            sortMode = position;
            switch (position) {
                case 0:
                    Collections.sort(stories, new Comparator<Story>() {
                        public int compare(Story s1, Story s2) {
                            if (s1.getName() == null || s2.getName() == null)
                                return 0;
                            return s1.getName().compareTo(s2.getName());
                        }
                    });
                    break;
                case 1:
                    Collections.sort(stories, new Comparator<Story>() {
                        public int compare(Story s1, Story s2) {
                            if (s1.getDate() == null || s2.getDate() == null)
                                return 0;
                            return s1.getDate().compareTo(s2.getDate());
                        }
                    });
                    break;
                case 2:
                    Collections.sort(stories, new Comparator<Story>() {
                        public int compare(Story s1, Story s2) {
                            return (int) (s1.getDuration() - s2.getDuration());
                        }
                    });
                    break;
                case 3:
                    Collections.sort(stories, new Comparator<Story>() {
                        public int compare(Story s1, Story s2) {
                            if (s1.isPurchased() && !s2.isPurchased())
                                return -1;
                            if (!s1.isPurchased() && s2.isPurchased())
                                return 1;
                            return 0;
                        }
                    });
                    break;
                default:
                    break;
            }
            mAdapter = new StoryAdapter(context, stories);
            gridView.setAdapter(mAdapter);
        }
    }

    private void fillStories() {
        Call<List<Story>> call = dc.getPublishedStories();
        final StoryListFragment storyListFragment = this;
        call.enqueue(new Callback<List<Story>>() {

            //TODO: document
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getInstance(PersistenceController.CONFIG);

                    final List<Story> stories = response.body();
                    final User dcUser = dc.getUser();
                    final RealmList<Story> realmStories = new RealmList<Story>();
                    for (Story story : stories) {
                        realmStories.add(story);
                    }

                    final String[] storyPaths = new String[stories.size()];
                    for (int i = 0; i < stories.size(); i++) {
                        Story story = stories.get(i);
                        storyPaths[i] = story.getPath();
                        for (RealmString realmString : dcUser.getBoughtStories()) {
                            if (realmString.getName().equals(story.get_id())) {
                                story.setPurchased(true);
                            }
                        }
                    }

                    final User realmUser = realm.where(User.class).equalTo("_id", dc.getUser().get_id()).findFirstAsync();
                    realmUser.addChangeListener(new RealmChangeListener<RealmModel>() {
                        @Override
                        public void onChange(RealmModel element) {
                            if (realmUser.isValid()) {
                                realm.copyFromRealm(realmUser);
                                if (realmUser.getStories().size() == stories.size()) {
                                    dc.setUser(realmUser);
                                    fillStoriesOffline();
                                } else {
                                    realm.beginTransaction();
                                    dcUser.setStories(new RealmList<Story>());
                                    for (Story story : realmStories) {
                                        dcUser.getStories().add(story);
                                    }
                                    realm.commitTransaction();
                                    new DownloadImageTask(storyListFragment).execute(storyPaths);
                                }
                            } else {
                                dcUser.setStories(realmStories);
                                new DownloadImageTask(storyListFragment).execute(storyPaths);
                            }
                            realmUser.removeChangeListener(this);

                        }
                    });

                } else {
                    txtError.setText(response.message());
                    setProgressBarInvisible();
                    Log.e("stories", response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                txtError.setText(R.string.connection_error);
                setProgressBarInvisible();
                t.printStackTrace();
            }
        });
    }

    private void fillStoriesOffline() {
        User user = dc.getUser();
        mAdapter = new StoryAdapter(context, user.getStories());
        gridView.setAdapter(mAdapter);
        setProgressBarInvisible();
    }

    public void updateStoryImages(List<byte[]> images) {
        List<Story> stories = dc.getUser().getStories();
        for (int i = 0; i < stories.size(); i++) {
            stories.get(i).setPicture(images.get(i));
        }

        mAdapter = new StoryAdapter(context, stories);
        gridView.setAdapter(mAdapter);

        //.....holy ***.... seems like a very complicated way to do this but no idea how to do it better.....
        //...damn database being so complicated...
        //STEP 1: puting all soundwidgetfiles from all scenes from all stories into a list and same for imagewidgetfiles
        List<WidgetFile> soundWidgetFiles = new ArrayList();
        List<WidgetFile> imageWidgetFiles = new ArrayList();
        for (Story story : stories) {
            for (Scene scene : story.getScenes()) {
                for (Widget widget : scene.getWidgets()) {
                    for (WidgetFile widgetFile : widget.getWidgetFiles()) {
                        switch (widgetFile.getType().toLowerCase()) {
                            case "afbeelding":
                                imageWidgetFiles.add(widgetFile);
                                break;
                            case "geluid":
                                soundWidgetFiles.add(widgetFile);
                                break;
                        }
                    }
                }
            }
        }

        //STEP 2: download all imagewidgetfiles from server -> put them into a byte array and put them into the widgetfile's bytes field
        WidgetFile[] imageWidgetFilesArray = imageWidgetFiles.toArray(new WidgetFile[imageWidgetFiles.size()]);
        new DownloadWidgetFileImageTask(this, soundWidgetFiles).execute(imageWidgetFilesArray);
    }

    public void updateWidgetImages(boolean success, List<WidgetFile> soundWidgetFiles) {
        if (success) {
            //STEP 3: download all soundwidgetfiles from server -> put them into a byte array and put them into the widgetfile's bytes field
            WidgetFile[] soundWidgetFilesArray = soundWidgetFiles.toArray(new WidgetFile[soundWidgetFiles.size()]);
            new DownloadSoundTask(this).execute(soundWidgetFilesArray);
        } else {

        }
    }

    public void updateWidgetSounds(boolean success) {
        if (success) {
            //STEP 4: save EVERYTHING to local db! :D
            Realm realm = Realm.getInstance(PersistenceController.CONFIG);

            realm.beginTransaction();
            final User user = realm.copyToRealmOrUpdate(dc.getUser()); // Persist unmanaged objects
            realm.commitTransaction();

            setProgressBarInvisible();
        } else {

        }
    }

    public void setProgressBarInvisible() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("savedState", (savedState != null) ? savedState : saveState());
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        state.putInt("selectedStoryPosition", selectedStoryPosition);
        return state;
    }
}
