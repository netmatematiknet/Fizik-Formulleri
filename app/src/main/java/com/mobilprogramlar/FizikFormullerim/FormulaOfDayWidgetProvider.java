package com.mobilprogramlar.FizikFormullerim;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

/**
 * Ana ekran widget: günün formül konusu; dokununca formül detayı.
 */
public class FormulaOfDayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // no-op
    }

    public static void refreshAll(@NonNull Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, FormulaOfDayWidgetProvider.class));
        if (ids == null || ids.length == 0) {
            return;
        }
        Intent intent = new Intent(context, FormulaOfDayWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    private static void updateWidget(@NonNull Context context,
                                     @NonNull AppWidgetManager manager,
                                     int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_formula_of_day);
        TopicCatalog.Topic topic = FormulaOfTheDayHelper.today(context);
        String title = topic != null
                ? topic.title
                : context.getString(R.string.manifest_activity_splash_app_name);
        views.setTextViewText(R.id.widget_title, title);

        Intent open;
        if (topic != null) {
            open = new Intent(context, Formula_Detail.class);
            open.putExtra("formula_category", context.getString(R.string.toolbar_baslik_1));
            open.putExtra("formula_title", topic.title);
        } else {
            open = new Intent(context, MainActivity.class);
        }
        open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending = PendingIntent.getActivity(
                context,
                appWidgetId,
                open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_root, pending);

        manager.updateAppWidget(appWidgetId, views);
    }
}
