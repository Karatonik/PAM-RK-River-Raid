package pl.r.river_raid_mobile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.Random;

public class BackgroundAndRiver {

    private long renderSpeed;
    private  long renderIterator;

    private double high;
    private double width;


    //podział rzeki
    private int segmentNumber;
    private float highSegment;

    //środek
    private float centerPointWidth;

    private float[] widths;

    //flags
    boolean afterRender;
    boolean afterUpdate;

    //river
    private Path path;
    private Paint paint;
    //for generator
    private Random random;
    private double lastGeneratedValue;
    private double oldGeneratedValue;
    private double generateIterator;
    private double tempSubstract;


    //bg
    private Paint paintBg;
    private RectF rect;


    public BackgroundAndRiver(double high, double width,int segmentNumber,long renderSpeed) {
        this.generateIterator=segmentNumber;
        this.lastGeneratedValue=400;
        this.renderIterator=0;
        this.segmentNumber=segmentNumber;
        this.renderSpeed=renderSpeed;
        afterRender=false;
        afterRender=true;

        this.high = high;
        this.width = width;



        this.centerPointWidth =(float)( width/2);

        this.highSegment=(float)(high/(segmentNumber-1));

        this.random = new Random();

        //bg paint and rect
        this.paintBg=new Paint();
        this.paintBg.setStyle(Paint.Style.FILL);
        this.paintBg.setColor(Color.rgb(18,50,5));

        this.rect=new RectF(0,0,(float)( this.width),(float)( this.high));

        this.paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);


        //na tablicy
        this.path=new Path();
        this.widths =new float[segmentNumber];
        for (int i =0;i<widths.length;i++){
            widths[i] = generateWidth();
        }

    }

    public float generateWidth(){
        //return (float) (this.random.nextDouble()*((this.width/2)-(this.width/8))+(this.width/10));
        if(generateIterator==segmentNumber){
            generateIterator=0;
            oldGeneratedValue=lastGeneratedValue;
           this.lastGeneratedValue=this.random.nextDouble()*((this.width/2)-(this.width/8))+(this.width/10);
           this.tempSubstract=oldGeneratedValue/lastGeneratedValue;
           return (float) oldGeneratedValue;
        }else {
            generateIterator++;
            double temp3=generateIterator/segmentNumber;
            double res=oldGeneratedValue+(tempSubstract*temp3);
            return (float) res;
        }
    }

    public void draw(Canvas canvas) {
        if(afterUpdate) {
            afterRender = false;
            //bg render
            canvas.drawRect(this.rect,this.paintBg);

            //river render
            for(int widthInx=0;widthInx<widths.length-1;widthInx++) {
                this.path = new Path();
                path.moveTo(centerPointWidth - widths[widthInx], (float) (high-(highSegment*widthInx)));//1
                path.lineTo(centerPointWidth - widths[widthInx+1], (float) high - (highSegment*(widthInx+1)));//2
                path.lineTo(centerPointWidth + widths[widthInx+1], (float) high - (highSegment*(widthInx+1)));//3
                path.lineTo(centerPointWidth + widths[widthInx], (float) (high-(highSegment*widthInx)));//4
                path.lineTo(centerPointWidth - widths[widthInx], (float) (high-(highSegment*widthInx)));//5
                canvas.drawPath(this.path, this.paint);
            }
            //System.out.println("is rendering");
            afterRender = true;
        }
    }

    public void update() {
        if(afterRender){
            afterUpdate=false;
            //System.out.println("update");
            if(renderIterator>renderSpeed) {
                renderIterator=0;
                if (widths.length - 1 >= 0)
                    System.arraycopy(widths, 1, widths, 0, widths.length - 1);
                widths[segmentNumber - 1] = generateWidth();
            }else{
                renderIterator++;
            }
           afterUpdate=true;
        }
    }
}
