package com.taskforce.thedaywefightback;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start set alarm
        Alarm alarm = new Alarm();
        Context contextX = this.getApplicationContext();
        alarm.SetAlarm(contextX);
        //end set alarm
        
        //weblink
        TextView mTextSample = (TextView) findViewById(R.id.webLink);
        String text = "Visit thedaywefightback.org";
        mTextSample.setText(text);
        Pattern pattern = Pattern.compile("thedaywefightback.org");
        Linkify.addLinks(mTextSample, pattern, "https://");
        //end weblink
      
       TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
       
       Button share_btn = (Button) findViewById(R.id.shareButton);

       share_btn.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
        	   Intent sendIntent = new Intent();
        	   sendIntent.setAction(Intent.ACTION_SEND);
        	   sendIntent.putExtra(Intent.EXTRA_TEXT,
        	       "Thousands of websites are planning a massive online protest against surveillance on February 11th - http://thedaywefightback.org");
        	   sendIntent.setType("text/plain");
        	   startActivity(sendIntent);
           }
       });

  	    View btn = findViewById(R.id.callButton);
  	    btn.setVisibility(View.GONE);
  	    
  	    TextView hideText = (TextView) findViewById(R.id.hideThisTextOnDay);
        TextView Counttext = (TextView) findViewById(R.id.countDownText);

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        try {
			request.setURI(new URI("http://email-congress.taskforce.is/time"));
	        HttpResponse response = client.execute(request);

				HttpEntity entity = response.getEntity();				
				String responseText = EntityUtils.toString(entity);
	            JSONObject jsonObj = new JSONObject(responseText);
	            String days = jsonObj.getString("days");
	            
	            if (days == "0"){
	            	Counttext.setText("CALL CONGRESS!"); 
	    	    	hideText.setVisibility(View.GONE);
	    	    	btn.setVisibility(View.VISIBLE);
	    	    	btn.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF00FF00));
	            }
	            
	            if (days == "1"){
	            	Counttext.setText("TOMORROW!");
	            } else {
	            	Counttext.setText(days+" DAYS LEFT");
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

	    Button mButton = (Button)findViewById(R.id.callButton);

	    mButton.setOnClickListener(
	        new View.OnClickListener()
	        {
	            public void onClick(View view)
	            {
	            	//call action
	            	Intent callIntent = new Intent(Intent.ACTION_CALL);
	                callIntent.setData(Uri.parse("tel:345678987654"));
	                startActivity(callIntent);
	            	//end call action
	            }
	        });   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    } 
}