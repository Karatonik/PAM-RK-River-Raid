package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

public class ShootButton {
    public Matrix btn_matrix = new Matrix();

    public RectF btn_rect;

    float width;
    float height;
    Bitmap bg;
    boolean iWantShoot;
    Paint paint;

    public ShootButton(Context context, float width, float height)
    {
        paint = new Paint();
        iWantShoot=false;
        this.width = width;
        this.height = height;
        this.bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.sb);

        btn_rect = new RectF(0, 0, width, height);
    }

    public void setPosition(float x, float y)
    {
        btn_matrix.setTranslate(x, y);
        btn_matrix.mapRect(btn_rect);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bg, btn_matrix, paint);
    }

    public boolean isiWantShoot() {
        return iWantShoot;
    }

    public void setIWantShoot(boolean iWantShoot) {
        this.iWantShoot = iWantShoot;
    }

    public void isPressed(float touchPositionX, float touchPositionY,boolean inMove) {
        //System.out.println(touchPositionX+" "+touchPositionY);
        if(btn_rect.contains(touchPositionX,touchPositionY)){
            if(!inMove) {//jeżeli pocisk nie jest w drodzę (zabezpeiczenie przed zmianą w trakcie strzału )
                iWantShoot = true;
            }
        }
    }

}
