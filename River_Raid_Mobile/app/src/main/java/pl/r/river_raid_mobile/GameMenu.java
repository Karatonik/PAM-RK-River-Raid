package pl.r.river_raid_mobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class GameMenu {
    float width;
    float height;
    Paint paintBg;
    Paint paintStart;
    Paint paintText;
    Paint paintResText;
    private RectF rect;
    private RectF rectStart;
   private  int points;


    public GameMenu(float width, float height, int points) {
        this.width = width;
        this.height = height;
        this.points = points;
        init();





    }




    public GameMenu(float width, float height) {
        this.width = width;
        this.height = height;
        this.points=0;
        init();



    }

    public void draw(Canvas canvas) {
        canvas.drawRect(this.rect,this.paintBg);
      //  canvas.drawRect(this.rectStart,this.paintStart);
        canvas.drawText("START",(width/4),this.height/2,paintText);

        if(points>0){
            canvas.drawText("SCORE: "+points,(width/4),3*height/4,paintResText);
        }


    }

    public boolean startIsPressed(float x, float y) {
        if(x>=(width/4)&&x<=3*(width/4)&&y>=(3*this.height/8)&&y<=(this.height/2)){
            return  true;
        }
        return  false;
    }


    private void init(){
        paintText=new Paint();
        paintText.setStyle(Paint.Style.FILL);
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(250);

        paintResText= new Paint();
        paintResText.setStyle(Paint.Style.FILL);
        paintResText.setColor(Color.YELLOW);
        paintResText.setTextSize(120);

        this.paintBg=new Paint();
        this.paintBg.setStyle(Paint.Style.FILL);
        this.paintBg.setColor(Color.rgb(18,50,5));


        this.rect=new RectF(0,0, this.width,this.height);

        this.paintStart=new Paint();
        this.paintStart.setStyle(Paint.Style.FILL);
        this.paintStart.setColor(Color.BLACK);

        this.rectStart=new RectF((width/4),3*this.height/8, 3*(width/4),this.height/2);
    }
}
