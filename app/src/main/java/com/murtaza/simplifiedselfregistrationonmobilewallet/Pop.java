package com.murtaza.simplifiedselfregistrationonmobilewallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Pop extends AppCompatActivity {

    EditText editText;
    String phoneNumber;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_pop);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.4));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("number");
        }

        editText = findViewById(R.id.amountOfMoney);

        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.18.152/read.php";
                new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        try {
                            JSONObject object = new JSONObject(new String(bytes));
                            for (int k = 0; k < object.getJSONArray("users").length(); k++) {
                                JSONObject toAdd = (JSONObject) object.getJSONArray("users").get(k);
                                User user = new User(toAdd.getString("number"), toAdd.getString("amount"));

                                if (user.getNumber().equals(phoneNumber)) {
                                    String url2 = "http://192.168.18.152/update.php";
                                    RequestParams requestParams = new RequestParams();
                                    requestParams.put("id", toAdd.getInt("id"));
                                    requestParams.put("number", phoneNumber);
                                    requestParams.put("amount", Integer.parseInt(user.getAmount()) + Integer.parseInt(editText.getText().toString()));
                                    new AsyncHttpClient().post(Pop.this, url2, requestParams, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                            FcmNotificationsSender sender = new FcmNotificationsSender("eHEnQOFvT2Sqq0tsvfKjLR:APA91bHwu7ooulCCBTHG1h1W5i1xT4FXGZ9r8oJSO4Kmz3HtUQV1xhbll4QmuD_d2BejpRgACpVbw10HQVp8qDgqLqokB5OrSouVVKV6WqOJbCz0i-O5MVRq1s_pTVxt11MZxu_7lZuu", "Money Recieved", "Rs. " + Integer.parseInt(editText.getText().toString()) + " recieved from +923345820814", getApplicationContext(),Pop.this);
                                            sender.SendNotifications();
                                            Toast.makeText(Pop.this, "Rs. " + Integer.parseInt(editText.getText().toString()) + " sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
                                            updateCurrentUser();
                                        }

                                        @Override
                                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }
                });
            }
        });
    }

    protected void updateCurrentUser() {
        String url = "http://192.168.18.152/read.php";
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    JSONObject object = new JSONObject(new String(bytes));
                    for (int k = 0; k < object.getJSONArray("users").length(); k++) {
                        JSONObject toAdd = (JSONObject) object.getJSONArray("users").get(k);
                        User user = new User(toAdd.getString("number"), toAdd.getString("amount"));

                        if (user.getNumber().equals(Home.phoneNumber)) {
                            String url2 = "http://192.168.18.152/update.php";
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("id", toAdd.getInt("id"));
                            requestParams.put("number", Home.phoneNumber);
                            requestParams.put("amount", Integer.parseInt(user.getAmount()) - Integer.parseInt(editText.getText().toString()));
                            new AsyncHttpClient().post(Pop.this, url2, requestParams, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    int toPut = Integer.parseInt(Home.amount) - Integer.parseInt(editText.getText().toString());
                                    Home.amount = Integer.toString(toPut);
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}