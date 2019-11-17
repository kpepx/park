package com.example.test;

import android.content.Intent;
import android.app.IntentService;
import android.util.Log;

public class RSSPullService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RSSPullService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        Log.d("pp","uu");
        // Do work here, based on the contents of dataString

    }
}
