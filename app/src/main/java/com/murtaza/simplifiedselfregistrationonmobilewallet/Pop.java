package com.murtaza.simplifiedselfregistrationonmobilewallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
                                            FcmNotificationsSender sender = new FcmNotificationsSender("f_nFYMBCSTqV_loMADFXGf:APA91bG5CF13Ajt2bML_IIgfcdpl_rAr2MpHTa3vhpMiuT3V5XzM9rJoKjW69Q3Oto3W4g6JdedXIH-gTvjON6OeOOJELEnxlZFNGzoJhR8BovBI39qdAa7LJHsuOfn32dTZxxoLEaan", "Money Recieved", "Rs. " + Integer.parseInt(editText.getText().toString()) + " recieved from +923345820814", getApplicationContext(),Pop.this);
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
                        DBHelper helper = new DBHelper(Pop.this);
                        SQLiteDatabase database = helper.getReadableDatabase();

                        Cursor cursor = database.rawQuery("SELECT * FROM USERS WHERE number = '" + phoneNumber + "'", new String[]{});
                        if (cursor != null)
                            cursor.moveToFirst();

                        do {
                            Integer value1 = 0;
                            if (cursor != null) {
                                value1 = cursor.getInt(1);
                            }
                            String value2 = null;
                            if (cursor != null) {
                                value2 = cursor.getString(2);
                            }
                            Integer value3 = 0;
                            if (cursor != null) {
                                value3 = cursor.getInt(3);
                            }
                            DBHelper helper2 = new DBHelper(Pop.this);
                            SQLiteDatabase database2 = helper2.getWritableDatabase();

                            ContentValues cv = new ContentValues();
                            cv.put("id", value1);
                            cv.put("number", value2);
                            cv.put("amount", value3 + Integer.parseInt(editText.getText().toString()));
                            database.update("USERS", cv, "id=" + value1, null);

                            FcmNotificationsSender sender = new FcmNotificationsSender("f_nFYMBCSTqV_loMADFXGf:APA91bG5CF13Ajt2bML_IIgfcdpl_rAr2MpHTa3vhpMiuT3V5XzM9rJoKjW69Q3Oto3W4g6JdedXIH-gTvjON6OeOOJELEnxlZFNGzoJhR8BovBI39qdAa7LJHsuOfn32dTZxxoLEaan", "Money Recieved", "Rs. " + Integer.parseInt(editText.getText().toString()) + " recieved from +923345820814", getApplicationContext(),Pop.this);
                            sender.SendNotifications();
                            Toast.makeText(Pop.this, "Rs. " + Integer.parseInt(editText.getText().toString()) + " sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
                            updateCurrentUser();
                        } while (cursor.moveToNext());
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
                DBHelper helper = new DBHelper(Pop.this);
                SQLiteDatabase database = helper.getReadableDatabase();

                Cursor cursor = database.rawQuery("SELECT * FROM USERS WHERE number = '" + Home.phoneNumber + "'", new String[]{});
                if (cursor != null)
                    cursor.moveToFirst();

                do {
                    Integer value1 = 0;
                    if (cursor != null) {
                        value1 = cursor.getInt(1);
                    }
                    String value2 = null;
                    if (cursor != null) {
                        value2 = cursor.getString(2);
                    }
                    Integer value3 = 0;
                    if (cursor != null) {
                        value3 = cursor.getInt(3);
                    }
                    DBHelper helper2 = new DBHelper(Pop.this);
                    SQLiteDatabase database2 = helper2.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put("id", value1);
                    cv.put("number", value2);
                    cv.put("amount", value3 - Integer.parseInt(editText.getText().toString()));
                    database.update("USERS", cv, "id=" + value1, null);
                    int toPut = value3 - Integer.parseInt(editText.getText().toString());
                    Home.amount = Integer.toString(toPut);
                } while (cursor.moveToNext());
            }
        });
    }
}