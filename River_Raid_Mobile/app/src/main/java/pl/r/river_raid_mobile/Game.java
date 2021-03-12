package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;


public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private  Player player;
    private Joystick joystick;
    private Background background;
    private River river;//todo
    private GameInfo gameInfo;
    private GamePoint gamePoint;
    private Canvas canvas;
    public Game(Context context) {
        super(context);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        double width = metrics.widthPixels;
        double height = metrics.heightPixels;
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gamePoint = new GamePoint(0,height/8,width);

        gameLoop = new GameLoop(this,surfaceHolder);

        river = new River();

        background = new Background(height,width);

        gameInfo = new GameInfo(height/8,width);

        joystick = new Joystick((int) (width/2+10),(int)(9*(height/10)),150,120);

        player = new Player(getContext(),(width/2)-50,6*(height/8)-50,15);


        setFocusable(true);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
    switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
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

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        background.draw(canvas);
        river.draw(canvas);
        gameInfo.draw(canvas);
        joystick.draw(canvas);
        player.draw(canvas);
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

    public void update() {
        gamePoint.update();
        joystick.update();
        player.update(joystick);
        river.update();
        gameInfo.update();
    }
}
