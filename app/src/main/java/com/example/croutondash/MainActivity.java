package com.example.croutondash;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView digitalClock;
    private TextView currentDate;
    private TextView messageGreet;
    private TextView currentTemperature;
    private TextView currentSummary;
    private ImageView weatherIcon;
    private Handler handlerS;
    private Handler handlerH;
    private ImageButton settingsButton;
    private Animation slideLeft;
    private Animation slideRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        digitalClock = findViewById(R.id.digitalClock);
        currentDate = findViewById(R.id.currentDate);
        messageGreet = findViewById(R.id.messageGreet);
        currentTemperature = findViewById(R.id.currentTemperature);
        currentSummary = findViewById(R.id.currentSummary);
        weatherIcon = findViewById(R.id.weatherIcon);
        settingsButton = findViewById(R.id.settingsButton);

        HandlerThread handlerThreadS = new HandlerThread("handlerThreadS");
        handlerThreadS.start();
        handlerS = new Handler(handlerThreadS.getLooper());

        HandlerThread handlerThreadH = new HandlerThread("handlerThreadH");
        handlerThreadH.start();
        handlerH = new Handler(handlerThreadH.getLooper());

        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);

        updateSecond();
        updateWeather();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            toggleButtonVisibility();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void toggleButtonVisibility() {
        if (settingsButton.getVisibility() == View.VISIBLE) {
            settingsButton.startAnimation(slideRight);
            settingsButton.setVisibility(View.GONE);
        } else {
            settingsButton.startAnimation(slideLeft);
            settingsButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerS.removeCallbacksAndMessages(null);
        handlerH.removeCallbacksAndMessages(null);
        handlerS.getLooper().quit();
        handlerH.getLooper().quit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hide the navigation bar and make the activity fullscreen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void updateSecond() {
        handlerS.postDelayed(() -> {
            runOnUiThread(() -> {
                setTime();
                setDate();
                setMessage();
            });
            updateSecond();
        }, 1000); // every 1 second
    }

    private void updateWeather() {
        runOnUiThread(() -> {
            currentSummary.setText("Fetching Weather...");
            setWeather();
        });

        handlerH.postDelayed(() -> {
            runOnUiThread(this::setWeather);
            updateWeather();
        }, 3600000); // every 1 hour
    }

    private void setTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm", Locale.getDefault());
        dateFormat.setCalendar(Calendar.getInstance());
        String currentTime = dateFormat.format(new Date());
        digitalClock.setText(currentTime);
    }

    private void setDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
        String now = dateFormat.format(new Date());
        currentDate.setText(now);
    }

    private void setMessage() {
        String name = "Ricky";
        int h = new Date().getHours();
        String partOfDay = "Evening";

        if (h >= 0 && h < 12) {
            partOfDay = "Morning";
        } else if (h >= 12 && h < 18) {
            partOfDay = "Afternoon";
        }

        String msg = "Good " + partOfDay + ", " + name;
        messageGreet.setText(msg);
    }

    private void setWeather() {
        weatherAPI currentWeather = new weatherAPI();
        currentWeather.fetchWeather(currentTemperature, currentSummary, weatherIcon);
    }
    
}