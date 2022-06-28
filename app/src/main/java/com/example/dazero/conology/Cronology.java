package com.example.dazero.conology;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageSwitcher;

import com.example.dazero.R;
import com.example.dazero.db.ResultDao;
import com.example.dazero.services.ResultService;
import com.google.android.material.card.MaterialCardView;

public class Cronology extends AppCompatActivity {

    MaterialCardView uid;
    int click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronology);

        View whatIsUID=findViewById(R.id.whatIsUID);
        View line= findViewById(R.id.line);
        View copyUID= findViewById(R.id.copyUID);
        ImageSwitcher arrowUID = new ImageSwitcher(getApplicationContext());
        //....
        uid = findViewById(R.id.uid);
        uid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(uid);


                if (click % 2 == 0) {
                    whatIsUID.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    whatIsUID.setVisibility(View.VISIBLE);

                                    line.setVisibility(View.VISIBLE);

                                    copyUID.setVisibility(View.VISIBLE);
                                    super.onAnimationEnd(animation);
                                }
                            });
                    arrowUID.setImageResource(R.drawable.avatar);
                } else {
                    whatIsUID.animate()
                            .alpha(0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    whatIsUID.setVisibility(View.GONE);
                                    line.setVisibility(View.GONE);
                                    copyUID.setVisibility(View.GONE);
                                    super.onAnimationEnd(animation);
                                }
                            });
                    arrowUID.setImageResource(R.drawable.about_us);
                }
                click++;
            }
        });

        //....
    }
}