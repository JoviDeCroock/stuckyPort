package projecten3.stuckytoys.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.StoryOverviewActivity;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.fragments.StoryListFragment;

public class StoryAdapter extends BaseAdapter {
    private List<Story> stories = new ArrayList<Story>();
    private final LayoutInflater mInflater;
    private Context context;

    public StoryAdapter(Context context,  List<Story> stories) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.stories = stories;
    }


    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Story getItem(int i) {
        return stories.get(i);
    }

    //Implementation for extended class BaseAdapter; method isn't needed for now & i can't figure out where to get any long id from
    @Override
    public long getItemId(int i) {
        Log.e("StoryAdapter", "getItemId was somehow called; where?");
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        //TODO(maybe):can butterknife be used for this?? both for the binding and the onclicklistener??
        View v = view;
        final ImageView picture;
        final ProgressBar progressBar;
        TextView name;

        final Story story = getItem(i);

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.itemImage, v.findViewById(R.id.itemImage));
            v.setTag(R.id.txtItemName, v.findViewById(R.id.txtItemName));
            v.setTag(R.id.progress, v.findViewById(R.id.progress));
            name = (TextView) v.getTag(R.id.txtItemName);
            name.setText(story.getName());
            picture = (ImageView) v.getTag(R.id.itemImage);
            progressBar = (ProgressBar) v.getTag(R.id.progress);

            //image saved as base64 in api, example: ""data:image/png;base64,iVBORw0KGgoA...."
            //we don't need the data:image..base64, part so we split the string after the comma
            byte[] imageByteArray = story.getPicture();

            Transformation<Bitmap> transformation;
            if(story.isPurchased()) {
                transformation = new Transformation<Bitmap>() {
                    @Override
                    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
                        return resource;
                    }

                    @Override
                    public String getId() {
                        return "NoTransformation()";
                    }
                };
            } else {
                transformation = new GrayscaleTransformation(context);
            }

            //.load: the byte array to be loaded
            //.asBitmap(): necessary to be called because we're working with a bitmap
            //.listener(): hides the progressbar loading icon thing as soon as image is loaded (or failed to load)
            //.error(): image to be displayed when image failed to load (TODO: find a better error image)
            Glide.with(context)
                    .load(imageByteArray)
                    .asBitmap()
                    .transform(transformation)
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
                    .into(picture);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryOverviewActivity mContext = (StoryOverviewActivity) context;
                StoryListFragment fragment = (StoryListFragment) mContext.getSupportFragmentManager().findFragmentById(R.id.stories);
                fragment.itemClicked(i);
            }
        });

        return v;
    }

}
