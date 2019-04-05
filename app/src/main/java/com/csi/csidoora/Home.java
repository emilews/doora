package com.csi.csidoora;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class Home extends AppCompatActivity {
    CONSTANTS constants = CONSTANTS.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        TextView code = findViewById(R.id.codeText);
        progressBar.setVisibility(View.INVISIBLE);
        code.setText(constants.getCODE());

    }
    private void refreshData() {
        VolleyService vs = VolleyService.getInstance();
        vs.getCode(this);
        LoadCode();
    }

}
