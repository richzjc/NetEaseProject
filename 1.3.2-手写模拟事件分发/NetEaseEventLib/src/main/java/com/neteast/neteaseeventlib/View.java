package com.neteast.neteaseeventlib;

import com.neteast.neteaseeventlib.listener.OnClickListener;
import com.neteast.neteaseeventlib.listener.OnTouchListener;

public class View {
    private int left;
    private int top;
    private int right;
    private int bottom;
    private OnTouchListener onTouchListener;
    private OnClickListener onClickListener;

    public View() {
    }

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    protected boolean isContainer(int x, int y) {
        if (x > left && x < right && y > top && y < bottom) {
            return true;
        }
        return false;
    }

    //接收事件分发
    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println(name + "   View  dispatchTouchEvent");
        //消费
        boolean result = false;
        if (onTouchListener != null && onTouchListener.onTouch(this, event)) {
            result = true;
        }
        if (!result && onTouchEvent(event)) {
            result = true;
        }
        return result;
    }

    private boolean onTouchEvent(MotionEvent event) {
        if (onClickListener != null) {
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }

    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "" + name;
    }
}
