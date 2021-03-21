package pl.r.river_raid_mobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class GamePoint {
    private int points;
    private double high;
    private double width;
    private Paint paint;


    public GamePoint(int points, double high, double width) {
        this.points = points;
        this.high = high;
        this.width = width;
        this.paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(30);
        paint.setTextSize(150);
    }

    public void addPoint(){
        this.points++;
    }

    public void setPoints(int points) {
        this.points += points;
    }

    public void draw(Canvas canvas) {
        float partOfWidth=(float) width/20;
        float partOfHigh=(float) high/20;
        canvas.drawText(String.valueOf(this.points),8*partOfWidth, 9*partOfHigh,paint);
    }

    public void update() {

    }
}
