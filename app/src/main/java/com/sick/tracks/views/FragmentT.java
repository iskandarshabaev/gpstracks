package com.sick.tracks.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sick.tracks.Constants;
import com.sick.tracks.R;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t, container, false);

        linearLayout = (RelativeLayout) view.findViewById(R.id.fragment_content);
        initAnimations();
        linearLayout.setAnimation(animStart);
        startTime = (TextView) view.findViewById(R.id.start_time_value);
        stopTime = (TextView) view.findViewById(R.id.stop_time_value);
        allTime = (TextView) view.findViewById(R.id.time_all);
        distance = (TextView) view.findViewById(R.id.distance_value);
        stop = (Button) view.findViewById(R.id.stop);
        if(getArguments() != null){
            startTime.setText(getArguments().getString(Constants.START_TIME));
            stopTime.setText(getArguments().getString(Constants.STOP_TIME));
            allTime.setText(getArguments().getString(Constants.ALL_TIME));
            distance.setText(Integer.toString(getArguments().getInt(Constants.DISTANCE)));
        }
        stop.setOnClickListener(onClickListener);
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            close();
        }
    };

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
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(FragmentT.this);
                fragmentTransaction.commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
