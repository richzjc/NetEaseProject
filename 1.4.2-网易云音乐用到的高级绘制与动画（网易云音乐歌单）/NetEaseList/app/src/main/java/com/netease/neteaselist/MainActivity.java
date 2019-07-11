package com.netease.neteaselist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.netease.neteaselist.ui.UIUtils;
import com.netease.neteaselist.ui.ViewCalculateUtil;
import com.netease.neteaselist.util.FastBlurUtil;
import com.netease.neteaselist.util.StatusBarUtil;
import com.netease.neteaselist.util.ToolbarUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private View headView;
    private ImageView iv_header_bg;//顶部背景图片

    private ImageView iv_header_poster;//head中的封面

    private LinearLayout ll_layout6;//head中的封面右边布局
    private LinearLayout ll_layout5;//head中的播放全部布局
    private LinearLayout ll_layout1;//head中的封面图和标题等的包裹布局
    private LinearLayout ll_layout2;//head中的评论下载等按钮的包裹布局
    private LinearLayout ll_layout3;//head中的会员享高品质及下方的悬浮区域的包裹布局
    private LinearLayout ll_layout4;//head中的会员享高品质布局
    private TextView tv_music_list_name;//head中歌单名字
    private FrameLayout fl_layout_float;//真正悬浮的view包裹布局
    private LinearLayout ll_layout_float;//真正悬浮的view布局


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UIUtils.getInstance(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.more));
        toolbar.setTitle("歌单");
        ToolbarUtils.setMarqueeForToolbarTitleView(toolbar);
        setSupportActionBar(toolbar);
        //设置沉浸式，并且toolbar设置padding
        StatusBarUtil.setStateBar(this, toolbar);


        headView = getLayoutInflater().inflate(R.layout.item_lv_header, null);
        iv_header_poster = headView.findViewById(R.id.iv_header_poster);
        ll_layout6 = headView.findViewById(R.id.ll_layout6);
        ll_layout5 = headView.findViewById(R.id.ll_layout5);
        ll_layout1 = headView.findViewById(R.id.ll_layout1);
        ll_layout2 = headView.findViewById(R.id.ll_layout2);
        ll_layout3 = headView.findViewById(R.id.ll_layout3);
        ll_layout4 = headView.findViewById(R.id.ll_layout4);
        iv_header_bg = headView.findViewById(R.id.iv_header_bg);

        tv_music_list_name = headView.findViewById(R.id.tv_music_list_name);

        fl_layout_float = findViewById(R.id.fl_layout_float);
        ll_layout_float = findViewById(R.id.ll_layout_float);
        listView = findViewById(R.id.listView);


        ViewCalculateUtil.setViewLinearLayoutParam(iv_header_poster, 252, 252, 0, 0, 32, 0, true);
        ViewCalculateUtil.setViewLinearLayoutParam(ll_layout6, 560, 252, 0, 0, 16, 0, true);

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setSubtitle("编辑推荐");
                ll_layout1.setPadding(ll_layout1.getPaddingLeft(), toolbar.getBottom() + 10, ll_layout1.getPaddingRight(), ll_layout1.getPaddingBottom());
            }
        });

        try2UpdatePicBackground(R.drawable.poster);
        listView.addHeaderView(headView);
        //创建一个list集合，list集合的元素是Map
        List<Map<String, Object>> ListItems = new ArrayList<Map<String, Object>>();
        for (int i = 1; i < 30; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("music_no", i);
            listItem.put("music_name", "歌曲" + i);
            listItem.put("auth_name", "歌手" + i);
            //加入list集合
            ListItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, ListItems, R.layout.item_list, new String[]{"music_no", "music_name", "auth_name"}, new int[]{R.id.item_position, R.id.music_name, R.id.item_auth});
        listView.setAdapter(simpleAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("headView.getBottom()", headView.getBottom() + "");
                Log.e("headView.getHeight()", headView.getHeight() + "");
                Log.e("layout_title.getbottom", toolbar.getBottom() + "");
                Log.e("ll_layout5.getTop()", ll_layout5.getTop() + "");

                //播放全部与标题栏初始距离
                float originHeight = headView.getHeight() - toolbar.getHeight() - ll_layout5.getHeight();
                //播放全部与标题栏当前
                float currentHeight = headView.getBottom() - toolbar.getHeight() - ll_layout5.getHeight();
                Log.e("currentHeight", currentHeight + "");
                if (originHeight == 0)
                    return;
                //获取相对于屏幕的位置
                Rect rect = new Rect();
                tv_music_list_name.getGlobalVisibleRect(rect);
                Log.e("rect", rect.top + "");
                if (rect.top < 10) {
                    //标题显示歌单名
                    if (!toolbar.getTitle().equals(getResources().getText(R.string.list_name)))
                        toolbar.setTitle(getResources().getText(R.string.list_name));
                } else if (rect.top > 10 && currentHeight > 0) {
                    //标题显示默认  歌单
                    if (!toolbar.getTitle().equals(getResources().getText(R.string.list_name_default)))
                        toolbar.setTitle(getResources().getText(R.string.list_name_default));
                }
                if (currentHeight > 0) {
                    //拖动比
                    float part = currentHeight / originHeight;

                    ll_layout1.setAlpha(1.4f * part);
                    ll_layout2.setAlpha(1.4f * part);
                    ll_layout4.setAlpha(1.4f * part);
                    ll_layout3.getBackground().setAlpha((int) (255 * part));
                    Drawable drawable = toolbar.getBackground();
                    drawable.setAlpha((int) (255 * (1 - part)));
                    toolbar.setBackground(drawable);
                    fl_layout_float.setVisibility(View.GONE);
                } else {
                    ll_layout1.setAlpha(0);
                    ll_layout2.setAlpha(0);
                    ll_layout4.setAlpha(0);
                    Drawable drawable = toolbar.getBackground();
                    drawable.setAlpha(255);
                    toolbar.setBackground(drawable);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ll_layout_float.getLayoutParams();
                    layoutParams.topMargin = toolbar.getHeight();
                    fl_layout_float.setVisibility(View.VISIBLE);
                    fl_layout_float.setBackground(fl_layout_float.getBackground());
                    Log.e("fl_layout_float", fl_layout_float.getBackground().toString());

                }
            }
        });


    }


    private void try2UpdatePicBackground(final int picRes) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fl_layout_float.setBackground(getForegroundDrawable(picRes));
                        iv_header_bg.setImageDrawable(getForegroundDrawable(picRes));
                        toolbar.setBackground(getForegroundDrawable(picRes));
                    }
                });
            }
        }).start();

    }

    private Drawable getForegroundDrawable(int picRes) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (UIUtils.getScreenWidth(this)
                * 1.0 / UIUtils.getScreenHeight(this) * 1.0);

        Bitmap bitmap = getForegroundBitmap(picRes);

        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                .getHeight() / 50, false);
        /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;

    }

    private Bitmap getForegroundBitmap(int picRes) {
        int screenWidth = UIUtils.getScreenWidth(this);
        int screenHeight = UIUtils.getScreenHeight(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(getResources(), picRes, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            return BitmapFactory.decodeResource(getResources(), picRes);
        }

        int sample = 2;
        int sampleX = imageWidth / UIUtils.getScreenWidth(this);
        int sampleY = imageHeight / UIUtils.getScreenHeight(this);

        if (sampleX > sampleY && sampleY > 1) {
            sample = sampleX;
        } else if (sampleY > sampleX && sampleX > 1) {
            sample = sampleY;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = sample;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeResource(getResources(), picRes, options);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music, menu);
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }

        return true;
    }


}
