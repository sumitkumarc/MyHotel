package ontime.app.utils;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import ontime.app.R;
import ontime.app.customer.doneActivity.MyOrdersListActivity;
import ontime.app.restaurant.ui.Activity.RiderOrderDetails;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    Bitmap bitmap;
    long reminderDateTimeInMilliseconds = 000;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        String noty_type = remoteMessage.getData().get("noty_type");
//        String order_id = remoteMessage.getData().get("order_id");
//        bitmap = getBitmapfromUrl(imageUri);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> mapNotification = remoteMessage.getData();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(remoteMessage.getData().toString());
                JSONObject jsonObject1 = jsonObject.optJSONObject("message");
                if (!isAppIsInBackground(getApplicationContext())) {
                    Intent request_accept_intent = new Intent("ACTION_REFRESH_USER.intent.MAIN");
                    if (jsonObject1.optInt("noty_type") == 4) {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                        System.out.println("=======" + cn.getClassName());
                        if (cn.getClassName().equals("ontime.app.customer.doneActivity.MyOrdersListActivity")) {
                            request_accept_intent.putExtra("REFRESH", "REFRESH");
                            request_accept_intent.putExtra("ORDER_ID", jsonObject1.optString("order_id"));
                            sendBroadcast(request_accept_intent);
                        } else {
                            sendNotification(bitmap, jsonObject1);
                        }
                    } else if (jsonObject1.optInt("noty_type") == 5) {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                        System.out.println("=======" + cn.getClassName());
                        if (cn.getClassName().equals("ontime.app.customer.doneActivity.MyOrdersListActivity")) {
                            request_accept_intent.putExtra("REFRESH", "REFRESH_TWO");
                            request_accept_intent.putExtra("ORDER_ID", jsonObject1.optString("order_id"));
                            sendBroadcast(request_accept_intent);
                        } else {
                            sendNotification(bitmap, jsonObject1);
                        }
                    } else {
                        sendNotification(bitmap, jsonObject1);
                    }
                } else {
                    sendNotification(bitmap, jsonObject1);
                }

                /*String zcxc = jsonObject.optString("noty_type");
                Log.e("", "" + zcxc);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void scheduleJob(String body, String title, String image, String scheduledTime) {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance().beginWith(work).enqueue();
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void handleNow() {

        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {

    }

    Intent intent = null;

    private void sendNotification(Bitmap bitmap, JSONObject remoteMessage) {
        try {

            if (remoteMessage.optInt("noty_type") == 2) {
                intent = new Intent(this, MyOrdersListActivity.class);
                intent.putExtra("order_id", remoteMessage.optString("order_id"));
                intent.putExtra("noty_type", remoteMessage.optString("noty_type"));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else if (remoteMessage.optInt("noty_type") == 4) {
                intent = new Intent(this, MyOrdersListActivity.class);
                intent.putExtra("order_id", remoteMessage.optString("order_id"));
                intent.putExtra("noty_type", remoteMessage.optString("noty_type"));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else if (remoteMessage.optInt("noty_type") == 5) {
                intent = new Intent(this, MyOrdersListActivity.class);
                intent.putExtra("order_id", remoteMessage.optString("order_id"));
                intent.putExtra("noty_type", remoteMessage.optString("noty_type"));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else if (remoteMessage.optInt("noty_type") == 1) {
                intent = new Intent(this, RiderOrderDetails.class);
                intent.putExtra("order_id", remoteMessage.optString("order_id"));
                intent.putExtra("noty_type", remoteMessage.optString("noty_type"));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else if (remoteMessage.optInt("noty_type") == 3) {
                intent = new Intent(this, RiderOrderDetails.class);
                intent.putExtra("order_id", remoteMessage.optString("order_id"));
                intent.putExtra("noty_type", remoteMessage.optString("noty_type"));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            String channelId = getString(R.string.app_name);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setLargeIcon(bitmap)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap))/*Notification with Image*/
                            .setSmallIcon(R.drawable.logo_2)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(remoteMessage.optString("title"))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


}
