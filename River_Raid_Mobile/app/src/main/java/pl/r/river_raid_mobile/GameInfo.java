package pl.r.river_raid_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import pl.r.river_raid_mobile.Activity.GameActivity;
import pl.r.river_raid_mobile.Activity.MainActivity;

public class GameInfo {
    private double high;
    private double width;
    private double fuelLevel;
    private Context context;
    private Bitmap bitmapHp;
    private int hpLevel;
    public GameInfo(Context context,double high, double width) {
        this.hpLevel=1;
        this.high = high;
        this.width = width;
        this.fuelLevel=12;
        this.context=context;
         this.bitmapHp= BitmapFactory.decodeResource(context.getResources(),R.drawable.player);

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
        float partOfWidth=(float) width/20;
        float partOfHigh=(float) high/20;
        //bg
        Paint paint=new Paint();
        paint.setColor(Color.rgb(51,51,51));
        paint.setStyle(Paint.Style.FILL);
        RectF rect = new RectF(0,0,(float) this.width,(float)this.high);
        canvas.drawRect(rect,paint);
        //tabelka paliwa
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        rect= new RectF(4*partOfWidth,12*partOfHigh,16*partOfWidth,18*partOfHigh);
        canvas.drawRect(rect,paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawLine(10*partOfWidth,12*partOfHigh,10*partOfWidth,14*partOfHigh,paint);
        canvas.drawLine(5*partOfWidth,12*partOfHigh,5*partOfWidth, 14*partOfHigh,paint);
        canvas.drawLine(15*partOfWidth,12*partOfHigh,15*partOfWidth,14*partOfHigh,paint);


        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);
        paint.setTextSize(60);
        canvas.drawText("E",5*partOfWidth,17*partOfHigh,paint);
        canvas.drawText("F",15*partOfWidth,17*partOfHigh,paint);

        paint=new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(20);
        canvas.drawLine((16*partOfWidth)-((float) (12-fuelLevel)*partOfWidth),14*partOfHigh,
                (16*partOfWidth)-((float) (12-fuelLevel)*partOfWidth),18*partOfHigh,paint);


        //hp

        for(int i =0;i<hpLevel;i++){
            canvas.drawBitmap(this.bitmapHp,(13+(i*2))*partOfWidth ,2*partOfHigh, paint);
        }
    }

    public int getHpLevel() {
        return hpLevel;
    }

    public void update(int hpLevel) {
        this.hpLevel = hpLevel;
        subtractFuel();
    }


}
