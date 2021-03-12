package pl.r.river_raid_mobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class GameInfo {
    private double high;
    private double width;
    private double fuelLevel;

    public GameInfo(double high, double width) {
        this.high = high;
        this.width = width;
        this.fuelLevel=12;



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
        //trzeba zapiać ile jest nie dzielić bo nie działa

    }

    public void update() {
        subtractFuel();
    }


}
