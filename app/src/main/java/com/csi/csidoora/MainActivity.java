package com.csi.csidoora;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
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
        final CONSTANTS constants = new CONSTANTS(this);

        EditText e = findViewById(R.id.emailInput);
        EditText p = findViewById(R.id.passwordInput);
        Button b = findViewById(R.id.loginButton);
        final String email = e.getText().toString();
        final String pass = p.getText().toString();
        e.setVisibility(View.INVISIBLE);
        p.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.VISIBLE);
        Sprite sprite = new CubeGrid();
        progressBar.setIndeterminateDrawable(sprite);

        final Context ctx = this;
        RequestQueue r = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, constants.getLOGIN_URL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("Success")){
                            Toast t = Toast.makeText(ctx, "Success!", Toast.LENGTH_LONG);
                            t.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", pass);
                params.put("device", constants.getDEVICE_CODE());
                return params;
            }
        };
        r.add(sr);
        r.start();

    }
}
