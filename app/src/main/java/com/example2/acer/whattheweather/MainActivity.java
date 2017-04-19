package com.example2.acer.whattheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
     TextView resTextView;

    public void findWeather(View view){
        Log.i("City Name",cityName.getText().toString());
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromInputMethod(cityName.getWindowToken(),0);
        try {
            String encodCityName= URLEncoder.encode(cityName.getText().toString(),"UTF-8") ;
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodCityName+"&appid=40035a4e9b5c81f561aabdc543ef38ef");

        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(),"Could not find thr weather",Toast.LENGTH_LONG).show();

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText)findViewById(R.id.cityName);
        resTextView=(TextView)findViewById(R.id.resultTextView);

    }
    class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data != -1){
                    char current=(char)data;
                    result +=current;
                    data=reader.read();
                }
                return result;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Could not find thr weather",Toast.LENGTH_LONG).show();
              //  e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String meseage="";
                JSONObject jsonObject=new JSONObject(result);
                String weatherInfo= jsonObject.getString("weather");
                Log.i("weather Content ",weatherInfo);
                JSONArray array=new JSONArray(weatherInfo);
                for (int i=0;i<array.length(); i++){
                    JSONObject jasonPart=array.getJSONObject(i);
                    String main="";
                    String descrition="";
                    main=jasonPart .getString("main");
                    descrition=jasonPart .getString("description");
                    if (main !="" && descrition !=""){
                        meseage +=main+" : "+descrition+"\r\n";
                    }
                   // Log.i("Main",jasonPart .getString("main"));
                    //Log.i("Description=",jasonPart .getString("description"));

                }
                if ( meseage != ""){
                    resTextView.setText(meseage);
                }else {

                    Toast.makeText(getApplicationContext(),"Could not find thr weather",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
              //  e.printStackTrace();

                Toast.makeText(getApplicationContext(),"Could not find thr weather",Toast.LENGTH_LONG).show();
            }

        }
    }
}
