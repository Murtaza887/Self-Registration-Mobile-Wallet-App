package com.murtaza.simplifiedselfregistrationonmobilewallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    Context applicationContext;
    GoogleSignInClient mGoogleSignInClient;
    Bitmap bitmap;
    String personName, personGivenName, personFamilyName, personEmail, personId, phoneNumber, amount;
    Uri personPhoto;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

        applicationContext = Home.getContextOfApplication();
        personName = Home.personName;
        personGivenName = Home.personGivenName;
        phoneNumber = Home.phoneNumber;
        amount = Home.amount;

        TextView tv1 = view.findViewById(R.id.name);
        if (personName.length() > 13) {
            tv1.setText(personGivenName);
        }
        else {
            tv1.setText(personName);
        }

        TextView tv2 = view.findViewById(R.id.number);
        tv2.setText(phoneNumber);

        TextView tv3 = view.findViewById(R.id.money_amount);
        tv3.setText(amount);

        ProfilePicture profilePicture = new ProfilePicture();
        personPhoto = profilePicture.getImage();

        CircleImageView imageView = view.findViewById(R.id.profilepic);
        Picasso.get().load(personPhoto).into(imageView);

        Button btn3 = view.findViewById(R.id.signout);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.signout:
                        signOut();
                        break;
                }
            }
        });

        Button btn1 = view.findViewById(R.id.addMoney);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddMoney.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });

        Button btn2 = view.findViewById(R.id.accountButton);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AccountFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TextView t1 = view.findViewById(R.id.add_Money);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddMoney.class));
            }
        });

        TextView t2 = view.findViewById(R.id.sendMoney);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new SearchFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TextView t3 = view.findViewById(R.id.addShortcut);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddShortcut.class));
            }
        });

        TextView t4 = view.findViewById(R.id.settings);
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new SettingsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(view.getContext(), Login.class));
            }
        });
    }

}