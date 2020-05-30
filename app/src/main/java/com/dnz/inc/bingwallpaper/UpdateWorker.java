package com.dnz.inc.bingwallpaper;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateWorker extends Worker {
    public static final String WORKER_TAG = "update_worker";

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Intent intent = new Intent("com.dnz.inc.bingwallpaper.UPDATE_SERVICE");
        // TODO: 5/30/20 remove after test
        intent.putExtra("from", UpdateWorker.class.getName());
        intent.setPackage(getApplicationContext().getPackageName());

        getApplicationContext().startService(intent);
        return ListenableWorker.Result.success();
    }
}
