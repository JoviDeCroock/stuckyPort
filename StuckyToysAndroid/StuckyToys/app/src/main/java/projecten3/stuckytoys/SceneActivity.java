package projecten3.stuckytoys;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import projecten3.stuckytoys.adapters.MemberAdapter;
import projecten3.stuckytoys.domain.DomainController;

public class SceneActivity extends AppCompatActivity {

    @BindView(R.id.btnSoundtest)
    ImageButton btnSoundtest;

    private DomainController dc;
    private MemberAdapter mAdapter;
    private MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);

        ButterKnife.bind(this);

        dc = DomainController.getInstance();

        btnSoundtest.setImageResource(R.drawable.plus_sign);

        mediaPlayer = new MediaPlayer();
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

    }

    @OnClick(R.id.btnSoundtest)
    public void playSound() {
        mediaPlayer.start();
    }

}
