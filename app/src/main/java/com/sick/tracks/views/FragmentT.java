package com.sick.tracks.views;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sick.tracks.R;
import com.sick.tracks.pojo.LocationInf;

import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by i.shabaev on 17.09.2015.
 */
public class FragmentT extends Fragment {

    private RelativeLayout linearLayout;
    private Animation animStart;
    private Animation animClose;
    private TextView startTime;
    private TextView stopTime;
    private TextView allTime;
    private TextView distance;
    private Button stop;
    private Chronometer chronometer;
    private TextView speed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t, container, false);

        linearLayout = (RelativeLayout) view.findViewById(R.id.fragment_content);
        initAnimations();
        linearLayout.setAnimation(animStart);

        Calendar c = Calendar.getInstance();
        Date startTimeDate = c.getTime();

        startTime = (TextView) view.findViewById(R.id.start_time_value);
        startTime.setText(startTimeDate.getHours()
                + ":" + startTimeDate.getMinutes()
                + ":" + startTimeDate.getSeconds());
        stopTime = (TextView) view.findViewById(R.id.stop_time_value);
        allTime = (TextView) view.findViewById(R.id.time_all);
        distance = (TextView) view.findViewById(R.id.distance_value);
        stop = (Button) view.findViewById(R.id.stop);
        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        speed = (TextView) view.findViewById(R.id.speed_value);
        stop.setOnClickListener(onClickListener);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        chronometer.setOnChronometerTickListener(chronometerTickListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    Chronometer.OnChronometerTickListener chronometerTickListener =
            new Chronometer.OnChronometerTickListener() {

        @Override
        public void onChronometerTick(Chronometer chronometer) {

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            chronometer.stop();
            ((MainActivity) getActivity()).stopTracking();
            Calendar c = Calendar.getInstance();
            Date stopTimeDate = c.getTime();
            stopTime.setText(stopTimeDate.getHours()
                    + ":" + stopTimeDate.getMinutes()
                    + ":" + stopTimeDate.getSeconds());
        }
    };


    public void onEvent(LocationInf locationInf) {
        Location location = locationInf.getLocation();
        distance.setText(Double.toString(locationInf.getDistance()));
        double time = SystemClock.elapsedRealtime() - chronometer.getBase();
        time = time / 1000;
        time = time / 60;
        time = time / 60;
        double speedD = (locationInf.getDistance()/1000) / time;
        speed.setText(Double.toString(speedD));
    }

    public void close(){
        linearLayout.clearAnimation();
        linearLayout.setAnimation(animClose);
    }

    public void initAnimations() {

        animStart = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_top);
        animStart.setAnimationListener(new Animation.AnimationListener() {
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

        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_top);
        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                android.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.remove(FragmentT.this);
                fragmentTransaction.commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
