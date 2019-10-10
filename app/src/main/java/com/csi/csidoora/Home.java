package com.csi.csidoora;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;


public class Home extends AppCompatActivity {
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
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.loadCode);
        progressBar.setVisibility(View.VISIBLE);
        Sprite sprite = new ThreeBounce();
        progressBar.setIndeterminateDrawable(sprite);
        TextView code = findViewById(R.id.codeText);
        progressBar.setVisibility(View.INVISIBLE);
        code.setText(BackgroundService.getCODE());

    }
    private void refreshData() {
        BackgroundService.sessionIDCode(this);
        BackgroundService.refresh(this);
        TextView c = findViewById(R.id.codeText);
        c.setText(BackgroundService.getCODE());
    }

}

