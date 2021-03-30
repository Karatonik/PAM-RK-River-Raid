package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

import pl.r.river_raid_mobile.enums.RenderedObjectEnum;

public class RenderedObject {
    float width;
    float height;
    private Bitmap bitmap;
    private Context context;
    private RenderedObjectEnum renderedObjectEnum;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(RenderedObjectEnum renderedObjectEnum) {
        //todo zmiana obiketu skręt
        switch (renderedObjectEnum) {
            case boat: {
                this.renderedObjectEnum = RenderedObjectEnum.boat;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
                break;
            }
            case fuel: {
                this.renderedObjectEnum = RenderedObjectEnum.fuel;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fuel);
                break;
            }

            case helicopterP: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterP;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heli);
                break;
            }

            case helicopterL: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterL;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.helicopter);
                break;
            }
        }
    }
    public RenderedObjectEnum getRenderedObjectEnum() {
        return renderedObjectEnum;
    }

    public void setHeight(float height) {
        this.height = height;
    }



    public RenderedObject(Context context, RenderedObjectEnum roe, float width, float height) {
        this.context = context;
        this.width = width;
        this.height = height;


        switch (roe) {
            case boat: {
                this.renderedObjectEnum = RenderedObjectEnum.boat;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
                break;
            }
            case fuel: {
                this.renderedObjectEnum = RenderedObjectEnum.fuel;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fuel);
                break;
            }

            case helicopterP: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterP;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heli);
                break;
            }

            case helicopterL: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterL;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.helicopter);
                break;
            }
        }
    }

    public void collision(Shoot shoot, GamePoint gamePoint) {
        //todo kolizja 32/32
        if (shoot.isInMove()) {
            if (!renderedObjectEnum.equals(RenderedObjectEnum.exp)) {
                if ((shoot.getBulletWidth() <= this.width + 64) && (shoot.getBulletWidth() >= this.width - 64)
                        && (shoot.getBulletHeight() <= this.height + 64)
                        && (shoot.getBulletHeight() >= this.height - 64)) {
                    //todo sprecyzować kolizję
                    renderedObjectEnum = RenderedObjectEnum.exp;
                    //muzyka
                   MediaPlayer mediaPlayer= MediaPlayer.create( context,R.raw.expa);
                    mediaPlayer.start();
                    shoot.setInMove(false);
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exp);
                    gamePoint.setPoints(30);
                }


            }
        }
    }

    public void collision(Player player, GameInfo gameInfo,AudioController audioController) {
        //todo kolizja 32/32
        if (!renderedObjectEnum.equals(RenderedObjectEnum.exp)) {

            if ((player.getPlayerPosX()<= this.width + 64) && (player.getPlayerPosX()>= this.width - 64)
                    && (player.getPlayerPosY() <= this.height + 64)
                    && (player.getPlayerPosY() >= this.height - 64)) {

                if(renderedObjectEnum.equals(RenderedObjectEnum.fuel)){
                    renderedObjectEnum = RenderedObjectEnum.exp;
                    gameInfo.addFuel();
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exp);
                }else {
                    renderedObjectEnum = RenderedObjectEnum.exp;
                    gameInfo.subtractHpLevel();
                    audioController.getSubstractHpVoice();
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exp);
                }
            }
        }
    }
}