package com.example.kanishk.galleryapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import customview.DrawView;


public class MainActivity extends Activity implements View.OnClickListener{
    
    private final String Tag = getClass().getName();
    private File outPutFile = null;
    Uri imageUri;
    LinearLayout tool;
    final int PIC_CROP = 1;
    ImageView imageview;
    TextView cropButton;
    private int mCurrRotation = 0;
    private RotateAnimation rotateAnim;
    private double IMAGE_MAX_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        imageview = (ImageView) findViewById(R.id.imageview);
        imageUri = getIntent().getData();
        imageview.setImageURI(imageUri);
        Picasso.with(this).load(imageUri).resize(700, 600).into(imageview);
        
        tool = (LinearLayout) findViewById(R.id.tool1);
        //setSupportActionBar(tool);
        /*rotateRight = (ImageButton) findViewById(R.id.rotateRight);
        rotateLeft = (ImageButton) findViewById(R.id.rotateLeft);
         rotateRight.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);*/
        cropButton = (TextView) findViewById(R.id.crop);
        cropButton.setOnClickListener(this);
       
       
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.rotateLeft:
                onRotateLeft();
                break;
            case R.id.rotateRight:
                onRotateRight();
                break;
            case R.id.rightbtn:
                openCutActivity(imageview);
                break;*/
            case R.id.leftbtn:
                openStartActivity(imageview);
                break;
            case R.id.crop:
                performCrop(imageUri);
                Toast.makeText(this, "Crop Maximum possible Area", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*") ;
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 512);
            cropIntent.putExtra("outputY", 512);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==PIC_CROP) {
            try {
                if (outPutFile.exists()) {
                    
                    imageview.setAnimation(rotateAnim);
                    Bitmap photo = decodeFile(outPutFile);
                    imageview.setImageBitmap(photo);
                    saveToInternalStorage(photo);
                    
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Intent intent = new Intent(MainActivity.this, CutEraseActivity.class);
                    intent.setData(Uri.fromFile(outPutFile));
                    intent.putExtra("image", byteArray);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    
                    //imgPath =outPutFile.getAbsolutePath();
                   // setImageUri();
                   /* File outputDir = context.getCacheDir(); // context being the Activity pointer
                    try {
                        outPutFile = File.createTempFile("image", ".jpg", outputDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   saveToInternalStorage(photo);
                    outFile.getAbsolutePath();
                    
                   outFile.deleteOnExit();*/
                    
                } else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getCacheDir();
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap decodeFile(File f) throws IOException {
        Bitmap b = null;
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = 1;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();

        return b;
    }
  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void openCutActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CutEraseActivity.class);
       intent.setData(Uri.fromFile(outPutFile));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void openStartActivity(View view) {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        // intent.setData(Uri.fromFile(outPutFile));
        intent.putExtra("EXIT", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void setSupportActionBar(Toolbar tool) {
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        
    }

    public RotateAnimation onRotateRight()
    {
        mCurrRotation %= 360;
        float fromRotation = mCurrRotation;
        float toRotation = mCurrRotation += 90;
        rotateAnim = new RotateAnimation(fromRotation, toRotation, imageview.getWidth() / 2, imageview.getHeight() / 2);
        rotateAnim.setDuration(0); // Use 0 ms to rotate instantly 
        rotateAnim.setFillAfter(true); // Must be true or the animation will reset
        imageview.startAnimation(rotateAnim);
        imageview.setAnimation(rotateAnim);
        return rotateAnim;
    }
    public RotateAnimation onRotateLeft()
    {
        mCurrRotation %= 360;
        float fromRotation = mCurrRotation;
        float toRotation = mCurrRotation -= 90;
        rotateAnim = new RotateAnimation(fromRotation, toRotation, imageview.getWidth() / 2, imageview.getHeight() / 2);
        rotateAnim.setDuration(0); // Use 0 ms to rotate instantly 
        rotateAnim.setFillAfter(true); // Must be true or the animation will reset
        imageview.startAnimation(rotateAnim);
        imageview.setAnimation(rotateAnim);
        return rotateAnim;
    }    
}
 /* public Uri setImageUri() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("com.example.kanishk.galleryapp", Context.MODE_PRIVATE);
        File file = new File(directory,System.currentTimeMillis() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }*/
 /*   private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }*/
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                openStartActivity(imageview);
                return true;
            case R.id.next:
                openCutActivity(imageview);
                return true;
            case R.id.crop:
                performCrop(imageUri);
                return true;
            case R.id.rotateLeft:
                onRotateLeft();
                return true;
            case R.id.rotateRight:
                onRotateRight();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
    
  /*  public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        imageview.invalidate();
        return true;
    }*/
    /*public void CropingIMG() {

              //  final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setType("image/*");

                List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
                int size = list.size();
                if (size == 0) {
                    Toast.makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    intent.setData(imageUri);
                    intent.putExtra("outputX", 512);
                    intent.putExtra("outputY", 512);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);

                    //TODO: don't use return-data tag because it's not return large image data and crash not given any message
                    //intent.putExtra("return-data", true);
                    
                    //Create output file here
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));


                    if (size == 1) {
                        Intent i = new Intent(intent);
                        ResolveInfo res = (ResolveInfo) list.get(0);

                        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                        startActivityForResult(i, CROPING_CODE);
                    } else {
                        for (ResolveInfo res : list) {
                            final CropingOption co = new CropingOption();

                            co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                            co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                            co.appIntent = new Intent(intent);
                            co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                            cropOptions.add(co);
                        }

                      CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Choose Croping App");
                        builder.setCancelable(false);
                        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(cropOptions.get(item).appIntent, CROPING_CODE);
                            }
                        });
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if (imageUri != null) {
                                    getContentResolver().delete(imageUri, null, null);
                                    imageUri = null;
                                }
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            } */

   