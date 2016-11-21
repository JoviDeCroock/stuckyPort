package projecten3.stuckytoys.fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.adapters.StoryAdapter;
import projecten3.stuckytoys.custom.ServerOfflineHelper;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Story;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListFragment extends Fragment {

    @BindView(R.id.gridStories) GridView gridView;
    @BindView(R.id.txtError) TextView txtError;
    @BindView(R.id.txtSelectStory) TextView txtSelectStory;

    boolean dualPane;
    int selectedStoryPosition = 0;
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
            }

        } else {
            StoryDetailsFragment details = StoryDetailsFragment.newInstance(index);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_bot, FragmentTransaction.TRANSIT_NONE);
            ft.replace(R.id.stories, details, "DETAILS");
            ft.commit();
        }
    }

    private void fillStories() {
        Call<List<Story>> call = dc.getAllStories();
        call.enqueue(new Callback<List<Story>>() {

            //TODO: document
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if (response.isSuccessful()) {
                    List<Story> stories = response.body();
                    dc.getUser().setStories(stories);

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

        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME,
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENES,
                ServerOfflineHelper.THEMES,
                true));
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME + " 2",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENES,
                ServerOfflineHelper.THEMES,
                false));
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME + " 3",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENES,
                ServerOfflineHelper.THEMES,
                false));
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME + " 4",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENES,
                ServerOfflineHelper.THEMES,
                true));
        stories.add(new Story(ServerOfflineHelper.USERID,
                ServerOfflineHelper.STORYNAME + " 5",
                date,
                ServerOfflineHelper.PICTURE,
                ServerOfflineHelper.SCENES,
                ServerOfflineHelper.THEMES,
                true));

        dc.getUser().setStories(stories);
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
