package com.neteast.neteaseeventlib;

import com.neteast.neteaseeventlib.listener.OnClickListener;
import com.neteast.neteaseeventlib.listener.OnTouchListener;

public class Activity {
    public static void main(String[] args) {
        ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
        viewGroup.setName("顶层容器");
        ViewGroup viewGroup1 = new ViewGroup(0, 0, 600, 600);
        viewGroup1.setName("第二级容器");
        View view = new View(0, 0, 300, 300);
        view.setName("自控件");

        viewGroup1.addView(view);
        viewGroup.addView(viewGroup1);
        viewGroup.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("顶级容器的OnTouch事件");
                return false;
            }
        });
        viewGroup1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("第二级的OnTouch事件");
                return false;
            }
        });
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("View的OnTouch事件");
                return false;
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("View的onClick事件");
            }
        });
        MotionEvent motionEvent = new MotionEvent(100, 100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWM);
        //顶层容器传递事件
        viewGroup.dispatchTouchEvent(motionEvent);
    }
}
