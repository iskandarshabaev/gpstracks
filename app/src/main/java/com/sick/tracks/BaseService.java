package com.sick.tracks;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.sick.tracks.pojo.Position;
import com.sick.tracks.views.MainActivity;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by i.shabaev on 24.09.2015.
 */
public class BaseService extends Service {

    LocationBase locationBase;
    ArrayList<LatLng> points;

    @Override
    public void onCreate() {
        super.onCreate();
        locationBase = new LocationBase(this);
        points = new ArrayList<>();
        EventBus.getDefault().register(this);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.play)
                        // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.play))
                        //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker("")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                        //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("Trail")
                        //.setContentText(res.getString(R.string.notifytext))
                .setContentText("Trail write"); // Текст уведомления

         Notification notification = builder.getNotification(); // до API 16
        //Notification notification = builder.build();
        startForeground (1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationBase.removeLocationRequest();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        points.add(latLng);
        Position position = new Position(latLng, points);
        EventBus.getDefault().post(position);
    }
}
