package com.app.photomanager;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;

import android.widget.ImageView;

import com.bumptech.glide.Glide;



public class imageDisplay extends AppCompatActivity {

        private ImageView imageView;
        String imgpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        imgpath=getIntent().getStringExtra("strPath");
        imageView=findViewById(R.id.ImageView2);

            Glide.with(this).load(imgpath).into(imageView);
    }
}