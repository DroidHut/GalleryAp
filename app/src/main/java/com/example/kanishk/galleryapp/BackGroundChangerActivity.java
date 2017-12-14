package com.example.kanishk.galleryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class BackGroundChangerActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "Touch";
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    ScratchView view;
    boolean crop;
    TextView save,smooth,change_background,color;
    ImageView imageview1,imageview2,imageview3,imageview4,imageview5,imageview6,imageview7,imageview8,
            image1,image2,image3,image4,image5,image6,image7,image8,image9,image10,image11,image12,image13,
            image14,image15,image16,image17, image18, image19, image20;
    LinearLayout layout1,tool;
    Bitmap resultingImage;
    ImageView compositeImageView;
    private Context context=this;
    SeekBar seek;
    private float MAX_ZOOM;
    private Matrix savedMatrix2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_background_changer);
        
        compositeImageView = (ImageView) findViewById(R.id.imageView1);
        //compositeImageView.setOnTouchListener(this);
        Toast.makeText(this, "Set transparency & Background Image", Toast.LENGTH_LONG).show();
        
        tool=(LinearLayout)findViewById(R.id.tool2);
        tool.setVisibility(View.INVISIBLE);
        layout1=(LinearLayout)findViewById(R.id.layout_bck_chng);
                
      //  back= (ImageButton)findViewById(R.id.leftbtn);
      //  back.setOnClickListener(this);
        
        smooth=(TextView)findViewById(R.id.smooth);
        smooth.setOnClickListener(this);
        
        change_background=(TextView)findViewById(R.id.change_background);
        change_background.setOnClickListener(this);
       // change_background.setOnLongClickListener(this);
        
        save=(TextView)findViewById(R.id.save);
        save.setOnClickListener(this);
        
        seek=(SeekBar)findViewById(R.id.seekbar);
        seek.setVisibility(View.INVISIBLE);

        imageview1=(ImageView)findViewById(R.id.image2);
        imageview1.setOnClickListener(this);
        imageview2=(ImageView)findViewById(R.id.image3);
        imageview2.setOnClickListener(this);
        imageview3=(ImageView)findViewById(R.id.image4);
        imageview3.setOnClickListener(this);
        imageview4=(ImageView)findViewById(R.id.image5);
        imageview4.setOnClickListener(this);
        imageview5=(ImageView)findViewById(R.id.image6);
        imageview5.setOnClickListener(this);
        imageview6=(ImageView)findViewById(R.id.image7);
        imageview6.setOnClickListener(this);
        imageview7=(ImageView)findViewById(R.id.image8);
        imageview7.setOnClickListener(this);
        imageview8=(ImageView)findViewById(R.id.image9);
        imageview8.setOnClickListener(this);
        
        
        color =(TextView)findViewById(R.id.image1);
        color.setOnClickListener(this);
        
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                crop = extras.getBoolean("crop");
            }
            int widthOfscreen = 0;
            int heightOfScreen = 0;
        

            DisplayMetrics dm = new DisplayMetrics();
            try {
                getWindowManager().getDefaultDisplay().getMetrics(dm);
            } catch (Exception ex) {
            }
            widthOfscreen = dm.widthPixels;
            heightOfScreen = dm.heightPixels;


            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.girl);
        BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inPurgeable = true; 
      //  Bitmap.createScaledBitmap(bitmap2,400,400, false);
        resultingImage = Bitmap.createBitmap(widthOfscreen,
                    heightOfScreen, bitmap2.getConfig());

            Canvas canvas = new Canvas(resultingImage);
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            Path path = new Path();
            for (int i = 0; i < SomeView.points.size(); i++) {
                path.lineTo(SomeView.points.get(i).x, SomeView.points.get(i).y);
            }
            canvas.drawPath(path, paint);
            if (crop) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            } else {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            }
        canvas.save();
        canvas.translate(50, 50);
            canvas.drawBitmap(bitmap2, 0, 0, paint);
            compositeImageView.setImageBitmap(resultingImage);
              canvas.restore();
        
        }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           // case R.id.leftbtn:
              //  Intent intent = new Intent(BackGroundChangerActivity.this, CutEraseActivity.class);
               // startActivity(intent);
               // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
               // break;
            case R.id.smooth:
                int c= seek.getVisibility();
                if(c ==View.INVISIBLE) {
                    seek.setVisibility(View.VISIBLE);
                }
                else
                {
                    seek.setVisibility(View.INVISIBLE);
                }
              
                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        compositeImageView.setAlpha((float)progress / 100);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            case R.id.change_background:
                int d= tool.getVisibility();
                if(d ==View.INVISIBLE) {
                    tool.setVisibility(View.VISIBLE);
                }
                else
                {
                    tool.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.save:
                compositeImageView.buildDrawingCache();
                compositeImageView.getContext();
                Bitmap newImage = compositeImageView.getDrawingCache();
                File storagePath = new File(Environment.getExternalStorageDirectory() + "/Photo Eraser/");
                storagePath.mkdirs();
                if (!storagePath.mkdirs()) {
                    File file = new File(context.getFilesDir(), "Camera");
                    file.setWritable(true);
                }
                File myImage = new File(storagePath, Long.toString(System.currentTimeMillis()) + ".jpg");
                String name= myImage.toString();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                newImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intnt= new Intent(BackGroundChangerActivity.this,SaveViewActivity.class);
                intnt.putExtra("image", byteArray);
                intnt.putExtra("location", name);
                startActivity(intnt);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                newImage.recycle();
                newImage = null;
                break;
            
            case R.id.image1:
                final Dialog openDialog = new Dialog(context);
                openDialog.setContentView(R.layout.color_palette);
                openDialog.setTitle("Select Color");
                Button button=(Button)openDialog.findViewById(R.id.ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog.dismiss();
                    }
                });
                 image1 = (ImageView)openDialog.findViewById(R.id.color1);
                 image2 = (ImageView)openDialog.findViewById(R.id.color2);
                 image3 = (ImageView)openDialog.findViewById(R.id.color3);
                 image4 = (ImageView)openDialog.findViewById(R.id.color4);
                 image5 = (ImageView)openDialog.findViewById(R.id.color5);
                 image6 = (ImageView)openDialog.findViewById(R.id.color6);
                 image7 = (ImageView)openDialog.findViewById(R.id.color7);
                 image8 = (ImageView)openDialog.findViewById(R.id.color8);
                 image9 = (ImageView)openDialog.findViewById(R.id.color9);
                 image10 = (ImageView)openDialog.findViewById(R.id.color10);
                 image11 = (ImageView)openDialog.findViewById(R.id.color11);
                 image12 = (ImageView)openDialog.findViewById(R.id.color12);
                 image13 = (ImageView)openDialog.findViewById(R.id.color13);
                 image14 = (ImageView)openDialog.findViewById(R.id.color14);
                 image15 = (ImageView)openDialog.findViewById(R.id.color15);
                 image16 = (ImageView)openDialog.findViewById(R.id.color16);
                 image17 = (ImageView)openDialog.findViewById(R.id.color17);
                 image18 = (ImageView)openDialog.findViewById(R.id.color18);
                 image19 = (ImageView)openDialog.findViewById(R.id.color19);
                 image20 = (ImageView)openDialog.findViewById(R.id.color20);
               
                image1.setOnClickListener(this);
                image2.setOnClickListener(this);
                image3.setOnClickListener(this);
                image4.setOnClickListener(this);
                image5.setOnClickListener(this);
                image6.setOnClickListener(this);
                image7.setOnClickListener(this);
                image8.setOnClickListener(this);
                image9.setOnClickListener(this);
                image10.setOnClickListener(this);
                image11.setOnClickListener(this);
                image12.setOnClickListener(this);
                image13.setOnClickListener(this);
                image14.setOnClickListener(this);
                image15.setOnClickListener(this);
                image16.setOnClickListener(this);
                image17.setOnClickListener(this);
                image18.setOnClickListener(this);
                image19.setOnClickListener(this);
                image20.setOnClickListener(this);
                openDialog.show();
                break;
            
            case R.id.color1:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case R.id.color2:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.pink));
                break;
            case R.id.color3:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.purple));
                break;

            case R.id.color4:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.deep_purple));
                break;
            case R.id.color5:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.indigo));
                break;
            case R.id.color6:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case R.id.color7:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.light_blue));
                break;
            case R.id.color8:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.cyan));
                break;
            case R.id.color9:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.teal));
                break;

            case R.id.color10:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case R.id.color11:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.light_green));
                break;
            case R.id.color12:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.lime));
                break;
            case R.id.color13:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.color14:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.amber));
                break;
            case R.id.color15:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.orange));
                break;
            case R.id.color16:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.brown));
                break;
            case R.id.color17:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.grey));
                break;
            case R.id.color18:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.blue_grey));
                break;
            case R.id.color19:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.color20:
                compositeImageView.setBackgroundColor(getResources().getColor(R.color.black));
                break;
            case R.id.image2:
        compositeImageView.setBackgroundResource(R.drawable.new1);
                break;
            case R.id.image3:
                compositeImageView.setBackgroundResource(R.drawable.new2);
                break;
            case R.id.image4:
                compositeImageView.setBackgroundResource(R.drawable.new3);
                break;
            case R.id.image5:
                compositeImageView.setBackgroundResource(R.drawable.new4);
                break;
            case R.id.image6:
                compositeImageView.setBackgroundResource(R.drawable.new5);
                break;
            case R.id.image7:
                compositeImageView.setBackgroundResource(R.drawable.new6);
                break;
            case R.id.image8:
                compositeImageView.setBackgroundResource(R.drawable.new7);
                break;
            case R.id.image9:
                compositeImageView.setBackgroundResource(R.drawable.new8);
                break;     
           
        
        }
    }

   /* private Bitmap smoothImage(Bitmap src) {
            int width = src.getWidth();
            int height = src.getHeight();

            BlurMaskFilter blurMaskFilter;
            Paint paintBlur = new Paint();

            Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(dest);

            //Create background in White
            Bitmap alpha = src.extractAlpha();
            paintBlur.setColor(0xFFFFFFFF);
            canvas.drawBitmap(alpha, 0, 0, paintBlur);

            //Create outer blur, in White
            blurMaskFilter = new BlurMaskFilter(blurValue,BlurMaskFilter.Blur.OUTER);
            paintBlur.setMaskFilter(blurMaskFilter);
            canvas.drawBitmap(alpha, 0, 0, paintBlur);

            //Create inner blur
            blurMaskFilter = new BlurMaskFilter(blurValue,BlurMaskFilter.Blur.INNER);
            paintBlur.setMaskFilter(blurMaskFilter);
            canvas.drawBitmap(src, 0, 0, paintBlur);
        return dest;
        }*/




  /*  @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        int rotation = 25;
        // Dump touch event to log
        dumpEvent(event);
       
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                savedMatrix.set(matrix);
                matrix.postRotate(90);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x,
                            event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                           
                break;
        }
            view.setImageMatrix(matrix);
            return true; // indicate event was handled
            
        }
    
   

    /** Show an event in the LogCat view, for debugging 
    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /** Determine the space between the first two fingers */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers 
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }*/

        @Override
        public void onBackPressed () {
            Intent intent = new Intent(BackGroundChangerActivity.this, MainActivity.class);
            //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // intent.putExtra("EXIT", true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

   
}
       /* view = (ScratchView) findViewById(R.id.imageView1);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); 
        setSupportActionBar(toolbar);
        ImageButton cutButton = (ImageButton) toolbar.findViewById(R.id.scissors);
        cutButton.setOnClickListener(this);
        imgUri = getIntent().getData();
       view.setImageURI(imgUri);

      Picasso.with(this).load(imgUri).resize(700,600).into((Target) view);
        
    }
    private void setSupportActionBar(Toolbar toolbar) {
    }
    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (view.getId()) {
            case R.id.scissors:
                String stringUri = imgUri.toString();
                Bundle b = new Bundle();
                // b.putString("Image", stringUri);
                //fragment.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CutFragment fragment = new CutFragment();
                // fragment= (CutFragment) getFragmentManager().findFragmentById(R.id.imageView1);
               // fragmentTransaction.add(R.id.imageView1, fragment, "Fragment");
                fragmentTransaction.commit();
                break;
        }
    }*/
    
