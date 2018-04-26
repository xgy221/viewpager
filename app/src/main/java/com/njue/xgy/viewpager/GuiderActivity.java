package com.njue.xgy.viewpager;

/**
 * Created by XGY on 2017/4/8.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;

public class GuiderActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private int[] mImageIds = new int[]{R.drawable.guide_image1, R.drawable.guide_image2, R.drawable.guide_image3};
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout Container;
    private ImageView ivBluePoint;
    private int mPaintDis;
    private Button start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        start_btn = (Button) findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuiderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.ViewPager);
        Container = (LinearLayout) findViewById(R.id.container);
        ivBluePoint = (ImageView) findViewById(R.id.iv_blue);
        initData();
        GuideAdapter adapter = new GuideAdapter();
        mViewPager.setAdapter(adapter);
        ivBluePoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //避免重复回调        出于兼容性考虑，使用了过时的方法
                ivBluePoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //布局完成了就获取第一个小灰点和第二个之间left的距离
                mPaintDis = Container.getChildAt(1).getLeft()-Container.getChildAt(0).getLeft();
                System.out.println("距离："+mPaintDis);
            }
        });


        //ViewPager滑动Pager监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动过程中的回调
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当滑到第二个Pager的时候，positionOffset百分比会变成0，position会变成1，所以后面要加上position*mPaintDis
                int letfMargin = (int)(mPaintDis*positionOffset)+position*mPaintDis;
                //在父布局控件中设置他的leftMargin边距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)ivBluePoint.getLayoutParams();
                params.leftMargin = letfMargin;
                ivBluePoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("position:"+position);
                if (position==mImageViewList.size()-1){
                    start_btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("state:"+state);
            }
        });
    }

    private void initData(){
        mImageViewList = new ArrayList<>();
        for (int i=0; i<mImageIds.length; i++){
            //创建ImageView把mImgaeViewIds放进去
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(view);


            //小圆点    一个小灰点是一个ImageView
            ImageView pointView = new ImageView(this);
            pointView.setImageResource(R.drawable.shape);
            //初始化布局参数，父控件是谁，就初始化谁的布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i>0){
                //当添加的小圆点的个数超过一个的时候就设置当前小圆点的左边距为10dp;
                params.leftMargin=10;
            }
            //设置小灰点的宽高包裹内容
            pointView.setLayoutParams(params);
            //将小灰点添加到LinearLayout中
            Container.addView(pointView);
        }
    }



    class GuideAdapter extends PagerAdapter {
        //item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }
        //销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}


