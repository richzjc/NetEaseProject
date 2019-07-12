
# 自定义LoadingView 

=== 

** 首先看一下效果图

![](http://g.recordit.co/wZWw53z2nn.gif)

### 具体的实现细节 可分为如下几个步骤
 
1. 绘制六个小圆， 并用属性动画让小圆旋转起来

2. 扩散，聚合的效果。同样是用属性，计算扩散大圆的半径

3. 实现水波纹的效果


* 第一步 * 

```
绘制背景

canvas.drawColor(mBackgroundColor);

绘制六个小圆

private void drawCircles(Canvas canvas) {
        float rotateAngle = (float) (Math.PI *2 / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            // x = r * cos(a) + centX;
            // y = r * sin(a) + centY;
            float angle = i * rotateAngle + mCurrentRotateAngle;
            float cx = (float) (Math.cos(angle) * mCurrentRotateRadius + mCenterX);
            float cy = (float) (Math.sin(angle) * mCurrentRotateRadius + mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
         }
    }
    
让小圆按照属性动画动起来

 //1.旋转
    private class RotateState extends SplashState{

        private RotateState(){
            mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI *2));
            mValueAnimator.setRepeatCount(2);

            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new MerginState();
                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            //绘制背景
            drawBackground(canvas);
            //绘制6个小球
            drawCircles(canvas);
        }
    }

```

* 第二步 *

```
同样的，先绘制背景

canvas.drawColor(mBackgroundColor);

绘制六个小球

private void drawCircles(Canvas canvas) {
        float rotateAngle = (float) (Math.PI *2 / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            // x = r * cos(a) + centX;
            // y = r * sin(a) + centY;
            float angle = i * rotateAngle + mCurrentRotateAngle;
            float cx = (float) (Math.cos(angle) * mCurrentRotateRadius + mCenterX);
            float cy = (float) (Math.sin(angle) * mCurrentRotateRadius + mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
         }
    }
    
第三步，运用属性动画不断的更改半径，然后调用invalidate达到动画的效果（这里会用到OverShootInterpolator插值器，达到扩散聚合的效果)

 private class MerginState extends SplashState{

        private MerginState(){
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mRotateRadius);
//            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.setInterpolator(new OvershootInterpolator(10f));
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpandState();
                }
            });
            mValueAnimator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }
    
```

* 第三步 * 

```
只需要绘制背景即可

  private void drawBackground(Canvas canvas){
        if (mCurrentHoleRadius > 0){
            //绘制空心圆
            float strokeWidth = mDistance - mCurrentHoleRadius;
            float radius = strokeWidth / 2 + mCurrentHoleRadius;
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(mCenterX,mCenterY, radius, mHolePaint);
        }else{
            canvas.drawColor(mBackgroundColor);
        }
    }
    
    会执行大于0的分支， 把画笔设置成Stroke, 运用属性动画不断的更改mCurrentHoleRadius属性，并调用invalidate方法
    
    //3.水波纹
        private class ExpandState extends SplashState{
    
            public ExpandState() {
                mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mDistance);
    //            mValueAnimator.setRepeatCount(2);
                mValueAnimator.setDuration(mRotateDuration);
                mValueAnimator.setInterpolator(new LinearInterpolator());
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mCurrentHoleRadius = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
    
                mValueAnimator.start();
            }
    
            @Override
            void drawState(Canvas canvas) {
                drawBackground(canvas);
            }
        }
 
   
```




