package pl.r.river_raid_mobile;

import android.content.Context;
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
    //flags
    boolean afterRender;
    //spwolnienie gry
    boolean slowGame;

    boolean afterUpdate;
    boolean canBeIsland;
    //generowanie obiektów na mapie - iteratory()
    int helicopterIterator, boatIterator, bridgeIterator, fuelIterator;
    int helicopterItMax, boatItMax, bridgeIMax, fuelItMax;
    private Context context;
    private long renderSpeed;
    private long renderIterator;
    private double high;
    private double width;
    //podział rzeki
    private int segmentNumber;
    private float highSegment;
    //środek
    private float centerPointWidth;
    private float[] widths;
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


    public GameController(Context context, double high, double width, int segmentNumber, long renderSpeed) {

        this.context = context;
        this.high = high;
        this.width = width;

        this.slowGame=true;

        this.renderedObjectList = new ArrayList<>();

        this.generateIterator = segmentNumber;
        this.lastGeneratedValue = 400;

        this.renderIterator = 0;

        this.segmentNumber = segmentNumber;

        this.renderSpeed = renderSpeed;

        afterRender = false;
        afterRender = true;

        this.centerPointWidth = (float) (width / 2);

        this.highSegment = (float) (high / (segmentNumber - 1));

        this.random = new Random();

        initDrawable();

        initSegments();

        //obliczanie subHighObject
        this.subHighObject = highSegment / (renderSpeed) - 4;


        //most
        this.bridgeIMax = 100;
        this.bridgeIterator=0;

    }

    public void initDrawable() {
        //bg paint and rect
        this.paintBg = new Paint();
        this.paintBg.setStyle(Paint.Style.FILL);
        this.paintBg.setColor(Color.rgb(18, 50, 5));

        this.rect = new RectF(0, 0, (float) (this.width), (float) (this.high));

        this.paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    public void initSegments() {
        this.path = new Path();
        this.widths = new float[segmentNumber];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = generateWidth();
        }
    }

    public List<RenderedObject> getRenderedObjectList() {
        return renderedObjectList;
    }

    public float[] getWidths() {
        return widths;
    }

    public float generateWidth() {

        double res;
        if (generateIterator == segmentNumber) {
            generateIterator = 0;
            oldGeneratedValue = lastGeneratedValue;
            this.lastGeneratedValue = this.random.nextDouble() * ((this.width / 2) - (this.width / 8)) + (this.width / 10);
            this.tempSubstract = oldGeneratedValue / lastGeneratedValue;
            res = oldGeneratedValue;
        } else {
            generateIterator++;
            res = oldGeneratedValue + (tempSubstract * generateIterator / segmentNumber);
        }
        return (float) res;
    }

    public double getWidthForPlayerMove() {
        return this.widths[2];
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void draw(final Canvas canvas) {
        if (afterUpdate) {
            afterRender = false;
            //bg render
            canvas.drawRect(this.rect, this.paintBg);

            //river render
            for (int widthInx = 0; widthInx < widths.length - 1; widthInx++) {

                this.path = new Path();
                path.moveTo(centerPointWidth - widths[widthInx], (float) (high - (highSegment * widthInx)));//1
                path.lineTo(centerPointWidth - widths[widthInx + 1], (float) high - (highSegment * (widthInx + 1)));//2
                path.lineTo(centerPointWidth + widths[widthInx + 1], (float) high - (highSegment * (widthInx + 1)));//3
                path.lineTo(centerPointWidth + widths[widthInx], (float) (high - (highSegment * widthInx)));//4
                path.lineTo(centerPointWidth - widths[widthInx], (float) (high - (highSegment * widthInx)));//5
                canvas.drawPath(this.path, this.paint);
            }


            loverObj();


            //renderowanie obiektów losowo na mapie
            renderedObjectList.stream().parallel()
                    .forEach(v -> canvas.drawBitmap(v.getBitmap(), v.width, v.height, paint));


            deleteUselessObj();

            afterRender = true;
        }
    }
    public void changeRenderSpeed(Double actuator){

        if(actuator<0) {
            System.out.println("góra");
            if(slowGame){//jeżeli występuje spowolnienie
                slowGame=false;
                renderSpeed=renderSpeed-1;
                this.subHighObject = highSegment / (renderSpeed) - 4;
            }

        }

        if(actuator>0){
            System.out.println("dół");
            if(!slowGame){//jeżeli nie występuje spowonienie
                slowGame=true;
                renderSpeed=renderSpeed+1;
                this.subHighObject = highSegment / (renderSpeed) - 4;
            }

        }



    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Shoot shoot, GamePoint gamePoint, Player player, GameInfo gameInfo, AudioController audioController, Joystick joystick) {

        getCollisions(shoot,gamePoint, player, gameInfo, audioController);




        if (afterRender) {

            afterUpdate = false;
            if (renderIterator > renderSpeed) {

                changeRenderSpeed(joystick.getActuatorY());

                generateObject();

                renderIterator = 0;
                bridgeIterator++;

                moveObj();

                if (widths.length - 1 >= 0)
                    System.arraycopy(widths, 1, widths, 0, widths.length - 1);
                widths[segmentNumber - 1] = generateWidth();
            } else {
                renderIterator++;
            }
            afterUpdate = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void generateObject() {
        deleteExpObj();

        //renderowanie obiektów jest 2 razy losowane , raz obiekt a potem szerokość
        int generatedIer = random.nextInt(200);

        if((widths[widths.length-1]<=width/10)&&(bridgeIterator>=bridgeIMax)){
            renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.bridge, centerPointWidth- 330, 0));
            bridgeIterator=0;
        }


        if (renderedObjectList.size() <= 16) {
            if (generatedIer > 0 && generatedIer <= 12) {
                float objectWidth = centerPointWidth - widths[widths.length - 1] + 100 + (2 * (widths[widths.length - 1] - 100)) * random.nextFloat();
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.boat, objectWidth, 0));
            }
            if (generatedIer > 12 && generatedIer <= 20) {
                float objectWidth = centerPointWidth - widths[widths.length - 1] + 100 + (2 * (widths[widths.length - 1] - 100)) * random.nextFloat();
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.fuel, objectWidth, 0));
            }
            if (generatedIer > 20 && generatedIer < 30) {
                float objectWidth = centerPointWidth - widths[widths.length - 1] + 100 + (2 * (widths[widths.length - 1] - 100)) * random.nextFloat();
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.helicopterP, objectWidth, 0));
            }
            ///////
            if (generatedIer > 30 && generatedIer < 40) {
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.shipL, (float) width, 0));
            }
            if (generatedIer > 40 && generatedIer < 50) {
                renderedObjectList.add(new RenderedObject(context, RenderedObjectEnum.shipP, 0, 0));
            }
        }
    }

    //strumienie

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteUselessObj(){
        //usuwam niepotrzbene elemnenty z listy
        renderedObjectList = renderedObjectList.stream()
                .filter(v -> !(v.height >= high))
                .collect(Collectors.toList());
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteExpObj() {
        renderedObjectList = renderedObjectList.stream().parallel()
                .filter(v -> !v.getRenderedObjectEnum().equals(RenderedObjectEnum.exp))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void moveObj() {
        renderedObjectList = renderedObjectList.stream().parallel().peek(v -> {
            if (v.getRenderedObjectEnum().equals(RenderedObjectEnum.helicopterL)
                    || v.getRenderedObjectEnum().equals(RenderedObjectEnum.helicopterP)) {
                int temp = random.nextInt(100);
                if (temp < 25) {
                    v.width += 30;
                    v.setBitmap(RenderedObjectEnum.helicopterP);
                }
                if (temp > 25 && temp < 50) {
                    v.width -= 30;
                    v.setBitmap(RenderedObjectEnum.helicopterL);
                }
            }
            if (v.getRenderedObjectEnum().equals(RenderedObjectEnum.shipL)) {
                v.width -= 30;
            }
            if (v.getRenderedObjectEnum().equals(RenderedObjectEnum.shipP)) {
                v.width += 30;
            }
        }).collect(Collectors.toList());

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCollisions(Shoot shoot, GamePoint gamePoint, Player player , GameInfo gameInfo
            , AudioController audioController){
        //Wykrywanie kolizji
        renderedObjectList = renderedObjectList.stream()
                .peek(v -> v.collision(shoot, gamePoint)).collect(Collectors.toList());
        //wykrywanie kolizcji z graczem
        renderedObjectList = renderedObjectList.stream()
                .peek(v -> v.collision(player, gameInfo, audioController))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loverObj(){
        //obniżenie obiektów o 1 segment
        renderedObjectList = renderedObjectList.stream()
                .peek(v -> v.setHeight(v.height + subHighObject))
                .collect(Collectors.toList());
    }



}
