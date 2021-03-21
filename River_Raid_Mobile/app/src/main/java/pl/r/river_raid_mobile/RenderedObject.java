package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

import pl.r.river_raid_mobile.enums.RenderedObjectEnum;

public class RenderedObject {
    float width;
    float height;
    private Bitmap bitmap;
    private Context context;
    private RenderedObjectEnum renderedObjectEnum;


    public Bitmap getBitmap() {
        return bitmap;
    }



    public void setHeight(float height) {
        this.height = height;
    }

    public RenderedObject(Context context, RenderedObjectEnum roe, float width, float height) {

        this.context=context;

        this.width = width;
        this.height = height;



        switch (roe){
            case boat:{
                this.renderedObjectEnum=RenderedObjectEnum.boat;
                bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.boat);
                break;
            }
            case fuel:{
                this.renderedObjectEnum=RenderedObjectEnum.fuel;
                bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.fuel);
                break;
            }
            case bridge:{
                this.renderedObjectEnum=RenderedObjectEnum.bridge;
                bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.bridge);
                break;
            }
            case helicopter:{
                this.renderedObjectEnum=RenderedObjectEnum.helicopter;
                bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.helicopter);
                break;
            }
        }
    }
    public void collision(Shoot shoot,GamePoint gamePoint) {
        //todo kolizja 32/32
        if (shoot.isInMove()) {
            if (!renderedObjectEnum.equals(RenderedObjectEnum.exp)) {
                System.out.println(shoot.getBulletWidth() + "  " + this.width + "  | " + shoot.getBulletHeight() + "  " + this.height);
                if ((shoot.getBulletWidth() <= this.width + 64) && (shoot.getBulletWidth() >= this.width - 64)
                        && (shoot.getBulletHeight() <= this.height + 64)
                        && (shoot.getBulletHeight() >= this.height - 64)) {
                    //todo sprecyzować kolizję
                    renderedObjectEnum=RenderedObjectEnum.exp;
                    shoot.setInMove(false);
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exp);
                    gamePoint.setPoints(30);
                }



            }
        }
    }
}
