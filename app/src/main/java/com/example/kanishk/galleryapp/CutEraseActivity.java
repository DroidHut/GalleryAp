package com.example.kanishk.galleryapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class CutEraseActivity extends Activity implements View.OnClickListener{
    
    TextView cut,erase,repair,zoom,undo,redo;
    ImageButton back,done;
    Uri imgUri;
    LinearLayout tool3;
    Bundle bundle;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cut_erase);
        ImageView imgview=(ImageView)findViewById(R.id.frameView);
       
         tool3=(LinearLayout)findViewById(R.id.tool_cut);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imgview.setImageBitmap(bmp);
       image= String.valueOf(bmp);
        //Picasso.with(this).load(String.valueOf(bmp)).resize(700, 600).into(imgview);

        cut=(TextView)findViewById(R.id.scissors);
        cut.setOnClickListener(this);

        erase=(TextView)findViewById(R.id.erase);
        erase.setOnClickListener(this);

        repair=(TextView)findViewById(R.id.repair);
        repair.setOnClickListener(this);

        zoom=(TextView)findViewById(R.id.zoom);
        zoom.setOnClickListener(this);

        back=(ImageButton)findViewById(R.id.leftbtn);
        back.setOnClickListener(this);

        done=(ImageButton)findViewById(R.id.rightbtn);
        done.setOnClickListener(this);
        
       
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scissors:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CutFragment fragment = new CutFragment();
                fragmentTransaction.replace(R.id.pager, fragment);
                fragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack(null);
                Toast.makeText(this, "Draw the area with finger you want to cut", Toast.LENGTH_LONG).show();
                fragmentTransaction.commit();
                break;
            case R.id.erase:
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                EraserFragment fragment2 = new EraserFragment();
                fragmentTransaction2.replace(R.id.pager, fragment2);
                fragmentTransaction2.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction2.addToBackStack(null);
                Toast.makeText(this, "Erase with Finger Touch", Toast.LENGTH_LONG).show();
                fragmentTransaction2.commit();
                break;
            case R.id.zoom:
                FragmentManager fragmentManager3 = getFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                ZoomFragment fragment3 = new ZoomFragment();
              //  bundle = new Bundle();
               // bundle.putString("image",image);
              //  fragment3.setArguments(bundle);
                fragmentTransaction3.replace(R.id.pager, fragment3);
                fragmentTransaction3.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction3.addToBackStack(null);
                Toast.makeText(this, "Finger Pinch Zoom", Toast.LENGTH_SHORT).show();
                fragmentTransaction3.commit();
               
                break;
            case R.id.repair:
                FragmentManager fragmentManager4 = getFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                RepairFragment fragment4 = new RepairFragment();
                fragmentTransaction4.replace(R.id.pager, fragment4);
                fragmentTransaction4.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
              //  tool3.setBackgroundColor(0x110000);
                fragmentTransaction4.addToBackStack(null);
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                fragmentTransaction4.commit();
                break;
            case R.id.rightbtn:
                Intent intent5 = new Intent(CutEraseActivity.this, BackGroundChangerActivity.class);
                startActivity(intent5);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
            case R.id.leftbtn:
                Intent intent6 = new Intent(CutEraseActivity.this, MainActivity.class);
                startActivity(intent6);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;

        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CutEraseActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    
}
  /*  private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }
   
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CutFragment(), "ONE");
        adapter.addFragment(new EraserFragment(), "TWO");
        viewPager.setAdapter(adapter);
    }
    

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        private class Pager {
        }
    }*/

