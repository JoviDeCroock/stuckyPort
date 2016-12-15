package projecten3.stuckytoys.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.StoryOverviewActivity;
import projecten3.stuckytoys.custom.StoryImageView;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.Theme;

public class StoryDetailsFragment extends Fragment {

    @BindView(R.id.storyName) TextView storyName;
    @BindView(R.id.storyImage)
    StoryImageView storyImage;
    @BindView(R.id.storyDate) TextView storyDate;
    @BindView(R.id.storyScenes) TextView storyScenes;
    @BindView(R.id.storyDuration) TextView storyDuration;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.themesContainer) LinearLayout themesContainer;
    @BindView(R.id.startOrBuyButton) Button startOrBuyButton;


    private Context context;
    private Story story;

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
        progressBar.setVisibility(View.VISIBLE);

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
        this.story = story;
        storyName.setText(story.getName());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        storyDate.setText(" " + df.format(story.getDate()));

        storyScenes.setText(" " + story.getScenes().size());
        storyDuration.setText(" " + 60 * story.getDuration() + " " + getString(R.string.minutes));

        //per theme: add two textviews to container; set name bold & indent description
        for (Theme theme : story.getThemes()) {
            TextView txtName = new TextView(context), txtDesc = new TextView(context);
            txtName.setText(theme.getName());
            txtDesc.setText(theme.getDescription());

            txtName.setTypeface(null, Typeface.BOLD);

            LayoutParams llp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            llp.setMargins(15, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
            txtDesc.setLayoutParams(llp);

            themesContainer.addView(txtName);
            themesContainer.addView(txtDesc);
        }

        if(!story.isPurchased()) {
            startOrBuyButton.setText(String.format("%s: €%.2f", getString(R.string.buy_story), story.getPrice()));
        }

        byte[] imageByteArray = story.getPicture();
        Glide.with(context)
                .load(imageByteArray)
                .asBitmap()
                .listener(new RequestListener<byte[], Bitmap>() {
                    @Override
                    public boolean onException(Exception e, byte[] model, Target<Bitmap> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, byte[] model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.error)
                .into(storyImage);
    }

    @OnClick(R.id.startOrBuyButton)
    public void startOrBuy() {
        if(story.isPurchased()) {
            StoryOverviewActivity mContext = (StoryOverviewActivity) context;
            mContext.start(story.get_id());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirm_buy_story)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            StoryOverviewActivity mContext = (StoryOverviewActivity) context;
                            mContext.purchaseStory(story.get_id());
                            story.setPurchased(true);
                            startOrBuyButton.setText(R.string.start_story);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            builder.show();
        }
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 100.f);
    }

}
