package pl.r.river_raid_mobile;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

public class AudioController {
    Context context;

    MediaPlayer mpAplause, mpGame, mpFuel,mpKill;

    public AudioController(Context context) {
        this.context = context;
        mpKill=MediaPlayer.create(context,R.raw.kill);
        mpAplause= MediaPlayer.create(context,R.raw.aplaus);
        mpGame = MediaPlayer.create(context,R.raw.gamem);
        mpFuel=MediaPlayer.create(context,R.raw.lowfuel);
    }
    public void getSubstractHpVoice(){
        mpKill.start();
    }

    public void getAplause(){
        mpAplause.start();
    }
    public void stopAplause(){
        if(mpAplause.isPlaying()){
            mpAplause.stop();
        }
    }



    public  void update(double fuelLevel){
        if(!mpGame.isPlaying()){
            mpGame.start();
        }
        if(fuelLevel<6&& !(mpFuel.isPlaying())){
            mpFuel.start();
        }



    }
}
