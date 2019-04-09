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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        Button login = findViewById(R.id.loginButton);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},1);

        BackgroundService.wasLogged(this);
        if (BackgroundService.getLogged()){
            startActivity(new Intent(this, Home.class));
            overridePendingTransition(3,3);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LogIn();
                } catch (IOException e) {
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
    public void LogIn() throws IOException {
        BackgroundService.sessionCode(this);

        final EditText e = findViewById(R.id.emailInput);
        final EditText p = findViewById(R.id.passwordInput);
        final Button b = findViewById(R.id.loginButton);
        final String email = e.getText().toString();
        final String pass = p.getText().toString();
        e.setVisibility(View.INVISIBLE);
        p.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.VISIBLE);
        Sprite sprite = new CubeGrid();
        progressBar.setIndeterminateDrawable(sprite);

        final Context ctx = this;
        boolean loggedin = false;
        BackgroundService.LogIn(this, email, pass);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BackgroundService.getLoggedIn()){
                    goHome();
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    e.setVisibility(View.VISIBLE);
                    p.setVisibility(View.VISIBLE);
                    b.setVisibility(View.VISIBLE);
                    Toast t = Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        }, 1000);

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
