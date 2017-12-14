package com.example.kanishk.galleryapp;

;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ZoomFragment extends Fragment {
    Bitmap bitmap;

    public ZoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // TouchImageView view=(TouchImageView)getView().findViewById(R.id.zoomView);  
      //  view.setImageResource(R.drawable.girl);
       // Bundle bundle=getArguments();
       // savedInstanceState.putBundle("image",bundle);
       // String imgPath = getArguments().getString("image");
       // bitmap = BitmapFactory.decodeFile(imgPath);
       // view.setImageBitmap(bitmap);
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zoom, container, false);
       
    }
 /*   private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path,"profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            TouchImageView img=(TouchImageView)getView().findViewById(R.id.zoomView);
            img.setImageBitmap(b);
            return;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }*/
    
}
