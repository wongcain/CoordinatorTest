package com.cainwong.coordinatortest;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewGroup bgdImgContainer;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initViews();
    }

    private void initViews(){
        mToolbar = (Toolbar) findViewById( R.id.tab_coord_toolbar );
        mToolbar.setTitle( "This is a test" );
        mToolbar.inflateMenu( R.menu.collapse_toolbar );

        mViewPager = (ViewPager) findViewById(R.id.tab_coord_viewpager);
        mViewPager.setAdapter( new MyPagerAdapter() );

        mTabLayout = (TabLayout) findViewById( R.id.tab_coord_tabs );
        mTabLayout.setupWithViewPager( mViewPager );


        bgdImgContainer = (ViewGroup) findViewById( R.id.tab_coord_background_image_container );

        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) bgdImgContainer.getLayoutParams();
        params.setBehavior(new BackgroundImageFadeBehavior( this, 100));

        ScrollView nestedScrollView = (ScrollView) findViewById( R.id.nested_scroll );
        nestedScrollView.setOnTouchListener( new OnTouchListener() {
            @Override public boolean onTouch( View view, MotionEvent motionEvent ) {
                if(doPassMotionEvent( motionEvent )) {
                    findViewById( R.id.main_coord ).dispatchTouchEvent( motionEvent );
                    return true;
                } else {
                    return false;
                }
            }
        } );
    }


    boolean isScrolling = false;
    private boolean doPassMotionEvent(MotionEvent event){
        if(isScrolling == true){
            if(MotionEvent.ACTION_UP == event.getAction()){
                isScrolling = false;
            }
            return false;
        } else {
            if(MotionEvent.ACTION_DOWN == event.getAction() && isInsideOverlay( event )){
                isScrolling = true;
                return false;
            } else {
                return true;
            }
        }

    }

    private boolean isInsideOverlay( MotionEvent event){
        int y = (int)event.getY();
        View img = findViewById( R.id.overlay_view );
        int top = img.getTop();
        int bottom = img.getBottom();
        Log.d( "TOUCH", String.format("y:%s - top:%s - bottom:%s", y, top, bottom) );
        return y >= top && y <= bottom;
    }


    static class MyPagerAdapter extends PagerAdapter{

        @Override
        public Object instantiateItem( ViewGroup container, int position) {
            ViewGroup view = (ViewGroup) LayoutInflater.from( container.getContext() ).inflate(R.layout.recycler_list, container, false);
            TextView tv = (TextView) view.findViewById( R.id.textview );
            tv.setText( String.format("LIST # %s", position) );
            RecyclerView rw = (RecyclerView) view.findViewById( R.id.recyclerview );
            rw.setLayoutManager( new LinearLayoutManager(view.getContext()) );
            rw.setAdapter( new SimpleStringRecyclerViewAdapter() );
            container.addView( view );
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.format( "TAB %s", position );
        }

        @Override public int getCount() {
            return 4;
        }

        @Override public boolean isViewFromObject( View view, Object object ) {
            return view == object;
        }
    }



    static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mTextView;

            ViewHolder(View view) {
                super(view);
                mTextView = (TextView) view.findViewById(R.id.just_text_tv);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.just_text, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView.setText( String.format("Item number %s", position) );
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }


}
