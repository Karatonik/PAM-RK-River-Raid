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




    public FuelGenerator(Context context, GameController gameController) {
        this.context=context;
        this.widths= gameController.getWidths();
        this.bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.fuel);


    }

    public void draw(Canvas canvas) {
    }


    public void update(GameController gameController) {
        this.widths= gameController.getWidths();
    }
}
