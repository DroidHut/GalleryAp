package com.example.kanishk.galleryapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CutFragment extends Fragment {
    SomeView view;
    boolean crop;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
       
      
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cut, container, false);
      //  String imgPath = getArguments().getString("Image");
       // bitmap = BitmapFactory.decodeFile(imgPath);
       // view = (SomeView )getView().findViewById(R.id.img);
       // view.setImageBitmap(bitmap);
       /*CutFragment frag= new CutFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(((ViewGroup)frag.getView().getParent()).getId(), frag,"Fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
        // Inflate the layout for this fragment
       //SomeView view =(SomeView)getView().findViewById(R.id.img);
        
    }

  }