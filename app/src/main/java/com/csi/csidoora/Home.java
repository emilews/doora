package com.csi.csidoora;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import java.io.IOException;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            LoadCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadCode() throws IOException {
        CONSTANTS constants = new CONSTANTS(this);
        constants.doorCode();
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.loadCode);
        progressBar.setVisibility(View.VISIBLE);
        Sprite sprite = new ThreeBounce();
        progressBar.setIndeterminateDrawable(sprite);
        VolleyService vs = VolleyService.getInstance();
        vs.getCode(this);
        TextView code = findViewById(R.id.codeText);
        progressBar.setVisibility(View.INVISIBLE);
        code.setText(constants.getCODE());

    }

}
