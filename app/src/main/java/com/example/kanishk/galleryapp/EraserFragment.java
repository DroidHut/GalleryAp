package com.example.kanishk.galleryapp;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class EraserFragment extends Fragment implements View.OnDragListener{
    ScratchView scratchView;
    Button btnReset;

    private int INVALID_POINTER_ID;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float lastTouchX,lastTouchY;
    private ScaleGestureDetector scaleDetector;
    private float posX,posY;

    public EraserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       try{
        scratchView = (ScratchView) getView().findViewById(R.id.scratchView);
       
        
      /*  btnReset = (Button) getView().findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scratchView.resetView();
                scratchView.setScratchAll(false);// todo: should include to resetView?
            }
        });
        */
       }
       catch (NullPointerException e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eraser, container, false);
    }
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        scaleDetector.onTouchEvent(ev);

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                lastTouchX = x;
                lastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - lastTouchX;
                final float dy = y - lastTouchY;

                posX += dx;
                posY += dy;
                
                scratchView.invalidate();
                // Remember this touch position for the next move event
                lastTouchX = x;
                lastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    lastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }
}