package com.neteast.neteaseeventlib;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View {
    //存放子控件
    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];
    private TouchTarget mFirstTouchTarget;

    public ViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }


    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }

    //事件分发入口
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println(name + "  dispatchTouchEvent");
        int actionMasked = ev.getActionMasked();
        boolean intercepted = onInterceptTouchEvent(ev);
        boolean handled = false;
        TouchTarget newTouchTarget;
        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercepted) {
            //down
            if (actionMasked == MotionEvent.ACTION_DOWM) {
                final View[] children = mChildren;
                //遍历 倒序（）
                for (int i = mChildren.length - 1; i >= 0; i--) {
                    //View不能接收事件
                    View child = mChildren[i];
                    if (!child.isContainer(ev.getX(), ev.getY())) {
                        continue;
                    }

                    //能够接收事件 分发给child
                    if (dispatchTransformedTouchEvent(ev, child)) {
                        handled = true;
                        newTouchTarget = addTouchTarget(child);
                        break;
                    }
                }
            }
        }
        if (mFirstTouchTarget == null) {
            handled = dispatchTransformedTouchEvent(ev, null);
        }
        return handled;
    }

    private TouchTarget addTouchTarget(View child) {
        final TouchTarget target = TouchTarget.obtain(child);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }

    private static final class TouchTarget {
        private View child;//当前缓存View
        //回收池
        private static TouchTarget sRecycleBin;
        //size
        private static int sRecycledCount;
        private static final Object sRecycleLock = new Object[0];
        public TouchTarget next;

        public static TouchTarget obtain(View child) {
            TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                }
                sRecycleBin = target.next;
                sRecycledCount--;
                target.next = null;
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("已经被回收过了");
            }
            synchronized (sRecycleLock) {
                if (sRecycledCount < 32) {
                    next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycledCount++;
                }
            }
        }
    }


    /**
     * 分发处理
     *
     * @param ev
     * @param child 子控件
     * @return
     */
    private boolean dispatchTransformedTouchEvent(MotionEvent ev, View child) {
        boolean handled = false;
        //child消费了
        if (child != null) {
            handled = child.dispatchTouchEvent(ev);
        } else {
            super.dispatchTouchEvent(ev);
        }
        return handled;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
