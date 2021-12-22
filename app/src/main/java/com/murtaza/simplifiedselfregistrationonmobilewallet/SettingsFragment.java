package com.murtaza.simplifiedselfregistrationonmobilewallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    List<Favorite> list = new ArrayList<>();
    RecyclerView recyclerView;
    FavoritesRvAdapter adapter;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        list.add(new Favorite(R.drawable.illus1));
        list.add(new Favorite(R.drawable.illus2));
        list.add(new Favorite(R.drawable.illus3));

        recyclerView = view.findViewById(R.id.favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new FavoritesRvAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);

        ImageView imageView = view.findViewById(R.id.back_arrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Home.class);
                startActivity(intent);
            }
        });

        TextView tv1 = view.findViewById(R.id.terms);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), TermsAndServices.class));
            }
        });

        TextView tv2 = view.findViewById(R.id.privacy);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PrivacyPolicy.class));
            }
        });

        TextView tv3 = view.findViewById(R.id.to_account);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AccountFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TextView tv4 = view.findViewById(R.id.support);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), Support.class));
            }
        });

        return view;
    }
}