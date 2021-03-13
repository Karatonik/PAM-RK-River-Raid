package pl.r.river_raid_mobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Background {
   private double high;
   private double width;
   private Paint paint;
   private RectF rect;




    public Background(double high, double width) {
        this.high = high;
        this.width = width;
        this.paint=new Paint();

        this.rect=new RectF(0,0,(float)( this.width),(float)( this.high));
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(Color.rgb(18,50,5));

    }

    public void draw(Canvas canvas) {
        canvas.drawRect(this.rect,this.paint);
    }

    public void update() {
        this.paint.setColor(Color.rgb(18,51,5));
    }
}
