package projecten3.stuckytoys.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import projecten3.stuckytoys.R;
import projecten3.stuckytoys.custom.RealmString;
import projecten3.stuckytoys.domain.DomainController;
import projecten3.stuckytoys.domain.Scene;
import projecten3.stuckytoys.domain.Story;
import projecten3.stuckytoys.domain.Widget;

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
    @BindView(R.id.imgWidget)
    ImageView btnWidget;

    private Scene scene;
    private DomainController dc;
    private Snackbar mSnackbar;
    private String shownHint = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dc = DomainController.getInstance();
        String storyId = (String) getArguments().getSerializable("story");
        String sceneId = (String) getArguments().getSerializable("scene");
        for(Story story : dc.getUser().getStories()) {
          if(story.get_id().equals(storyId)) {
            for (Scene sc : story.getScenes()) {
              if (sc.get_id().equals(sceneId)) {
                this.scene = sc;
              }
            }
          }
        }

        int layoutId = getResources().getIdentifier("fragment_scene", "layout", getActivity().getPackageName());
        /* TODO: Once we have multiple layouts...: search layout based on scene's layout number (for example: fragment_scene2)
        int layoutId = getResources().getIdentifier("fragment_scene" + scene.getLayout(), "layout", getActivity().getPackageName());
        */
        View view = inflater.inflate(layoutId, container, false);

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

            //ImageView btnWidget = new ImageView(getActivity());
            //ImageView btnWidget = (ImageView) getView().findViewById(R.id.imgWidget);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //btnWidget.setLayoutParams(params);
            //btnWidget.setBackgroundDrawable(null);

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
                        putImageInButton(currentWidget.getWidgetFiles().get(0).getBytes(), btnWidget);
                        break;
                    case "ar":
                        break;
                    default:
                        txtError.setText(R.string.scene_error);
                        return;
                }

                //add the widget to the layout
                //widgetContainer.addView(btnWidget);

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

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, px, 0);
            button.setLayoutParams(params);
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setImageResource(R.drawable.hint_button);

            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (!shownHint.equals(currentHint.getName())) {
                      shownHint = currentHint.getName();
                      mSnackbar = Snackbar.make(snackbarLayout, currentHint.getName(), Snackbar.LENGTH_INDEFINITE);
                      mSnackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          mSnackbar.dismiss();
                          shownHint = "";
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
                    } else {
                      mSnackbar.dismiss();
                      shownHint = "";
                    }
                }
            });

            hintContainer.addView(button);
        }

    }

    private void putSoundInButton(byte[] soundBytes, final ImageView btnWidget) {
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

    private void putGameInButton(String game, final ImageView btnWidget) {
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

    private void putImageInButton(byte[] imageBytes, ImageView btnWidget) {
        Glide.with(getActivity())
                .load(imageBytes)
                .error(R.drawable.error)
                .fitCenter()
                .into(btnWidget);
    }

    private void putDefaultImageInButton(int resource, ImageView btnWidget) {
        Glide.with(getActivity())
                .load(resource)
                .error(R.drawable.error)
                .into(btnWidget);
    }
}
