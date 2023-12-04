package com.example.passapp.Fragmentos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.passapp.R;


public class F_AcercaDe extends Fragment {
    ImageView Ir_Twitter, Ir_Facebook, Ir_Instagram, Ir_Youtube;
    Button Ir_terminos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f__acerca_de, container, false);

        Ir_Twitter = view.findViewById(R.id.Ir_Twitter);
        Ir_Facebook = view.findViewById(R.id.Ir_Facebook);
        Ir_Instagram = view.findViewById(R.id.Ir_Instagram);
        Ir_Youtube = view.findViewById(R.id.Ir_Youtube);

        Ir_terminos = view.findViewById(R.id.Ir_terminos);


        Ir_Twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_twitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/Panxoty"));
                startActivity(ir_p_twitter);
            }
        });
        Ir_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_facebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
                startActivity(ir_p_facebook);
            }
        });
        Ir_Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_instagram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"));
                startActivity(ir_p_instagram);
            }
        });
        Ir_Youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
                startActivity(ir_p_youtube);
            }
        });

        Ir_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir_p_terminos = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/terminosypoliticaa/inicio"));
                startActivity(ir_p_terminos);
            }
        });
        return view;
    }
}