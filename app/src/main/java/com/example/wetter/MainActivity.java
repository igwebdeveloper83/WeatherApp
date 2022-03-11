package com.example.wetter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
private EditText editText;
private  Button button_1;
private TextView result_info;
private TextView extra_info;
    private TextView extra_extra;
private ImageView icon;
private double temperature;
private String extra;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.text_input);
        button_1 = findViewById(R.id.button_1);
        result_info = findViewById(R.id.text_view2);
        extra_info = findViewById(R.id.text_view3);
        extra_extra = findViewById(R.id.text_view6);
        icon = findViewById(R.id.icon);

        button_1.setOnClickListener(event -> {
            if (editText.getText().toString().trim().equals("")) {
                Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG ).show();
            } else {
                String input = editText.getText().toString();
                String result = result_info.getText().toString();
                String city = editText.getText().toString();
                String key = ""; /* use your key */
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric";

                new GetURLData().execute(url);
            }
        });
    }
    private class GetURLData extends AsyncTask<String, String, String> {

       protected void onPreExecute() {
           super.onPreExecute();
           result_info.setText("Loading...");
       }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                    return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
           super.onPostExecute(result);
           result_info.setText(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                 temperature = jsonObject.getJSONObject("main").getDouble("temp");
                 extra = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                result_info.setText("Temperature is: " + temperature + " °C");
                extra_extra.setText("Today : " + extra);

                if ( temperature > 0 && temperature < 10) {
                    icon.setImageResource(R.drawable.cold);
                    extra_info.setText("It's cold!");
                }  else if ( temperature > 10 && temperature < 17) {
                    icon.setImageResource(R.drawable.mild);
                    extra_info.setText("It's mild!");
                }  else if ( temperature > 17 && temperature < 35) {
                    icon.setImageResource(R.drawable.hot);
                    extra_info.setText("It's hot");
                }








            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}