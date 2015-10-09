package com.sick.tracks.views;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sick.tracks.BaseService;
import com.sick.tracks.Constants;
import com.sick.tracks.pojo.Position;
import com.sick.tracks.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private FragmentT fragmentT;
    private MarkerOptions markerOptions;
    private Marker me;
    private Polyline track;
    private PolylineOptions polylineOptions;
    private Animation buttonIn;
    private Animation buttonOut;
    private FloatingActionButton floatingActionButton;
    private Date startTime;
    private FrameLayout frameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.faa);
        floatingActionButton.setOnClickListener(onClickListener);

        frameContainer = (FrameLayout) findViewById(R.id.frame_container);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("ffff")
                )

                .build();

        if (isMyServiceRunning(BaseService.class)) {
            int color = getResources().getColor(R.color.yellow);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
            Drawable image = getResources().getDrawable(R.drawable.pause);
            floatingActionButton.setImageDrawable(image);
        } else {
            int color = getResources().getColor(R.color.green);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
            Drawable image = getResources().getDrawable(R.drawable.play);
            floatingActionButton.setImageDrawable(image);
        }

        initAnimations();
        floatingActionButton.setColorFilter(getResources().getColor(R.color.textColorPrimaryInverted));
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mMap.animateCamera(cameraUpdate);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            floatingActionButton.getY();
            if (isMyServiceRunning(BaseService.class)) {
                Intent intent = new Intent(MainActivity.this, BaseService.class);
                stopService(intent);
                int color = getResources().getColor(R.color.green);
                Drawable image = getResources().getDrawable(R.drawable.play);
                floatingActionButton.setImageDrawable(image);
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
                floatingActionButton.setColorFilter(getResources().getColor(R.color.textColorPrimaryInverted));

                fragmentT = new FragmentT();

                Calendar c = Calendar.getInstance();
                Date stopTime = c.getTime();
                long allTime = stopTime.getTime() - startTime.getTime();

                long allTimeSeconds = allTime / (1000);
                long allTimeMinutes = allTime / (1000 * 60);
                long allTimeHours = allTime / (1000 * 60 * 60);
                long allTimeDays = allTime / (1000 * 60 * 60 * 24);

                String allTimeS = allTimeHours + ":" + allTimeMinutes + ":" + allTimeSeconds;

                SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
                String startTimeS = df.format(startTime);
                String stopTimeS = df.format(c.getTime());

                Bundle args = new Bundle();
                args.putString(Constants.START_TIME, startTimeS);
                args.putString(Constants.STOP_TIME, stopTimeS);
                args.putString(Constants.ALL_TIME, allTimeS);
                args.putInt(Constants.DISTANCE, getDistance());
                //fragmentT.setArguments(args);

                /*android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.container, fragmentT, "");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();*/
                //fragmentT.onDestroy();
            } else {

                Calendar c = Calendar.getInstance();
                startTime = c.getTime();


                int color = getResources().getColor(R.color.yellow);
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
                Drawable image = getResources().getDrawable(R.drawable.pause);
                floatingActionButton.setImageDrawable(image);
                floatingActionButton.setColorFilter(getResources().getColor(R.color.textColorPrimaryInverted));

                if (fragmentT != null) {
                    fragmentT.close();
                }

                Intent intent = new Intent(MainActivity.this, BaseService.class);
                startService(intent);


                fragmentT = new FragmentT();
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.frame_container, fragmentT, "");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        }
    };


    public int getDistance() {
        return track.getPoints().size();
    }


    public void initAnimations() {

        buttonOut = AnimationUtils.loadAnimation(this, R.anim.out);
        buttonOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //floatingActionButton.clearAnimation();
                floatingActionButton.setAnimation(buttonIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        buttonIn = AnimationUtils.loadAnimation(this, R.anim.in);
        buttonIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {

            }
        }
    }

    @Subscribe
    public void onEvent(Position position) {
        LatLng latLng = position.getLatLng();
        if (markerOptions == null) {
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            me = mMap.addMarker(markerOptions);

            polylineOptions = new PolylineOptions();
            track = mMap.addPolyline(polylineOptions);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(18)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.animateCamera(cameraUpdate);
        } else {
            me.setPosition(latLng);
            track.setPoints(position.getPoints());
            animateCamera(latLng);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
