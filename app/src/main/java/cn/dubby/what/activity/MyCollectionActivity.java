package cn.dubby.what.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.dubby.what.R;

public class MyCollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.zoomin, R.animator.zoomout);
    }
}
