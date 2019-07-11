package com.neteast.neteaseeventlib.listener;

import com.neteast.neteaseeventlib.MotionEvent;
import com.neteast.neteaseeventlib.View;

public interface OnTouchListener {
    boolean onTouch(View view, MotionEvent event);
}
