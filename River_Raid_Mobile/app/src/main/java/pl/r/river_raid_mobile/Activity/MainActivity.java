package pl.r.river_raid_mobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import pl.r.river_raid_mobile.Game;
import pl.r.river_raid_mobile.R;

public class MainActivity extends Activity {
    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window= getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        game = new Game(this);
        setContentView(new Game(this));

    }
    @Override
    protected void onPause() {

        game.pause();
        super.onPause();
    }
}