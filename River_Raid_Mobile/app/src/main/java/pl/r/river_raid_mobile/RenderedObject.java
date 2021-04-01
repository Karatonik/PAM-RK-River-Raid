package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import pl.r.river_raid_mobile.enums.RenderedObjectEnum;

public class RenderedObject {
    float width;
    float height;
    private Bitmap bitmap;
    private Context context;
    private RenderedObjectEnum renderedObjectEnum;


    public RenderedObject(Context context, RenderedObjectEnum roe, float width, float height) {
        this.context = context;
        this.width = width;
        this.height = height;


        switch (roe) {
            case boat: {
                this.renderedObjectEnum = RenderedObjectEnum.boat;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
                break;
            }
            case fuel: {
                this.renderedObjectEnum = RenderedObjectEnum.fuel;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fuel);
                break;
            }

            case helicopterP: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterP;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heli);
                break;
            }

            case helicopterL: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterL;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.helicopter);
                break;
            }
            case shipL: {
                this.renderedObjectEnum = RenderedObjectEnum.shipL;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shipl);
                break;
            }
            case shipP: {
                this.renderedObjectEnum = RenderedObjectEnum.shipP;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shipp);
                break;
            }
            case bridge: {
                this.renderedObjectEnum = RenderedObjectEnum.bridge;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bridge);
                break;
            }
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(RenderedObjectEnum renderedObjectEnum) {
        //todo zmiana obiketu skręt
        switch (renderedObjectEnum) {
            case boat: {
                this.renderedObjectEnum = RenderedObjectEnum.boat;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boat);
                break;
            }
            case fuel: {
                this.renderedObjectEnum = RenderedObjectEnum.fuel;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fuel);
                break;
            }

            case helicopterP: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterP;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heli);
                break;
            }

            case helicopterL: {
                this.renderedObjectEnum = RenderedObjectEnum.helicopterL;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.helicopter);
                break;
            }
            case shipL: {
                this.renderedObjectEnum = RenderedObjectEnum.shipL;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shipl);
                break;
            }
            case shipP: {
                this.renderedObjectEnum = RenderedObjectEnum.shipP;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shipp);
                break;
            }
        }
    }

    public RenderedObjectEnum getRenderedObjectEnum() {
        return renderedObjectEnum;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void collision(Shoot shoot, GamePoint gamePoint) {
        //todo kolizja 32/32
        if (shoot.isInMove()) {
            if (!renderedObjectEnum.equals(RenderedObjectEnum.exp)) {
                if (((shoot.getBulletWidth() <= this.width + 64) && (shoot.getBulletWidth() >= this.width - 64)
                        && (shoot.getBulletHeight() <= this.height + 64)
                        && (shoot.getBulletHeight() >= this.height - 64))
                        ||//most
                        (renderedObjectEnum.equals(RenderedObjectEnum.bridge)
                                && (shoot.getBulletHeight() <= this.height + 64)
                                && (shoot.getBulletHeight() >= this.height - 64))) {
                    //muzyka
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.expa);
                    mediaPlayer.start();
                    shoot.setInMove(false);
                    //punkty
                    switch (renderedObjectEnum) {
                        case bridge: {
                            gamePoint.setPoints(500);
                            break;
                        }
                        case boat: {
                            gamePoint.setPoints(30);
                            break;
                        }
                        case fuel: {
                            gamePoint.setPoints(80);
                            break;
                        }
                        case helicopterL:
                        case helicopterP: {
                            gamePoint.setPoints(60);
                            break;
                        }
                        case shipP:
                        case shipL: {
                            gamePoint.setPoints(100);
                            break;
                        }
                    }
                    renderedObjectEnum = RenderedObjectEnum.exp;
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exp);

                }


            }
        }
    }

    public void collision(Player player, GameInfo gameInfo, AudioController audioController) {
        //todo kolizja 32/32
        if (!renderedObjectEnum.equals(RenderedObjectEnum.exp)) {

            if ((player.getPlayerPosX() <= this.width + 64) && (player.getPlayerPosX() >= this.width - 64)
                    && (player.getPlayerPosY() <= this.height + 64)
                    && (player.getPlayerPosY() >= this.height - 64)) {

                if (renderedObjectEnum.equals(RenderedObjectEnum.fuel)) {
                    renderedObjectEnum = RenderedObjectEnum.exp;
                    gameInfo.addFuel();
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exp);
                } else {
                    renderedObjectEnum = RenderedObjectEnum.exp;
                    gameInfo.subtractHpLevel();
                    audioController.getSubstractHpVoice();
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.exp);
                }
            }
        }
    }
}