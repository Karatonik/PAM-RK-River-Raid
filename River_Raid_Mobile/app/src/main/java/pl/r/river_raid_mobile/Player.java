package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import pl.r.river_raid_mobile.enums.EnumDir;

public class Player {
    private double playerPosX;
    private double playerPosY;
    private Paint paint;
    private Bitmap bitmapPlayer;
    private double playerMaxSpeed;
    private Context context;
    private double centerWidth;

    public Player(Context context,double centerWidth, double playerPosX, double playerPosY, double playerMaxSpeed) {
        this.centerWidth=centerWidth;
        this.context=context;
        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;
        this.playerMaxSpeed = playerMaxSpeed;

        this.paint = new Paint();

        this.bitmapPlayer= BitmapFactory.decodeResource(context.getResources(),R.drawable.player);
        
    }
    public void setPlayerRotation(Double velocity){
        if(velocity==0) {
            this.bitmapPlayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        }
        if(velocity<0){
            this.bitmapPlayer =BitmapFactory.decodeResource(context.getResources(),R.drawable.player_left);

        }
        if(velocity>0) {
            this.bitmapPlayer = BitmapFactory.decodeResource(context.getResources(),R.drawable.player_right);
        }

    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.bitmapPlayer, (float)this.playerPosX,(float) this.playerPosY, paint);
    }

    public double getPlayerPosX() {
        return playerPosX;
    }

    public double getPlayerPosY() {
        return playerPosY;
    }

    public void update(Joystick joystick, double maxWidth) {
        setPlayerRotation(joystick.getActuatorX());

        double temp=playerPosX+joystick.getActuatorX()*playerMaxSpeed;


        if((temp>=centerWidth-maxWidth)&&(temp<=(centerWidth+maxWidth-bitmapPlayer.getWidth()))){
            playerPosX=temp;
        }else {
            if (centerWidth<playerPosX){
                playerPosX=centerWidth+maxWidth-bitmapPlayer.getWidth();
            }else {
                playerPosX=centerWidth-maxWidth;
            }

        }
    }
}
