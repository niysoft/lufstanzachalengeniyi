package com.lufstanza.app.utils;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ParentActivty extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.activityResumed();
    }

    @Override
    protected void onPause() {
        //AppController.storage.store("LAST_ACITVE_TIME", System.currentTimeMillis());
        super.onPause();
        AppController.activityPaused();
    }
}