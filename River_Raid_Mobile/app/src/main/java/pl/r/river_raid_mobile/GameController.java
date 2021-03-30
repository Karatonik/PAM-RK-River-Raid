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

public class GameController {

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







    //generowanie obiektów na mapie - iteratory()
    int helicopterIterator , boatIterator,bridgeIterator,fuelIterator;
    int helicopterItMax , boatItMax, bridgeIMax,fuelItMax;




    public void initDrawable(){
        //bg paint and rect
        this.paintBg=new Paint();
        this.paintBg.setStyle(Paint.Style.FILL);
        this.paintBg.setColor(Color.rgb(18,50,5));

        this.rect=new RectF(0,0,(float)( this.width),(float)( this.high));

        this.paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }
    public void initSegments(){
        this.path=new Path();
        this.widths =new float[segmentNumber];
        for (int i =0;i<widths.length;i++){
            widths[i] = generateWidth();
        }
    }




    public GameController(Context context, double high, double width, int segmentNumber, long renderSpeed) {

        this.context=context;
        this.high = high;
        this.width = width;


        this.renderedObjectList=new ArrayList<>();

        this.generateIterator=segmentNumber;
        this.lastGeneratedValue=400;

        this.renderIterator=0;

        this.segmentNumber=segmentNumber;

        this.renderSpeed=renderSpeed;

        afterRender=false;
        afterRender=true;

        this.centerPointWidth =(float)( width/2);

        this.highSegment=(float)(high/(segmentNumber-1));

        this.random = new Random();

        initDrawable();

        initSegments();

        //obliczanie subHighObject
        this.subHighObject = highSegment/(renderSpeed)-4;



        //most
        this.bridgeIMax=segmentNumber*10;


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

            //renderowanie obiektów losowo na mapie
            renderedObjectList.stream().parallel()
                    .forEach(v-> canvas.drawBitmap(v.getBitmap(),v.width,v.height, paint));


            //usuwam niepotrzbene elemnenty z listy
            renderedObjectList = renderedObjectList.stream()
                    .filter(v-> !(v.height >= high))
                    .collect(Collectors.toList());

            afterRender = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Shoot shoot,GamePoint gamePoint,Player player, GameInfo gameInfo,AudioController audioController) {

        //Wykrywanie kolizji
        renderedObjectList  =renderedObjectList.stream().peek(v->v.collision(shoot,gamePoint)).collect(Collectors.toList());
        //wykrywanie kolizcji z graczem
        renderedObjectList = renderedObjectList.stream().peek(v-> v.collision(player,gameInfo,audioController)).collect(Collectors.toList());







        if(afterRender){
            afterUpdate=false;
            //System.out.println("update");
            if(renderIterator>renderSpeed) {
                generateObject();
                System.out.println("dodanie do listy");
                renderIterator=0;

                //ruch helikopterów
                renderedObjectList = renderedObjectList.stream().parallel().peek(v->{
                    if(v.getRenderedObjectEnum().equals(RenderedObjectEnum.helicopterL)
                            ||v.getRenderedObjectEnum().equals(RenderedObjectEnum.helicopterP)){
                        int temp=random.nextInt(100);
                        if(temp<25){
                            v.width+=30;
                            v.setBitmap(RenderedObjectEnum.helicopterP);
                        }
                        if(temp>25&&temp<50){
                            v.width-=30;
                            v.setBitmap(RenderedObjectEnum.helicopterL);
                        }

                    }
                }).collect(Collectors.toList());



                if (widths.length - 1 >= 0)
                    System.arraycopy(widths, 1, widths, 0, widths.length - 1);
                widths[segmentNumber - 1] = generateWidth();
            }else{
                renderIterator++;
            }
           afterUpdate=true;
        }
    }
    public void InitIterators(){


        helicopterIterator=0;
        boatIterator=0;
        bridgeIterator=0;
        fuelIterator=0;

        //wartości maksymalne
        helicopterItMax=segmentNumber*15;
        boatItMax=segmentNumber*40;
        bridgeIMax=segmentNumber*20;
        fuelItMax=segmentNumber*10;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void generateObject(){
        //usuwanie niepotrzebnych elementów
        renderedObjectList=   renderedObjectList.stream().parallel().filter(v->{
            return !v.getRenderedObjectEnum().equals(RenderedObjectEnum.exp);
        }).collect(Collectors.toList());

        //renderowanie obiektów jest 2 razy losowane , raz obiekt a potem szerokość
       int generatedIer= random.nextInt(200);

        if(renderedObjectList.size()<=16) {
            if (generatedIer > 0 && generatedIer <= 12) {
                float objectWidth = centerPointWidth - widths[widths.length - 1] + 100 + (2 * (widths[widths.length - 1] -100)) * random.nextFloat();
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.boat, objectWidth, 0));
            }
            if(generatedIer>12&&generatedIer<=20) {
                float objectWidth = centerPointWidth - widths[widths.length - 1] + 100 + (2 * (widths[widths.length - 1] -100)) * random.nextFloat();
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.fuel, objectWidth, 0));
            }
            if(generatedIer>20&&generatedIer<30) {
                float objectWidth = centerPointWidth - widths[widths.length - 1] + 100 + (2 * (widths[widths.length - 1] -100)) * random.nextFloat();
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.helicopterP, objectWidth, 0));
            }
        }

        }
}
