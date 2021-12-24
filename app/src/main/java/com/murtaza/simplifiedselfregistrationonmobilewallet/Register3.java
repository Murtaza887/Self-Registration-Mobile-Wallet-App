package com.murtaza.simplifiedselfregistrationonmobilewallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class Register3 extends AppCompatActivity {

    String phoneNumber, otp, code;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    Uri personPhoto;
    FirebaseAuth mAuth;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_register3);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FcmNotificationsSender sender = new FcmNotificationsSender("dipw60j5R8ilOKTjXs23Vp:APA91bGfaQtCW-7BUkhlV3kL4SnEoKgH1dBtF3hB6A7GIPS3y67qC0S1oP6RF03t2GEWTmuRzPt0J8T3IUowaQ_TKego83Txei_Wt-MQhV6yfYAQW9udnyE2bl_i6wUoOC23iKJApFkK", "OTP", "124121", getApplicationContext(),Register3.this);
        sender.SendNotifications();

        mAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("number_");
            personName = extras.getString("personName_");
            personGivenName = extras.getString("personGivenName_");
            personFamilyName = extras.getString("personFamilyName_");
            personEmail = extras.getString("personEmail_");
            personId = extras.getString("personId_");
            code = extras.getString("code");
        }

        editText = findViewById(R.id.otp);
        otp = editText.getText().toString();

        Button btn = findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.18.152/insert.php";
                RequestParams requestParams = new RequestParams();
                requestParams.put("number", phoneNumber);
                requestParams.put("amount", 0);

                new AsyncHttpClient().post(Register3.this, url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        try {
                            JSONObject json = new JSONObject(new String(bytes));
                            Intent intent = new Intent(Register3.this, Home.class);
                            intent.putExtra("number_", phoneNumber);
                            intent.putExtra("personName_", personName);
                            intent.putExtra("personGivenName_", personGivenName);
                            intent.putExtra("personFamilyName_", personFamilyName);
                            intent.putExtra("personEmail_", personEmail);
                            intent.putExtra("personId_", personId);
                            intent.putExtra("amount_", 0);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        DBHelper helper = new DBHelper(Register3.this);
                        SQLiteDatabase database = helper.getWritableDatabase();
                        helper.insertData(100, phoneNumber, 0, database);
                        Intent intent = new Intent(Register3.this, Home.class);
                        intent.putExtra("number_", phoneNumber);
                        intent.putExtra("personName_", personName);
                        intent.putExtra("personGivenName_", personGivenName);
                        intent.putExtra("personFamilyName_", personFamilyName);
                        intent.putExtra("personEmail_", personEmail);
                        intent.putExtra("personId_", personId);
                        intent.putExtra("amount_", 0);
                        startActivity(intent);
                    }
                });
                editText.getText().clear();
            }
        });
    }
}