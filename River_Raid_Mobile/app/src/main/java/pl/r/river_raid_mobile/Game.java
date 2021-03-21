package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private  Player player;
    private Joystick joystick;
    private BackgroundAndRiver backgroundAndRiver;//todo
    private GameInfo gameInfo;
    private GamePoint gamePoint;
    private ShootButton shootButton;
    private Shoot shoot;
    //private FuelGenerator fuelGenerator;
    public Game(Context context) {
        super(context);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        double width = metrics.widthPixels;
        double height = metrics.heightPixels;
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gamePoint = new GamePoint(0,height/8,width);

        gameLoop = new GameLoop(this,surfaceHolder);

        backgroundAndRiver = new BackgroundAndRiver(getContext(),height,width,30,5);

        gameInfo = new GameInfo(getContext(),height/8,width);

        joystick = new Joystick((int) (width/4),(int)(9*(height/10)),(int)(height/14),(int)(height/16));

        shootButton = new ShootButton(getContext(),(int)(2*(height/10)),(int)(2*(height/10)));
        shootButton.setPosition( (float) (3*(width/4)-(height/12)),(float) (9*(height/10)-(height/12)));

        player = new Player(getContext(),width/2,(width/2),6*(height/8),15);
        shoot = new Shoot(getContext(),player,(float) height/8,20);


        setFocusable(true);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
    switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            //shoot
            shootButton.isPressed(event.getX(),event.getY(),shoot.isInMove());

            if(joystick.isPressed(event.getX(),event.getY())){
                joystick.setIsPressed(true);
            }
            return true;
        case MotionEvent.ACTION_MOVE:
            if(joystick.getIsPressed()){
                joystick.setActuator(event.getX(),event.getY());
            }
            return true;
        case MotionEvent.ACTION_UP:
            joystick.setIsPressed(false);
            joystick.resetActuator();
            return  true;
    }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        backgroundAndRiver.draw(canvas);
        gameInfo.draw(canvas);
        //fuelGenerator.draw(canvas);
        joystick.draw(canvas);
        shootButton.draw(canvas);
        player.draw(canvas);
        shoot.draw(canvas);
        gamePoint.draw(canvas);
        drawFPS(canvas);
        drawUPS(canvas);

    }

    public void drawUPS(Canvas canvas){

        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint =new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        canvas.drawText("UPS:"+averageUPS.substring(0,2),50,100,paint);
    }
    public void drawFPS(Canvas canvas){

        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint =new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        canvas.drawText("FPS:"+averageFPS.substring(0,2),50,200,paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update() {
        backgroundAndRiver.update(shoot, gamePoint);
        gamePoint.update();
        joystick.update();
        player.update(joystick,backgroundAndRiver.getWidthForPlayerMove());
        shoot.update(shootButton,(float) player.getPlayerPosX());
        //fuelGenerator.update(backgroundAndRiver);
        gameInfo.update(player.getHpLevel());
    }
}
