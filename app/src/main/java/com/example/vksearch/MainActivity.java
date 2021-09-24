package com.example.vksearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.vksearch.NetUtil.generateURL;
import static com.example.vksearch.NetUtil.getRespondFromURL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText searchField;
    private Button searchButton;
    private TextView result;
    private TextView error;
    private ProgressBar loading;

    private void showResultText(){
        result.setVisibility(View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
    }

    private void showErrorText(){
        error.setVisibility(View.VISIBLE);
        result.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.button);
        result = findViewById(R.id.result);
        error = findViewById(R.id.error);
        loading = findViewById(R.id.loading_indicator);
        searchButton.setOnClickListener(this::onClick);
    }
    class VKQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getRespondFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        protected void onPostExecute(String response){
            String firstName = null;
            String lastName =null;

            if(response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    String resultingString = "";
                    for (int i=0; i< jsonArray.length(); i++){

                        JSONObject userInfo = jsonArray.getJSONObject(i);
                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");
                      resultingString += "Имя: " + firstName + "\n" + "Фамилия: " + lastName + "\n\n" ;
                    }
                    result.setText(resultingString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            showResultText();
        }
            else{
                showErrorText();
            }
            loading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        URL generatedURL = generateURL( searchField.getText().toString());
        new VKQueryTask().execute(generatedURL);

    }
    }