package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class FuelGenerator {
    Context context;

    float[] widths;
    private Bitmap bitmap;




    public FuelGenerator(Context context, BackgroundAndRiver backgroundAndRiver) {
        this.context=context;
        this.widths=backgroundAndRiver.getWidths();
        this.bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.fuel);


    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        //canvas.drawBitmap(this.bitmap, (float)this.playerPosX,(float) this.playerPosY, this.paint);
    }


    public void update(BackgroundAndRiver backgroundAndRiver) {
        this.widths=backgroundAndRiver.getWidths();
    }
}
