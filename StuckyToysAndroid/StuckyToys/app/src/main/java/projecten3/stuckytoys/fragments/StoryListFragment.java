package projecten3.stuckytoys.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.adapters.StoryAdapter;
import projecten3.stuckytoys.custom.ServerOfflineHelper;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListFragment extends Fragment {

    @BindView(R.id.gridStories) GridView gridView;
    @BindView(R.id.txtError) TextView txtError;
    @BindView(R.id.txtSelectStory) TextView txtSelectStory;
    @BindView(R.id.sortSpinner) Spinner sortSpinner;

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

        if(ServerOfflineHelper.SERVEROFFLINE) {
            serverOffline();
        } else {
            fillStories();
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
        call.enqueue(new Callback<List<Story>>() {

            //TODO: document
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if (response.isSuccessful()) {
                    List<Story> stories = response.body();
                    User user = dc.getUser();
                    user.setStories(stories);

                    for (int i = 0; i < stories.size(); i++) {
                        if (user.getBoughtStories().contains(stories.get(i).get_id())) {
                            stories.get(i).setPurchased(true);
                        }
                    }

                    mAdapter = new StoryAdapter(context, stories);
                    gridView.setAdapter(mAdapter);
                } else {
                    txtError.setText(response.message());
                    Log.e("stories", response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                txtError.setText(R.string.connection_error);
                t.printStackTrace();
            }
        });
    }

    private void serverOffline() {
        List<Story> stories = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(ServerOfflineHelper.DATE);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        /*
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME,
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENEIDS,
                ServerOfflineHelper.THEMES,
                true));
        stories.add(new Story("notYetBought",
                ServerOfflineHelper.STORYNAME + " 2",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENEIDS,
                ServerOfflineHelper.THEMES,
                false));
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME + " 3",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENEIDS,
                ServerOfflineHelper.THEMES,
                false));
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME + " 4",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENEIDS,
                ServerOfflineHelper.THEMES,
                true));
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME + " 5",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENEIDS,
                ServerOfflineHelper.THEMES,
                true));
                */

        if(dc.getUser().getStories().isEmpty())
            dc.getUser().setStories(stories);

        User user = dc.getUser();
        for (int i = 0; i < stories.size(); i++) {
            if (user.getBoughtStories().contains(stories.get(i).get_id())) {
                stories.get(i).setPurchased(true);
            }
        }

        mAdapter = new StoryAdapter(context, stories);
        gridView.setAdapter(mAdapter);
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
