package com.murtaza.simplifiedselfregistrationonmobilewallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddMoney extends AppCompatActivity {

    Integer currentValue;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_add_money);
        currentValue = 0;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.5));

        NumberPicker np = findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(10000);
        np.setOnValueChangedListener(onValueChangeListener);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("phoneNumber");
        }

        Button btn = findViewById(R.id.submitAmount);
        btn.setOnClickListener(new View.OnClickListener() {
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
                                    int x = toAdd.getInt("id");
                                    requestParams.put("number", phoneNumber);
                                    requestParams.put("amount", Integer.parseInt(user.getAmount()) + currentValue);
                                    new AsyncHttpClient().post(AddMoney.this, url2, requestParams, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                            Toast.makeText(AddMoney.this, "Rs. " + currentValue.toString() + " added to your Account", Toast.LENGTH_SHORT).show();
                                            FirebaseMessaging.getInstance().subscribeToTopic("all");
                                            FcmNotificationsSender sender = new FcmNotificationsSender("/topics/all", "Money Added", "Rs. " + currentValue.toString() + " added to your Account", getApplicationContext(),AddMoney.this);
                                            sender.SendNotifications();
                                            int toPut = Integer.parseInt(user.getAmount()) + currentValue;
                                            Home.amount = Integer.toString(toPut);
                                        }

                                        @Override
                                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                            DBHelper helper = new DBHelper(AddMoney.this);
                                            SQLiteDatabase database = helper.getWritableDatabase();
                                            ContentValues cv = new ContentValues();
                                            cv.put("id", x);
                                            cv.put("number", phoneNumber);
                                            cv.put("amount", Integer.parseInt(user.getAmount()) + currentValue);
                                            database.update("USERS", cv, "id=" + x, null);
                                            Toast.makeText(AddMoney.this, "Rs. " + currentValue.toString() + " added to your Account", Toast.LENGTH_SHORT).show();
                                            int toPut = Integer.parseInt(user.getAmount()) + currentValue;
                                            Home.amount = Integer.toString(toPut);
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
                        DBHelper helper = new DBHelper(AddMoney.this);
                        SQLiteDatabase database = helper.getReadableDatabase();

                        Cursor cursor = database.rawQuery("SELECT * FROM USERS", new String[]{});
                        if (cursor != null) {
                            cursor.moveToFirst();
                        }

                        do {
                            int value1 = 0;
                            if (cursor != null) {
                                value1 = cursor.getInt(0);
                            }
                            String value2 = null;
                            if (cursor != null) {
                                value2 = cursor.getString(1);
                            }
                            String value3 = null;
                            if (cursor != null) {
                                value3 = cursor.getString(2);
                            }
                            if (value2.equals(phoneNumber)) {
                                DBHelper helper2 = new DBHelper(AddMoney.this);
                                SQLiteDatabase database2 = helper.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put("id", value1);
                                cv.put("number", phoneNumber);
                                cv.put("amount", Integer.parseInt(value3) + currentValue);
                                database2.update("USERS", cv, "id=" + value1, null);
                                Toast.makeText(AddMoney.this, "Rs. " + currentValue.toString() + " added to your Account", Toast.LENGTH_SHORT).show();
                                int toPut = Integer.parseInt(value3) + currentValue;
                                Home.amount = Integer.toString(toPut);
                            }
                        } while(cursor.moveToNext());
                    }
                });
            }
        });
    }

    NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            if (i1 > i) {
                numberPicker.setValue(i1+(9));
                currentValue = i1 + 9;
            } else if (i1 < i) {
                numberPicker.setValue(i1 - (9));
                currentValue = i1 - 9;
            }
        }
    };
}