package com.example.vksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.vksearch.NetUtil.generateURL;
import static com.example.vksearch.NetUtil.getRespondfromURL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText searchField;
    private Button searchButton;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.button);
        result = findViewById(R.id.result);
        searchButton.setOnClickListener(this::onClick);
    }
    class VKQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getRespondfromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        protected void onPostExecute(String response){
            String firstName = null;
            String lastName =null;

            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray jsonArray = jsonResponse.getJSONArray("response");
                JSONObject userInfo= jsonArray.getJSONObject(0);
                firstName = userInfo.getString("first_name");
                lastName = userInfo.getString("last_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String resultingString = "Имя: " + firstName + "\n" + "Фамилия: " + lastName;
            result.setText(resultingString);
        }
    }

    @Override
    public void onClick(View view) {
        URL generatedURL = generateURL( searchField.getText().toString());
        new VKQueryTask().execute(generatedURL);

    }
    }