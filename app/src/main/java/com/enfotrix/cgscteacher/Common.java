package com.enfotrix.cgscteacher;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.enfotrix.cgscteacher.Remote.FCMSendData;
import com.enfotrix.cgscteacher.Remote.IFCMService;
import com.enfotrix.cgscteacher.Remote.RetrofitFCMClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Common {
    public static final String NOTI_TITLE = "title";
    public static final String NOTI_CONTENT = "body";

    public static void sendNotification(String token, Context context, String message) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IFCMService ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put(NOTI_TITLE, "Attendance Alert");
        notificationData.put(NOTI_CONTENT, message);
        FCMSendData fcmSendData = new FCMSendData(token, notificationData);

        compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fcmResponse -> {
                    Log.d("YOMO", fcmResponse.toString());
                    if (fcmResponse.getSuccess() == 0) {
                        compositeDisposable.clear();

                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    compositeDisposable.clear();
                    //Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                }));

    }


}
