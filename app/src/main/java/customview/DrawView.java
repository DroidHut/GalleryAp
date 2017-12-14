package customview;

/**
 * Created by Kanishk on 2/14/2017.
 */
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.kanishk.galleryapp.R;

public class DrawView extends ImageView {
    
  Paint paint = new Paint();
    private int initial_size = 300;
    private static Point leftTop, rightBottom, center, previous;
    private static final int DRAG= 0;
    private static final int LEFT= 1;
    private static final int TOP= 2;
    private static final int RIGHT= 3;
    private static final int BOTTOM= 4;
    private int imageScaledWidth,imageScaledHeight;
    // Adding parent class constructors   
    public DrawView(Context context) {
        super(context);
        setFocusable(true); // necessary for getting the touch events
        initCropView();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setFocusable(true); // necessary for getting the touch events
        initCropView();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCropView();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(leftTop.equals(0, 0))
            resetPoints();
        canvas.drawRect(leftTop.x,leftTop.y,rightBottom.x , rightBottom.y, paint);
       // canvas.drawLine(leftTop.x,leftTop.y,rightBottom.x,rightBottom.y,paint);
   
       
            //  Find Screen size first  
          /*  DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;
            int screenHeight = (int) (metrics.heightPixels*0.9);

            //  Set paint options  
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.argb(255, 255, 255, 255));

            canvas.drawLine((screenWidth/3)*2,0,(screenWidth/3)*2,screenHeight,paint);
            canvas.drawLine((screenWidth/3),0,(screenWidth/3),screenHeight,paint);
            canvas.drawLine(0,(screenHeight/3)*2,screenWidth,(screenHeight/3)*2,paint);
            canvas.drawLine(0,(screenHeight/3),screenWidth,(screenHeight/3),paint);
             this.setWillNotDraw(false);*/

} 

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int event_action = event.getAction(); 
        switch (event_action) {
            case MotionEvent.ACTION_DOWN:
                    previous.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if(isActionInsideRectangle(event.getX(), event.getY())) {
                    adjustRectangle((int)event.getX(), (int)event.getY());
                    invalidate(); // redraw rectangle
                    previous.set((int)event.getX(), (int)event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                previous = new Point();
                break;
        }
        return true;
    }

    private void initCropView() {
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        leftTop = new Point();
        rightBottom = new Point();
        center = new Point();
        previous = new Point();
    }

    public void resetPoints() {
        center.set(getWidth()/2, getHeight()/2);
        leftTop.set((getWidth()-initial_size)/2,(getHeight()-initial_size)/2);
        rightBottom.set(leftTop.x+initial_size, leftTop.y+initial_size);
    }

    private static boolean isActionInsideRectangle(float x, float y) {
        int buffer = 10;
        return (x>=(leftTop.x-buffer)&&x<=(rightBottom.x+buffer)&& y>=(leftTop.y-buffer)&&y<=(rightBottom.y+buffer))?true:false;
    }

    private boolean isInImageRange(PointF point) {
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        getImageMatrix().getValues(f);

        // Calculate the scaled dimensions
        imageScaledWidth = Math.round(getDrawable().getIntrinsicWidth() * f[Matrix.MSCALE_X]);
        imageScaledHeight = Math.round(getDrawable().getIntrinsicHeight() * f[Matrix.MSCALE_Y]);

        return (point.x>=(center.x-(imageScaledWidth/2))&&point.x<=(center.x+(imageScaledWidth/2))
                &&point.y>=(center.y-(imageScaledHeight/2))&&point.y<=(center.y+(imageScaledHeight/2)))?true:false;
    }

    private void adjustRectangle(int x, int y) {
        int movement;
        switch(getAffectedSide(x,y)) {
            case LEFT:
                movement = x-leftTop.x;
                if(isInImageRange(new PointF(leftTop.x+movement,leftTop.y+movement)))
                    leftTop.set(leftTop.x+movement,leftTop.y+movement);
                break;
            case TOP:
                movement = y-leftTop.y;
                if(isInImageRange(new PointF(leftTop.x+movement,leftTop.y+movement)))
                    leftTop.set(leftTop.x+movement,leftTop.y+movement);
                break;
            case RIGHT:
                movement = x-rightBottom.x;
                if(isInImageRange(new PointF(rightBottom.x+movement,rightBottom.y+movement)))
                    rightBottom.set(rightBottom.x+movement,rightBottom.y+movement);
                break;
            case BOTTOM:
                movement = y-rightBottom.y;
                if(isInImageRange(new PointF(rightBottom.x+movement,rightBottom.y+movement)))
                    rightBottom.set(rightBottom.x+movement,rightBottom.y+movement);
                break;
            case DRAG:
                movement = x-previous.x;
                int movementY = y-previous.y;
                if(isInImageRange(new PointF(leftTop.x+movement,leftTop.y+movementY)) && isInImageRange(new PointF(rightBottom.x+movement,rightBottom.y+movementY))) {
                    leftTop.set(leftTop.x+movement,leftTop.y+movementY);
                    rightBottom.set(rightBottom.x+movement,rightBottom.y+movementY);
                }
                break;
        }
    }

    private static int getAffectedSide(float x, float y) {
        int buffer = 10;
        if(x>=(leftTop.x-buffer)&&x<=(leftTop.x+buffer))
            return LEFT;
        else if(y>=(leftTop.y-buffer)&&y<=(leftTop.y+buffer))
            return TOP;
        else if(x>=(rightBottom.x-buffer)&&x<=(rightBottom.x+buffer))
            return RIGHT;
        else if(y>=(rightBottom.y-buffer)&&y<=(rightBottom.y+buffer))
            return BOTTOM;
        else
            return DRAG;
    }

    public byte[] getCroppedImage() {
        BitmapDrawable drawable = (BitmapDrawable)getDrawable();
        float x = leftTop.x-center.x+(drawable.getBitmap().getWidth()/2);
        float y = leftTop.y-center.y+(drawable.getBitmap().getHeight()/2);
        Bitmap cropped = Bitmap.createBitmap(drawable.getBitmap(),(int)x,(int)y,(int)rightBottom.x-(int)leftTop.x,(int)rightBottom.y-(int)leftTop.y);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cropped.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
        
    }
}


