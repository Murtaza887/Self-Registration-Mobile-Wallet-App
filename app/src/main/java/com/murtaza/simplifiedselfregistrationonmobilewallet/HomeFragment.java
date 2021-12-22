package com.murtaza.simplifiedselfregistrationonmobilewallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    List<Favorite> list = new ArrayList<>();
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    FavoritesRvAdapter adapter, adapter2, adapter3;
    Context applicationContext;
    String personName, personGivenName, personFamilyName, personEmail, personId, phoneNumber, amount;
    Uri personPhoto;
    GoogleSignInClient mGoogleSignInClient;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        list.add(new Favorite(R.drawable.illus1));
        list.add(new Favorite(R.drawable.illus2));
        list.add(new Favorite(R.drawable.illus3));

        recyclerView = view.findViewById(R.id.favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new FavoritesRvAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

        applicationContext = Home.getContextOfApplication();
        personName = Home.personName;
        personGivenName = Home.personGivenName;
        phoneNumber = Home.phoneNumber;
        amount = Home.amount;

        if (personName != null) {
            TextView tv1 = view.findViewById(R.id.name);
            if (personName.length() > 13) {
                tv1.setText(personGivenName);
            } else {
                tv1.setText(personName);
            }
        }

        if (phoneNumber != null) {
            TextView tv2 = view.findViewById(R.id.number);
            tv2.setText(phoneNumber);
        }

        if (amount != null) {
            TextView tv3 = view.findViewById(R.id.money_amount);
            tv3.setText(amount);
        }

        ProfilePicture profilePicture = new ProfilePicture();
        personPhoto = profilePicture.getImage();

        if (personPhoto != null) {
            CircleImageView imageView = view.findViewById(R.id.profilepic);
            Picasso.get().load(personPhoto).into(imageView);
        }

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

        ImageView img1 = view.findViewById(R.id.send_Money);
        ImageView img2 = view.findViewById(R.id.settings);
        ImageView img3 = view.findViewById(R.id.add_Money);
        ImageView img4 = view.findViewById(R.id.info);
        ImageView img5 = view.findViewById(R.id.shortcut);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Search Fragment
                Fragment newFragment = new SearchFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Settings Fragment
                Fragment newFragment = new SettingsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddMoney.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), TermsAndServices.class));
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddShortcut.class));
            }
        });
        return view;
    }
}