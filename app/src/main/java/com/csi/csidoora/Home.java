package com.csi.csidoora;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;


public class Home extends AppCompatActivity {
    static CONSTANTS constants = CONSTANTS.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, BackgroundService.class));
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });
        LoadCode();

    }

    public void LoadCode()  {
        constants.doorCode(this);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.loadCode);
        progressBar.setVisibility(View.VISIBLE);
        Sprite sprite = new ThreeBounce();
        progressBar.setIndeterminateDrawable(sprite);
        final TextView code = findViewById(R.id.codeText);
        progressBar.setVisibility(View.INVISIBLE);
        code.setText(constants.getCODE());

    }
    private void refreshData() {
        VolleyService vs = VolleyService.getInstance();
        constants.sessionCode(this);
        vs.getCode(this);
        final TextView c = findViewById(R.id.codeText);
        Handler f = new Handler();
        f.postDelayed(new Runnable() {
            @Override
            public void run() {
                c.setText(constants.getCODE());
            }
        }, 300);
    }
    public  void updateViews(){
        new UIUpdater().execute();
    }
    class UIUpdater extends android.os.AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            TextView c = (TextView) findViewById(R.id.codeText);
            c.setText(constants.getCODE());
            return null;
        }
    }

}

