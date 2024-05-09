package com.example.campusnavigator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailsNoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_notice);


        Intent intent = getIntent();
        String heading = intent.getStringExtra("heading");
        String date = intent.getStringExtra("date");
        String content = intent.getStringExtra("content");
        String imageUri = intent.getStringExtra("imageUri");
        String username = intent.getStringExtra("username");


        TextView welcomeTextView = findViewById(R.id.welcome_text_view);
        welcomeTextView.setText(username);


        TextView headingTextView = findViewById(R.id.details_notice_heading);
        TextView dateTextView = findViewById(R.id.details_notice_date);
        TextView contentTextView = findViewById(R.id.details_notice_content);
        ImageView imageView = findViewById(R.id.notice_details_imageView);
        TextView closeTextView = findViewById(R.id.notice_details_close);
        ProgressBar imageProgressBar = findViewById(R.id.notice_image_progressBar);

        headingTextView.setText(heading);
        dateTextView.setText(date);
        contentTextView.setText(content);

        Picasso.get()
                .load(imageUri)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                        imageProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
