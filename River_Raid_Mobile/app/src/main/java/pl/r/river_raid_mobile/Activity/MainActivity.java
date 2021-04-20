package pl.r.river_raid_mobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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
        Log.d("MainActivity.java", "onPause()");
        game.pause();
        super.onPause();
    }
    @Override
    protected void onStart() {
        Log.d("MainActivity.java", "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity.java", "onResume()");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d("MainActivity.java", "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity.java", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Comment out "super.onBackPressed()" to disable button
        //super.onBackPressed();
    }
}