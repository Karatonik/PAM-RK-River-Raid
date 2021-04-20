package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;

public class GameInfo {
    private double high;
    private double width;
    private double fuelLevel;
    private Context context;
    private Bitmap bitmapHp;
    private int hpLevel;
    private MediaPlayer mediaPlayer;
    float partOfWidth,partOfHigh;
    Paint paintbg,paintFuel ,paint1,paint2,paint3;
    RectF rectbg, rectFuel;
    public GameInfo(Context context,double high, double width) {
        this.hpLevel=3;
        this.high = high;
        this.width = width;
        this.fuelLevel=12;
        this.context=context;
         this.bitmapHp= BitmapFactory.decodeResource(context.getResources(),R.drawable.player);

       this.partOfWidth=(float) width/20;
        this.partOfHigh=(float) high/20;

        //bg
         paintbg=new Paint();
        paintbg.setColor(Color.rgb(51,51,51));
        paintbg.setStyle(Paint.Style.FILL);
         rectbg = new RectF(0,0,(float) this.width,(float)this.high);

         //fuel
        paintFuel=new Paint();
        paintFuel.setColor(Color.BLACK);
        paintFuel.setStyle(Paint.Style.STROKE);
        paintFuel.setStrokeWidth(20);
        rectFuel= new RectF(4*partOfWidth,12*partOfHigh,16*partOfWidth,18*partOfHigh);

        //1
        paint1=new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
        paint1.setStrokeWidth(20);

        //2
        paint2 = new Paint();
        paint2.setColor(Color.BLACK);
        paint2.setStrokeWidth(15);
        paint2.setTextSize(60);

        //3
        paint3=new Paint();
        paint3.setColor(Color.YELLOW);
        paint3.setStyle(Paint.Style.FILL_AND_STROKE);
        paint3.setStrokeWidth(20);

    }

    public void setHpLevel(int hpLevel) {
        this.hpLevel = hpLevel;
    }
    public void subtractHpLevel(){
        this.hpLevel=this.hpLevel-1;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void subtractFuel(){
        if(fuelLevel>0) {
            fuelLevel = fuelLevel - 0.01;
        }
    }

    public void addFuel(){
        fuelLevel=12;
    }

    public void draw(Canvas canvas) {

        //bg
        canvas.drawRect(rectbg,paintbg);
        //tabelka paliwa
        canvas.drawRect(rectFuel,paintFuel);



        canvas.drawLine(10*partOfWidth,12*partOfHigh,10*partOfWidth,14*partOfHigh,paint1);
        canvas.drawLine(5*partOfWidth,12*partOfHigh,5*partOfWidth, 14*partOfHigh,paint1);
        canvas.drawLine(15*partOfWidth,12*partOfHigh,15*partOfWidth,14*partOfHigh,paint1);


        //
        canvas.drawText("E",5*partOfWidth,17*partOfHigh,paint2);
        canvas.drawText("F",15*partOfWidth,17*partOfHigh,paint2);


        canvas.drawLine((16*partOfWidth)-((float) (12-fuelLevel)*partOfWidth),14*partOfHigh,
                (16*partOfWidth)-((float) (12-fuelLevel)*partOfWidth),18*partOfHigh,paint3);


        //hp

        for(int i =0;i<hpLevel;i++){
            canvas.drawBitmap(this.bitmapHp,(13+(i*2))*partOfWidth ,2*partOfHigh, paint3);
        }
    }

    public int getHpLevel() {
        return hpLevel;
    }

    public void update() {
        subtractFuel();
    }


}
