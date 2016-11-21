package projecten3.stuckytoys.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.custom.MemberImageView;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.Theme;

public class StoryDetailsFragment extends Fragment {

    @BindView(R.id.storyName) TextView storyName;
    @BindView(R.id.storyImage) MemberImageView storyImage;
    @BindView(R.id.storyDate) TextView storyDate;
    @BindView(R.id.storyScenes) TextView storyScenes;
    @BindView(R.id.storyThemes) TextView storyThemes;

    private Context context;

    public static StoryDetailsFragment newInstance(int index) {
        StoryDetailsFragment f = new StoryDetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public StoryDetailsFragment() {
        // Required empty public constructor
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_details, container, false);

        ButterKnife.bind(this, view);

        Story story = DomainController.getInstance().getUser().getStories().get(getShownIndex());
        setDetails(story);

        return view;
    }

    public void update(int index) {
        Bundle args = new Bundle();
        args.putInt("index", index);
        this.setArguments(args);

        Story story = DomainController.getInstance().getUser().getStories().get(index);
        setDetails(story);
    }

    public void setDetails(Story story) {
        storyName.setText(story.getName());
        storyDate.setText(story.getDate().toString());
        storyScenes.setText("" + story.getScenes().length);
        String txtThemes = "";
        for (Theme theme : story.getThemes()) {
            txtThemes += theme.getName() + "\n\t\t" + theme.getDescription() + "\n";
        }
        txtThemes = txtThemes.substring(0, txtThemes.length()-3);
        storyThemes.setText(txtThemes);

        byte[] imageByteArray = Base64.decode(story.getPicture().split(",")[1], Base64.DEFAULT);
        Glide.with(context)
                .load(imageByteArray)
                .asBitmap()
                .listener(new RequestListener<byte[], Bitmap>() {
                    @Override
                    public boolean onException(Exception e, byte[] model, Target<Bitmap> target, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, byte[] model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.error)
                .into(storyImage);
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

}
