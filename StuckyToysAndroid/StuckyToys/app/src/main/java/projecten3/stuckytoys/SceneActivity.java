package projecten3.stuckytoys;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.custom.member_related_stuff.MemberAdapter;
import projecten3.stuckytoys.domain.DomainController;

public class SceneActivity extends AppCompatActivity {

    @BindView(R.id.btnSoundtest)
    ImageButton btnSoundtest;

    private DomainController dc;
    private MemberAdapter mAdapter;
    private MediaPlayer mediaPlayer = null;
    private int sceneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        btnSoundtest.setImageResource(R.drawable.bushorn);

        mediaPlayer = MediaPlayer.create(this, R.raw.bushorn);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btnSoundtest.setClickable(true);

        /*
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ODE3ODU0YjE0NTRjNDFhODJlNmM3NzgiLCJleHAiOjE0ODMxMjA0NjAsImlhdCI6MTQ3NzkzNjQ2MH0.tLhph7qwgDUHfygZhYM4tODafkR8UMtPx3ALZOQAAA4");

        Uri uri = Uri.parse("http://188.166.173.147:3000/story/download/582490846081bc3950f5245a");
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(this, uri, header);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    btnSoundtest.setClickable(true);
                }
            });
        } catch (IllegalArgumentException | IOException ex) {
            Log.e("media error", ex.getMessage());
        }
        */

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                sceneNumber = 1;
            } else {
                sceneNumber = extras.getInt("SCENE_NUMBER");
            }
        } else {
            sceneNumber = (int) savedInstanceState.getSerializable("SCENE_NUMBER");
        }



    }

    @OnClick(R.id.btnSoundtest)
    public void playSound() {
        mediaPlayer.start();
    }

    private void showHint(int i) {
        Toast.makeText(this, "Hint  " + i, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnNextScene)
    public void nextScene() {
        Intent i = new Intent(SceneActivity.this, Scene2Activity.class);
        i.putExtra("SCENE_NUMBER", 1);
        startActivity(i);
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
