package com.murtaza.simplifiedselfregistrationonmobilewallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login3 extends AppCompatActivity {

    String phoneNumber, code;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    FirebaseAuth mAuth;
    EditText editText;
    private String verificationId;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_login3);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FcmNotificationsSender sender = new FcmNotificationsSender("/topics/all", "OTP", "124121", getApplicationContext(),Login3.this);
        sender.SendNotifications();

        mAuth = FirebaseAuth.getInstance();

        if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

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

        Button btn = findViewById(R.id.submit);
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

                                if (user.getNumber().equals(phoneNumber) ) {
                                    try {
                                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, editText.getText().toString());
                                        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(Login3.this, Home.class);
                                                    intent.putExtra("number_", phoneNumber);
                                                    intent.putExtra("personName_", personName);
                                                    intent.putExtra("personGivenName_", personGivenName);
                                                    intent.putExtra("personFamilyName_", personFamilyName);
                                                    intent.putExtra("personEmail_", personEmail);
                                                    intent.putExtra("personId_", personId);
                                                    intent.putExtra("amount_", user.getAmount());
                                                    startActivity(intent);
                                                }
                                                else {
                                                    Toast.makeText(Login3.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Login3.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    catch (Exception e) {
                                        Intent intent = new Intent(Login3.this, Home.class);
                                        intent.putExtra("number_", phoneNumber);
                                        intent.putExtra("personName_", personName);
                                        intent.putExtra("personGivenName_", personGivenName);
                                        intent.putExtra("personFamilyName_", personFamilyName);
                                        intent.putExtra("personEmail_", personEmail);
                                        intent.putExtra("personId_", personId);
                                        intent.putExtra("amount_", user.getAmount());
                                        startActivity(intent);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        DBHelper helper = new DBHelper(Login3.this);
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
                            try {
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, editText.getText().toString());
                                Integer finalValue = value3;
                                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(Login3.this, Home.class);
                                            intent.putExtra("number_", phoneNumber);
                                            intent.putExtra("personName_", personName);
                                            intent.putExtra("personGivenName_", personGivenName);
                                            intent.putExtra("personFamilyName_", personFamilyName);
                                            intent.putExtra("personEmail_", personEmail);
                                            intent.putExtra("personId_", personId);
                                            intent.putExtra("amount_", finalValue.toString());
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(Login3.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login3.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            catch (Exception e) {
                                Intent intent = new Intent(Login3.this, Home.class);
                                intent.putExtra("number_", phoneNumber);
                                intent.putExtra("personName_", personName);
                                intent.putExtra("personGivenName_", personGivenName);
                                intent.putExtra("personFamilyName_", personFamilyName);
                                intent.putExtra("personEmail_", personEmail);
                                intent.putExtra("personId_", personId);
                                intent.putExtra("amount_", value3.toString());
                                startActivity(intent);
                            }
                        } while (cursor.moveToNext());
                    }
                });
                editText.getText().clear();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID,verificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }
}