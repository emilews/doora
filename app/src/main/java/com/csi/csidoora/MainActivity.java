package com.csi.csidoora;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;

import org.json.JSONObject;

import java.io.IOException;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.loginButton);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},1);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LogIn();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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
    public void LogIn() throws IOException, InterruptedException {
        final CONSTANTS constants = CONSTANTS.getInstance();
        constants.sessionCode(this);

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
        final VolleyService vs =VolleyService.getInstance();
        boolean loggedin = false;
        vs.LogIn(this, email, pass);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (vs.getLoggedIn()){
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
        }, 500);

    }
    public void goHome(){
        Intent intent = new Intent(MainActivity.this, Home.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}