       /* Point point1, point3;
        Point point2, point4;

        //point1 and point 3 are of same group and same as point 2 and point4
        
        int groupId = -1;
        private ArrayList < ColorBall > colorballs = new ArrayList < ColorBall > ();
        // array that holds the balls
        private int balID = 0;
        // variable to know what ball is being dragged
        Paint paint;
        Canvas canvas;

        public DrawView(Context context) {
            super(context);
            paint = new Paint();
            setFocusable(true); // necessary for getting the touch events
            canvas = new Canvas();
            // setting the start point for the balls
            point1 = new Point();
            point1.x = 50;
            point1.y = 10;

            point2 = new Point();
            point2.x = 200;
            point2.y = 10;

            point3 = new Point();
            point3.x = 200;
            point3.y = 120;

            point4 = new Point();
            point4.x = 50;
            point4.y = 120;

            // declare each ball with the ColorBall class
            colorballs.add(new ColorBall(context, R.drawable.dot, point1));
            colorballs.add(new ColorBall(context, R.drawable.dot, point2));
            colorballs.add(new ColorBall(context, R.drawable.dot, point3));
            colorballs.add(new ColorBall(context, R.drawable.dot, point4));

        }

        public DrawView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public DrawView(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint = new Paint();
            setFocusable(true); // necessary for getting the touch events
            canvas = new Canvas();
            // setting the start point for the balls
            point1 = new Point();
            point1.x = 50;
            point1.y = 20;

            point2 = new Point();
            point2.x = 150;
            point2.y = 20;

            point3 = new Point();
            point3.x = 150;
            point3.y = 120;

            point4 = new Point();
            point4.x = 50;
            point4.y = 120;

            // declare each ball with the ColorBall class
            colorballs.add(new ColorBall(context, R.drawable.dot, point1));
            colorballs.add(new ColorBall(context, R.drawable.dot, point2));
            colorballs.add(new ColorBall(context, R.drawable.dot, point3));
            colorballs.add(new ColorBall(context, R.drawable.dot, point4));

        }

        // the method that draws the balls
        @Override
        protected void onDraw(Canvas canvas) {
            // canvas.drawColor(0xFFCCCCCC); //if you want another background color

           // paint.setAntiAlias(true);
           // paint.setDither(true);
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            // mPaint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(5);

           // canvas.drawRect(point4.x,point4.y,point2.x,point2.y,paint);
           // paint.setColor(Color.CYAN);

            if (groupId == 1) {
                canvas.drawRect(point1.x + colorballs.get(0).getWidthOfBall() / 2,
                        point3.y + colorballs.get(2).getWidthOfBall() / 2, point3.x + colorballs.get(2).getWidthOfBall() / 2, point1.y + colorballs.get(0).getWidthOfBall() / 2, paint);
            } else {
                canvas.drawRect(point2.x + colorballs.get(1).getWidthOfBall() / 2,
                        point4.y + colorballs.get(3).getWidthOfBall() / 2, point4.x + colorballs.get(3).getWidthOfBall() / 2, point2.y + colorballs.get(1).getWidthOfBall() / 2, paint);
            }
            BitmapDrawable mBitmap;
            mBitmap = new BitmapDrawable();

            // draw the balls on the canvas
            for (ColorBall ball: colorballs) {
                canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                        new Paint());
            }
        }

        // events when touching the screen
        public boolean onTouchEvent(MotionEvent event) {
            int eventaction = event.getAction();

            int X = (int) event.getX();
            int Y = (int) event.getY();

            switch (eventaction) {

                case MotionEvent.ACTION_DOWN:
                    // touch down so check if the finger is on
                    // a ball
                    balID = -1;
                    groupId = -1;
                    for (ColorBall ball: colorballs) {
                        // check if inside the bounds of the ball (circle)
                        // get the center for the ball
                        int centerX = ball.getX() + ball.getWidthOfBall();
                        int centerY = ball.getY() + ball.getHeightOfBall();
                        paint.setColor(Color.CYAN);
                        // calculate the radius from the touch to the center of the ball
                        double radCircle = Math.sqrt((double)(((centerX - X) * (centerX - X)) + (centerY - Y) * (centerY - Y)));

                        if (radCircle < ball.getWidthOfBall()) {

                            balID = ball.getID();
                            if (balID == 1 || balID == 3) {
                                groupId = 2;
                                canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                        paint);
                            } else {
                                groupId = 1;
                                canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                        paint);
                            }
                            invalidate();
                            break;
                        }
                        invalidate();
                    }

                    break;

                case MotionEvent.ACTION_MOVE:
                    // touch drag with the ball
                    // move the balls the same as the finger
                    if (balID > -1) {
                        colorballs.get(balID).setX(X);
                        colorballs.get(balID).setY(Y);

                        paint.setColor(Color.CYAN);

                        if (groupId == 1) {
                            colorballs.get(1).setX(colorballs.get(0).getX());
                            colorballs.get(1).setY(colorballs.get(2).getY());
                            colorballs.get(3).setX(colorballs.get(2).getX());
                            colorballs.get(3).setY(colorballs.get(0).getY());
                            canvas.drawRect(point1.x, point3.y, point3.x, point1.y,
                                    paint);
                        } else {
                            colorballs.get(0).setX(colorballs.get(1).getX());
                            colorballs.get(0).setY(colorballs.get(3).getY());
                            colorballs.get(2).setX(colorballs.get(3).getX());
                            colorballs.get(2).setY(colorballs.get(1).getY());
                            canvas.drawRect(point2.x, point4.y, point4.x, point2.y,
                                    paint);
                        }

                        invalidate();
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    // touch drop - just do things here after dropping

                    break;
            }
            // redraw the canvas
            invalidate();
            return true;

        }

        public void shade_region_between_points() {
            canvas.drawRect(point1.x, point3.y, point3.x, point1.y, paint);
        }
    }*/
  