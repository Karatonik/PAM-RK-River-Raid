package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Shoot {
    private float bulletWidth;
    private float bulletHeight;
    private float bulletMinHeight;
    private float bulletMaxHeight;
    private Paint paint;
    private Bitmap bitmapBullet;
    private float bulletSpeed;
    private Context context;
    private boolean inMove;
    public Shoot(Context context, Player player,float bulletMaxHeight,float bulletSpeed) {
        this.bulletSpeed=bulletSpeed;
        inMove=false;
        paint= new Paint();
        this.bulletMaxHeight=bulletMaxHeight;
        bitmapBullet= BitmapFactory.decodeResource(context.getResources(),R.drawable.bullet);
        bulletHeight= (float) player.getPlayerPosY();
        bulletMinHeight=bulletHeight;
        bulletWidth= (float) player.getPlayerPosX();

    }

    public Context getContext() {
        return context;
    }

    public float getBulletWidth() {
        return bulletWidth;
    }

    public float getBulletHeight() {
        return bulletHeight;
    }

    public void draw(Canvas canvas) {
        if(inMove){
            canvas.drawBitmap(this.bitmapBullet, bulletWidth,bulletHeight, this.paint);
        }
    }

    public void setInMove(boolean inMove) {
        this.inMove = inMove;
    }

    public boolean isInMove() {
        return inMove;
    }

    public void update(ShootButton shootButton, float bulletWidth) {
        this.bulletWidth=bulletWidth;
        if(shootButton.isiWantShoot()&&!inMove){
            inMove=true;
            bulletHeight=bulletMinHeight;
            shootButton.setIWantShoot(false);
        }
        if(inMove){
            bulletHeight=bulletHeight-bulletSpeed;
        }
        if (bulletHeight<=bulletMaxHeight){
            inMove=false;
        }

    }
}
