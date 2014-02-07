package com.taskforce.thedaywefightback;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

public class Alarm extends BroadcastReceiver 
{    
   
     @Override
     public void onReceive(Context context, Intent intent) 
     {   
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         wl.acquire();
        
         HttpClient client = new DefaultHttpClient();
         HttpGet request = new HttpGet();
         try {
 			request.setURI(new URI("http://email-congress.taskforce.is/time"));
 	        HttpResponse response = client.execute(request);

				HttpEntity entity = response.getEntity();				
				String responseText = EntityUtils.toString(entity);
 	            JSONObject jsonObj = new JSONObject(responseText);
 	            String istheday = jsonObj.getString("thedaywefightback");
 	        
 	           // Toast.makeText(context, istheday, Toast.LENGTH_LONG).show(); // For example
 	            if (istheday=="true"){
 	            	
 	            	showNotification(context);  
 	           
 	            }
 	                 	            
             
 		} catch (URISyntaxException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (ClientProtocolException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

         wl.release();
     }
 
 public void SetAlarm(Context context)
 {
     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
     Intent i = new Intent(context, Alarm.class);
     PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
 }

 public void CancelAlarm(Context context)
 {
     Intent intent = new Intent(context, Alarm.class);
     PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
     AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
     alarmManager.cancel(sender);
 }
 
 private void showNotification(Context context) {
	
     PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
             new Intent(context, MainActivity.class), 0);

     NotificationCompat.Builder mBuilder =
             new NotificationCompat.Builder(context)
             .setSmallIcon(R.drawable.ic_launcher)
             .setContentTitle("The Day We Fight Back")
             .setContentText("Hello World!");
     mBuilder.setContentIntent(contentIntent);
     mBuilder.setDefaults(Notification.DEFAULT_SOUND);
     mBuilder.setAutoCancel(true);
     NotificationManager mNotificationManager =
         (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
     mNotificationManager.notify(1, mBuilder.build());

 }


}