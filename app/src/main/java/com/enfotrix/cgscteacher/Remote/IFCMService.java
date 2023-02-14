package com.enfotrix.cgscteacher.Remote;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAYrHcGW0:APA91bH06wiCMwKagOqqoZcowWBaPLJ54tjy_6NAe2HsHoO9H4YTWEpUOAZ4xrIDArNYuziHzeR77sxUMXWxsivPhGUIbzogKNEtPB3212WPwJR0mlR-q2LEoBszklXZSE0zfC7TK2FU"

    })

    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}
