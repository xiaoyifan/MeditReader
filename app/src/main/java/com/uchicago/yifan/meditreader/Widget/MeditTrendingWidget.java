package com.uchicago.yifan.meditreader.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.uchicago.yifan.meditreader.Activities.CreatePost.CreateLinkPostActivity;
import com.uchicago.yifan.meditreader.Activities.CreatePost.CreateQuotePostActivity;
import com.uchicago.yifan.meditreader.Activities.CreatePost.CreateTextPostActivity;
import com.uchicago.yifan.meditreader.Activities.MainActivity;
import com.uchicago.yifan.meditreader.R;

/**
 * Implementation of App Widget functionality.
 */
public class MeditTrendingWidget extends AppWidgetProvider {

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.medit_trending_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        Intent quoteIntent = new Intent(context, CreateQuotePostActivity.class);
        PendingIntent pendingQuoteIntent = PendingIntent.getActivity(context, 0, quoteIntent, 0);
        views.setOnClickPendingIntent(R.id.quote_item, pendingQuoteIntent);

        Intent linkIntent = new Intent(context, CreateLinkPostActivity.class);
        PendingIntent pendingLinkIntent = PendingIntent.getActivity(context, 0, linkIntent, 0);
        views.setOnClickPendingIntent(R.id.link_item, pendingLinkIntent);

        Intent textIntent = new Intent(context, CreateTextPostActivity.class);
        PendingIntent pendingTextIntent = PendingIntent.getActivity(context, 0, textIntent, 0);
        views.setOnClickPendingIntent(R.id.text_item, pendingTextIntent);

        Intent imageIntent = new Intent(context, MainActivity.class);
        Uri widgetUri = Uri.parse(String.valueOf(R.id.camera_item));
        imageIntent.setData(widgetUri);
        PendingIntent pendingImageIntent = PendingIntent.getActivity(context, 0, imageIntent, 0);
        views.setOnClickPendingIntent(R.id.camera_item, pendingImageIntent);
        
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

