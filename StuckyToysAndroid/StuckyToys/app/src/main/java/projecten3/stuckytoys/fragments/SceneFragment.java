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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.R;
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
    private MediaPlayer mediaPlayer = null;
    private DomainController dc;
    private Snackbar mSnackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        mediaPlayer = new MediaPlayer();
        dc = DomainController.getInstance();

        ButterKnife.bind(this, view);

        scene = (Scene) getArguments().getSerializable("scene");
        fillScene();

        return view;
    }

    public Scene getScene() {
        return scene;
    }

    private void fillScene() {

        //TODO: set background
        //linearlayout.setBackground()...

        //WIDGET BUTTONS0
        for (Widget currentWidget : scene.getWidgets()) {

            ImageButton btnWidget = new ImageButton(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            btnWidget.setLayoutParams(params);
            btnWidget.setBackground(null);


            if (currentWidget.getWidgetFiles().get(0) != null) {
                switch (currentWidget.getWidgetFiles().get(0).getType().toLowerCase()) {
                    case "geluid":
                        putSoundInButton(currentWidget.getWidgetFiles().get(0).get_id(), btnWidget);
                        putImageInButton(currentWidget.getWidgetFiles().get(1).get_id(), btnWidget);
                        break;
                    case "spel":
                        putGameInButton(currentWidget.getWidgetFiles().get(0).getFileName(), btnWidget);
                        putDefaultImageInButton(R.drawable.game_start_button, btnWidget);
                        break;
                    case "ar":
                        break;
                    case "opname":
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
        for (final String currentHint : scene.getHints()) {
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

            //final Crouton myCrouton = makeCrouton(currentHint);
            //final Snackbar mySnackbar = makeSnackbar(currentHint);

            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    //Toast.makeText(getActivity(), currentHint, Toast.LENGTH_LONG).show();
                    //myCrouton.show();
                    mSnackbar = Snackbar.make(snackbarLayout, currentHint, Snackbar.LENGTH_INDEFINITE);
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

    private void putSoundInButton(String soundId, final ImageButton btnWidget) {
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

    }

    private void putGameInButton(String game, final ImageButton btnWidget) {
        btnWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.example.Stoeky");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
    }

    private void putImageInButton(String imageId, ImageButton btnWidget) {
        GlideUrl glideUrl = new GlideUrl(PersistenceController.BASEURL + imageId, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + dc.getUser().getToken())
                .build());

        Glide.with(getActivity())
                .load(glideUrl)
                .error(R.drawable.error)
                .into(btnWidget);
    }

    private void putDefaultImageInButton(int resource, ImageButton btnWidget) {
        //btnWidget.setImageResource(R.drawable.game_start_button);
        Glide.with(getActivity())
                .load(resource)
                .error(R.drawable.error)
                .into(btnWidget);
    }

    private Snackbar makeSnackbar(String currentHint)
    {
        final Snackbar mySnackbar =  Snackbar.make(getView(), currentHint, Snackbar.LENGTH_INDEFINITE);

        mySnackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySnackbar.dismiss();
            }
        });

        View sbView = mySnackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setBackgroundResource(R.color.stuckytoys_green);
        textView.setTextColor(Color.WHITE);

        return mySnackbar;
    }
}
