package com.murtaza.simplifiedselfregistrationonmobilewallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class Login2 extends AppCompatActivity {

    EditText editText;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    Uri personPhoto;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_login2);

        editText = findViewById(R.id.phoneNumber);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            personName = extras.getString("personName_");
            personGivenName = extras.getString("personGivenName_");
            personFamilyName = extras.getString("personFamilyName_");
            personEmail = extras.getString("personEmail_");
            personId = extras.getString("personId_");
        }

        Button btn = findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(editText.getText().toString(), 30, TimeUnit.SECONDS, Login2.this, callback);
            }
        });

        callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Login2.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                TextView tv = findViewById(R.id.enterPrompt);
                tv.setText(e.toString());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(Login2.this, "Code Sent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login2.this, Login3.class);
                intent.putExtra("code", s);
                intent.putExtra("number_", editText.getText().toString());
                intent.putExtra("personName_", personName);
                intent.putExtra("personGivenName_", personGivenName);
                intent.putExtra("personFamilyName_", personFamilyName);
                intent.putExtra("personEmail_", personEmail);
                intent.putExtra("personId_", personId);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}