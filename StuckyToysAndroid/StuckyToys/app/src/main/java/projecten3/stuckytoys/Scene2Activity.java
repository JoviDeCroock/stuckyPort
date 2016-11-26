package projecten3.stuckytoys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.custom.ServerOfflineHelper;
import projecten3.stuckytoys.domain.DomainController;

public class Scene2Activity extends AppCompatActivity {

    @BindView(R.id.btnSoundtest)
    ImageButton btnSoundtest;

    private DomainController dc;
    private MediaPlayer mediaPlayer = null;
    private int sceneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene2);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        btnSoundtest.setImageResource(R.drawable.bushorn);
        byte[] imageByteArray = Base64.decode(ServerOfflineHelper.PICTURE.toString().split(",")[1], Base64.DEFAULT);

        Glide.with(this)
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
                .into(btnSoundtest);
    }

    @OnClick(R.id.btnSoundtest)
    public void startGame()
    {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.Stoeky");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    @OnClick(R.id.btnNextScene)
    public void nextScene() {
        Intent i = new Intent(Scene2Activity.this, SceneActivity.class);
        i.putExtra("SCENE_NUMBER", 1);
        startActivity(i);
    }

    private void showHint(int i) {
        Toast.makeText(this, "TEST " + i, Toast.LENGTH_LONG).show();
    }


    @OnClick(R.id.btnHint1)
    public void showHint1() {
        showHint(1);
    }
    @OnClick(R.id.btnHint2)
    public void showHint2() {
        showHint(2);
    }
    @OnClick(R.id.btnHint3)
    public void showHint3() {
        showHint(3);
    }

}
