package com.example.kanishk.galleryapp;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201;
    private TextView btn_gallery,btn_camera;
    private final static int  MY_PERMISSIONS_REQUEST_CAMERA=2;
    private Uri mImageCaptureUri;
    private Bitmap selectedphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        if (Build.VERSION.SDK_INT >= 23){
            checkCameraPermission();
        }
        
        btn_gallery = (TextView) findViewById(R.id.gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_CODE);
            }
        });
        btn_camera = (TextView) findViewById(R.id.camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                
                    // Here, thisActivity is the current activity
                   
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                mImageCaptureUri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        });
    }
    public void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(StartActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartActivity.this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(StartActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            ActivityCompat.requestPermissions(StartActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }}
    

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //start audio recording or whatever you planned to do
                    }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(StartActivity.this, Manifest.permission.CAMERA)) {
                            //Show an explanation to the user *asynchronously*
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("This permission is important to take picture.")
                                    .setTitle("Important permission required");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                                }
                            });
                            ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_CAMERA);
                        }else{
                            //Never ask again and handle your app without permission.
                        }
                    }
                
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI : " + mImageCaptureUri);
          //  stringUri = mImageCaptureUri.toString();
            String [] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(mImageCaptureUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            selectedphoto = BitmapFactory.decodeFile(filePath);
            cursor.close();
            Intent intent = new Intent(StartActivity.this,MainActivity.class);
            intent.setData( mImageCaptureUri );
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
          else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
                 System.out.println("Camera Image URI : "+mImageCaptureUri);
                 Intent intent = new Intent(StartActivity.this,MainActivity.class);
                 intent.setData( mImageCaptureUri );
                 startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }}
    
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },3000);
    }
}
