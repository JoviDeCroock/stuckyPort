package projecten3.stuckytoys.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Scene;
import projecten3.stuckytoys.domain.Widget;
import projecten3.stuckytoys.domain.WidgetFile;

public class SceneFragment extends Fragment {


    @BindView(R.id.txtText)
    TextView txtText;
    @BindView(R.id.btnWidget)
    Button btnWidget;
    @BindView(R.id.btnHint1)
    Button btnHint1;
    @BindView(R.id.btnHint2)
    Button btnHint2;
    @BindView(R.id.btnHint3)
    Button btnHint3;
    @BindView(R.id.widgetContainer)
    HorizontalScrollView widgetContainer;
    @BindView(R.id.hintContainer)
    LinearLayout hintContainer;

    private Scene scene;
    private MediaPlayer mediaPlayer = null;
    private DomainController dc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        mediaPlayer = new MediaPlayer();
        dc = DomainController.getInstance();

        ButterKnife.bind(this, view);

        //txtText = (TextView)view.findViewById(R.id.txttext);

        scene = (Scene) getArguments().getSerializable("scene");
        fillScene();


        return view;
    }

    public void setScene(Scene scene)
    {
        this.scene = scene;
        fillScene();
    }

    public Scene getScene()
    {
        return scene;
    }

    private void fillScene()
    {
        //TEXT
        txtText.setText(scene.getText());

        //WIDGET BUTTONS
        for(Widget currentWidget: scene.getWidgets())
        {
            ImageButton button = new ImageButton(getActivity());
            switch (currentWidget.getWidgetFiles().get(0).getType().toLowerCase())
            {
                case "music":
                    putSoundInButton(currentWidget.getWidgetFiles().get(0).get_id(), button);
                    putImageInButton(currentWidget.getWidgetFiles().get(1).get_id(), button);
                    break;
                case "game":
                    break;
                case "ar":
                    break;
                case "recording":
                    break;
                default:


            }

            widgetContainer.addView(button);
        }

        //HINTS
        for(final String currentHint : scene.getHints())
        {
            /*
            <ImageButton
            android:id="@+id/btnHint1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/hint_button"/>*/

            ImageButton button = new ImageButton(getActivity());

            ViewGroup.LayoutParams params= button.getLayoutParams();
            params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            button.setLayoutParams(params);

            button.setBackgroundResource(R.drawable.hint_button);
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Hint  " + currentHint, Toast.LENGTH_LONG).show();
                }
            });

            hintContainer.addView(button);
        }

    }

    private void putSoundInButton(String soundId, final ImageButton btnWidget)
    {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + dc.getUser().getToken());

        Uri uri = Uri.parse("http://188.166.173.147:3000/story/download/" + soundId);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getActivity(), uri, header);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    btnWidget.setClickable(true);
                }
            });
        } catch (IllegalArgumentException | IOException ex) {
            Log.e("media error", ex.getMessage());
        }
    }

    private void putImageInButton(String imageId, ImageButton btnWidget)
    {
        GlideUrl glideUrl = new GlideUrl("http://188.166.173.147:3000/story/download/" + imageId, new LazyHeaders.Builder()
        .addHeader("Authorization", "Bearer " + dc.getUser().getToken())
        .build());

        Glide.with(getActivity())
                .load(glideUrl)
                .error(R.drawable.error)
                .into(btnWidget);
    }
}
