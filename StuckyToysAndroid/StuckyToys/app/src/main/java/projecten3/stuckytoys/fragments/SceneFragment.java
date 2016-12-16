package projecten3.stuckytoys.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.custom.RealmString;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Scene;
import projecten3.stuckytoys.domain.Widget;
import projecten3.stuckytoys.persistence.PersistenceController;

public class SceneFragment extends Fragment {


    @BindView(R.id.txtText)
    TextView txtText;
    @BindView(R.id.widgetContainer)
    HorizontalScrollView widgetContainer;
    @BindView(R.id.hintContainer)
    LinearLayout hintContainer;
    @BindView(R.id.snackbarLayout)
    CoordinatorLayout snackbarLayout;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    private Scene scene;
    private DomainController dc;
    private Snackbar mSnackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scene = (Scene) getArguments().getSerializable("scene");

        int layoutId = getResources().getIdentifier("fragment_scene", "layout", getActivity().getPackageName());
        /* TODO: Once we have multiple layouts...: search layout based on scene's layout number (for example: fragment_scene2)
        int layoutId = getResources().getIdentifier("fragment_scene" + scene.getLayout(), "layout", getActivity().getPackageName());
        */
        View view = inflater.inflate(layoutId, container, false);
        dc = DomainController.getInstance();

        ButterKnife.bind(this, view);

        fillScene();

        return view;
    }

    public Scene getScene() {
        return scene;
    }

    private void fillScene() {

        //WIDGET BUTTONS0
        for (Widget currentWidget : scene.getWidgets()) {

            ImageButton btnWidget = new ImageButton(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            btnWidget.setLayoutParams(params);
            btnWidget.setBackgroundDrawable(null);

            if (currentWidget.getWidgetFiles().get(0) != null) {
                switch (currentWidget.getWidgetFiles().get(0).getType().toLowerCase()) {
                    case "geluid":
                        putSoundInButton(currentWidget.getWidgetFiles().get(0).getBytes(), btnWidget);
                        putImageInButton(currentWidget.getWidgetFiles().get(1).getBytes(), btnWidget);
                        break;
                    case "spel":
                        putGameInButton(currentWidget.getWidgetFiles().get(0).getFileName(), btnWidget);
                        putDefaultImageInButton(R.drawable.game_start_button, btnWidget);
                        break;
                    case "afbeelding":
                        putImageInButton(currentWidget.getWidgetFiles().get(1).getBytes(), btnWidget);
                        break;
                    case "ar":
                        break;
                    default:
                        txtError.setText(R.string.scene_error);
                        return;
                }

                //add the widget to the layout
                widgetContainer.addView(btnWidget);

            }
        }

        //TEXT
        txtText.setText(scene.getText());

        //HINTS
        for (final RealmString currentHint : scene.getHints()) {
            final ImageButton button = new ImageButton(getActivity());

            Resources r = getActivity().getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10,
                    r.getDisplayMetrics()
            );

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, px, 0);
            button.setLayoutParams(params);
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setImageResource(R.drawable.hint_button);

            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mSnackbar = Snackbar.make(snackbarLayout, currentHint.getName(), Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSnackbar.dismiss();
                        }
                    });
                    mSnackbar.setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //button.setImageResource(R.drawable.hint_button);
                        }
                    });

                    mSnackbar.getView().setBackgroundResource(R.color.stuckytoys_green);
                    mSnackbar.show();
                    //button.setImageResource(R.drawable.hint_button_balloon);
                }
            });

            hintContainer.addView(button);
        }

    }

    private void putSoundInButton(byte[] soundBytes, final ImageButton btnWidget) {
        /*
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

            btnWidget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();
                }
            });
        } catch (IllegalArgumentException | IOException ex) {
            Log.e("media error", ex.getMessage());
        }
        */
        final MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("widgetSound", "mp3");
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(soundBytes);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    btnWidget.setClickable(true);
                }
            });

            btnWidget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }

    }

    private void putGameInButton(String game, final ImageButton btnWidget) {
        final Intent launchIntent;
        switch (game) {
            case "RecycleActivity": launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.example.Stoeky");
                break;
            default: launchIntent = null;
        }
        btnWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
    }

    private void putImageInButton(byte[] imageBytes, ImageButton btnWidget) {
        /*
        GlideUrl glideUrl = new GlideUrl(PersistenceController.BASEURL + "story/download/" + imageId, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + dc.getUser().getToken())
                .build());
        */

        Glide.with(getActivity())
                .load(imageBytes)
                .error(R.drawable.error)
                .into(btnWidget);
    }

    private void putDefaultImageInButton(int resource, ImageButton btnWidget) {
        Glide.with(getActivity())
                .load(resource)
                .error(R.drawable.error)
                .into(btnWidget);
    }
}
