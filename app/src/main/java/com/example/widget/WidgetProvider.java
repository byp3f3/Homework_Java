package com.example.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    private static boolean isTorchOn = false;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction() != null && intent.getAction().equals("com.example.widget.TOGGLE_FLASH")) {
            toggleFlash(context);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_view);

        views.setTextViewText(R.id.widget_button, isTorchOn ? "Выключить" : "Включить");

        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction("com.example.widget.TOGGLE_FLASH");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT); // Use FLAG_UPDATE_CURRENT to update existing PendingIntent

        views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void toggleFlash(Context context) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    cameraManager.setTorchMode(cameraId, !isTorchOn);
                    isTorchOn = !isTorchOn;
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}