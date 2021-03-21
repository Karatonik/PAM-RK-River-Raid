package pl.r.river_raid_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import pl.r.river_raid_mobile.enums.RenderedObjectEnum;

public class BackgroundAndRiver {

    private Context context;

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

    boolean canBeIsland;

    //river
    private Path path;
    private Paint paint;
    //for generator
    private Random random;
    private double lastGeneratedValue;
    private double oldGeneratedValue;
    private double generateIterator;
    private double tempSubstract;


    //lista obiektów renderowanych takich jak łódz czy helikopter
    private List<RenderedObject> renderedObjectList;

    //zmienna do szybkości poruszania się obiektów względem tła (neutralizuje teleportowanie się obiektów)
    private float subHighObject;




    //bg
    private Paint paintBg;
    private RectF rect;


    public BackgroundAndRiver(Context context, double high, double width, int segmentNumber, long renderSpeed) {

        this.context=context;

        this.renderedObjectList=new ArrayList<>();
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



        this.path=new Path();
        this.widths =new float[segmentNumber];
        for (int i =0;i<widths.length;i++){
            widths[i] = generateWidth();
        }

        //obliczanie subHighObject
        this.subHighObject = highSegment/renderSpeed;



    }

    public List<RenderedObject> getRenderedObjectList() {
        return renderedObjectList;
    }

    public float[] getWidths() {
        return widths;
    }

    public float generateWidth(){

        double res;
        if(generateIterator==segmentNumber){
            generateIterator=0;
            oldGeneratedValue=lastGeneratedValue;
           this.lastGeneratedValue=this.random.nextDouble()*((this.width/2)-(this.width/8))+(this.width/10);
           this.tempSubstract=oldGeneratedValue/lastGeneratedValue;
           res= oldGeneratedValue;
        }else {
            generateIterator++;
            res=oldGeneratedValue+(tempSubstract*generateIterator/segmentNumber);
        }
        return (float) res;
    }
    public double getWidthForPlayerMove(){
        return this.widths[2];
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void draw(final Canvas canvas) {
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



            //obniżenie obiektów o 1 segment
            renderedObjectList= renderedObjectList.stream()
                    .peek(v->v.setHeight(v.height+subHighObject))
                    .collect(Collectors.toList());
            //System.out.println("obnizenie");


            //renderowanie obiektów losowo na mapie
            renderedObjectList.stream().parallel()
                    .forEach(v-> canvas.drawBitmap(v.getBitmap(),v.width,v.height, this.paint));
            //System.out.println("renderowanie obiektów");

            //usuwam niepotrzbene elemnenty z listy
            renderedObjectList = renderedObjectList.stream()
                    .filter(v-> !(v.height >= high))
                    .collect(Collectors.toList());
            //System.out.println("usuwanie niepotrzebnych z listy");

            //todo generowanie w różnych miejscach
            // renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.boat,centerPointWidth,0));



            //System.out.println("is rendering");
            afterRender = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Shoot shoot,GamePoint gamePoint) {

        //Wykrywanie kolizji
        //todo nie działa
        renderedObjectList  =renderedObjectList.stream().peek(v->v.collision(shoot,gamePoint)).collect(Collectors.toList());


        if(afterRender){
            afterUpdate=false;
            //System.out.println("update");
            if(renderIterator>renderSpeed) {
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.boat,centerPointWidth,0));
                System.out.println("dodanie do listy");
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
