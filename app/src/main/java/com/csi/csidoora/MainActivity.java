package com.csi.csidoora;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.Pulse;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create a notification channel for the permanent we use
        createNotificationChannel();
        //SessionCode
        BackgroundService.sessionIDCode(MainActivity.this);
        //Setting all the widgets for later use
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        final EditText e = findViewById(R.id.emailInput);
        final EditText p = findViewById(R.id.passwordInput);
        final Button b = findViewById(R.id.loginButton);
        final ImageView csi = findViewById(R.id.csiLogo);
        final TextView welcome = findViewById(R.id.welcomeText);
        final TextView userNameText = findViewById(R.id.userNameText);
        userNameText.setAlpha(0f);
        welcome.setAlpha(0f);
        p.setVisibility(View.INVISIBLE);
        p.setAlpha(0f);
        Sprite sprite = new Pulse();
        progressBar.setIndeterminateDrawable(sprite);
        progressBar.setVisibility(View.INVISIBLE);
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},1);
        BackgroundService.wasLogged(this);
        if (BackgroundService.getLoggedIn()){
            startActivity(new Intent(this, Home.class));
            overridePendingTransition(3,3);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!e.getText().toString().isEmpty()){
                        LogIn(progressBar, e, csi, b, welcome, userNameText, b, p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void LogIn(final ProgressBar progressBar, final EditText e, final ImageView csi, final
                      Button b, final TextView welcome, final TextView userNameText, final Button loginFirst,
                      final EditText p){
        BackgroundService.sessionIDCode(this);

        final String email = e.getText().toString();

        e.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(null);
        e.setVisibility(View.INVISIBLE);
        b.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(null);
        b.setVisibility(View.INVISIBLE);
        csi.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(null);
        csi.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        BackgroundService.LogInFirstPass(this, email);


        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BackgroundService.getUserExists()){
                    SecondPass(welcome, progressBar, userNameText, loginFirst, e.getText().toString(), p);
                }else{
                    progressBar.setVisibility(View.GONE);
                    e.setVisibility(View.VISIBLE);
                    b.setVisibility(View.VISIBLE);
                    csi.setVisibility(View.VISIBLE);
                    e.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    b.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    csi.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);

                    Toast t = Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        }, 1000);

    }
    public void SecondPass(final TextView welcome, final ProgressBar progressBar,
                           final TextView userNameText, final Button loginFirst, final String email,
                           final EditText p){
        p.setVisibility(View.VISIBLE);
        userNameText.setText(BackgroundService.getUserName());
        progressBar.setVisibility(View.GONE);
        welcome.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
        userNameText.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
        loginFirst.setVisibility(View.VISIBLE);
        loginFirst.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
        p.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);


        loginFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!p.getText().toString().isEmpty()){
                        final String pswd = p.getText().toString();
                        p.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(null);
                        loginFirst.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(null);
                        progressBar.setVisibility(View.VISIBLE);
                        Handler h = new Handler();

                        BackgroundService.LogInSecondPass(MainActivity.this, email, pswd);

                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (BackgroundService.getLoggedIn()){
                                    goHome();
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    welcome.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                                    userNameText.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                                    p.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                                    loginFirst.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                                    Toast t = Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                        }, 2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void goHome(){
        Intent intent = new Intent(MainActivity.this, Home.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "doora";
            String description = "A channel to send codes to user";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("csi", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
