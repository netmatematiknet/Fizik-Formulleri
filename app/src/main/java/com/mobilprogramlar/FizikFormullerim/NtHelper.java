package com.mobilprogramlar.FizikFormullerim;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.SystemBarStyle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class NtHelper {

    /** Tablet dikey: 2 sütun; tablet yatay: 3 sütun; telefon: 1. */
    public static int listColumnCount(@NonNull Context context) {
        int sw = context.getResources().getConfiguration().smallestScreenWidthDp;
        if (sw < 600) {
            return 1;
        }
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
    }















































    // Aktivite başlatma metodunu genel ve modüler hale getirmek için kullanılır.
    // Context'in bir Activity olup olmadığını kontrol eder ve eğer değilse FLAG_ACTIVITY_NEW_TASK bayrağını ekler.
    // KULLANIMI: ActivityHelper.startActivity(this, Ayarlar.class)
    public static void startActivity(Context context, Class<?> targetActivityClass) {
        Intent intent = new Intent(context, targetActivityClass);
        // Context'in bir Activity olup olmadığını kontrol ediyoruz.
        // Eğer Activity değilse FLAG_ACTIVITY_NEW_TASK bayrağını ekliyoruz.
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    // Metni paylaşmak için kullanılır.
    // Intent oluşturur ve paylaşım ekranını başlatır.
    // KULLANIMI: ActivityHelper.shareText(this)
    public static void shareText(Context context) {
        String title = context.getResources().getString(com.mobilprogramlar.FizikFormullerim.R.string.paylas_baslik);
        String message = context.getResources().getString(com.mobilprogramlar.FizikFormullerim.R.string.paylas_mesaj) + context.getResources().getString(com.mobilprogramlar.FizikFormullerim.R.string.paylas_link);
        String buttonText = context.getResources().getString(com.mobilprogramlar.FizikFormullerim.R.string.paylas_button_mesaj);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        // Sharing Intent için FLAG_ACTIVITY_NEW_TASK kontrolü ekliyoruz.
        if (!(context instanceof Activity)) {
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(Intent.createChooser(sharingIntent, buttonText));
    }


    /**
     * setContentView öncesi çağrılır. Android 15+ edge-to-edge; şeffaf sistem çubukları
     * (Play: enableEdgeToEdge — setStatusBarColor / setNavigationBarColor kullanılmaz).
     */
    public static void enableEdgeToEdge(@NonNull ComponentActivity activity) {
        EdgeToEdge.enable(
                activity,
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT));
    }

    /** setContentView sonrası: sistem çubuğu + çentik için padding. */
    public static void applySystemBarInsets(@NonNull Activity activity) {
        View root = activity.findViewById(android.R.id.content);
        if (root == null) {
            return;
        }
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    // Geri tuşuna basıldığında belirli bir aktiviteye dönmek için kullanılır.
    // OnBackPressedCallback kullanarak geri tuşu davranışını özelleştirir.
    // KULLANIMI: ActivityHelper.setOnBackPressed(this, MainActivity.class);
    public static void setOnBackPressed(AppCompatActivity activity, Class<?> targetActivity) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(activity.getApplicationContext(), targetActivity);
                // Intent'e FLAG_ACTIVITY_NEW_TASK ekleniyor.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                NavUtils.navigateUpTo(activity, intent);
            }
        };
        activity.getOnBackPressedDispatcher().addCallback(activity, callback);
    }


    // Ortak animasyonları uygulamak için bir metot ekliyoruz.
    // Bu metot, bir geçiş animasyonu uygulamak için kullanılabilir.
    // Aktivite geçişlerinde animasyonlar eklemek için kullanışlıdır.

    // KULLANIMI: NtHelper.applyTransitionAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);
    public static void applyTransitionAnimation(Activity activity, int enterAnim, int exitAnim) {
        activity.overridePendingTransition(enterAnim, exitAnim);
    }



    // Geri tuşuna basıldığında uygulamadan çıkmak için bir metot ekliyoruz.
    // Bu metot, kullanıcı geri tuşuna bastığında uygulamayı kapatmak için kullanılabilir.
    // Uygulamanın kapanması gerektiği durumlar için pratik bir çözümdür.

    // KULLANIMI: NtHelper.exitAppOnBackPressed(this);
    public static void exitAppOnBackPressed(AppCompatActivity activity) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activity.finishAffinity();
            }
        };
        activity.getOnBackPressedDispatcher().addCallback(activity, callback);
    }




    // Ortak diyalogları yönetmek için bir metot ekliyoruz

    //  KULLANIMI:
    //  // Bir onay diyalogu göstermek için kullanılır.

    //          DialogInterface.OnClickListener positiveListener = (dialog, which) -> {
    //                  // Pozitif butona tıklandığında yapılacaklar
    //          };
    //          DialogInterface.OnClickListener negativeListener = (dialog, which) -> {
    //                  // Negatif butona tıklandığında yapılacaklar
    //          };
    //          NtHelper.showConfirmationDialog(this, "Title", "Message", positiveListener, negativeListener);
    //  .
    public static void showConfirmationDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, negativeListener)
                .show();
    }





    //  // Kullanıcıdan girdi almak için bir diyalog göstermek için kullanılır.
    //  KULLANIMI:
    //      NtHelper.showInputDialog(this, "Title", "Message", new NtHelper.InputCallback() {
    //          @Override
    //          public void onInput(String input) {
    //                  // Kullanıcı girdisini işleyin
    //          }
    //      });

    // KULLANIMI: NtHelper.showInputDialog(this, "Title", "Message", new NtHelper.InputCallback() { ... });
    public static void showInputDialog(Context context, String title, String message, final com.mobilprogramlar.FizikFormullerim.NtHelper.InputCallback callback) {
        final EditText input = new EditText(context);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setView(input)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    String value = input.getText().toString();
                    callback.onInput(value);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, whichButton) -> dialog.dismiss())
                .show();
    }
    // Kullanıcıdan alınan girdiyi işlemek için bir arayüz.
    public interface InputCallback {
        void onInput(String input);
    }




























    // Genel amaçlı loglama metotları - Bilgi logu - Hata logu - Debug logu
    // KULLANIMI:
    //          NtHelper.logInfo("TAG", "Bilgilendirme mesajı");
    //          NtHelper.logError("TAG", "Hata mesajı", new Throwable());
    //          NtHelper.logDebug("TAG", "Hata mesajı");

    public static void logInfo(String tag, String message) {
        Log.i(tag, message);
    }
    public static void logError(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }
    public static void logDebug(String tag, String message) {
        Log.d(tag, message);
    }



    // KULLANIMI:
    //      AndroidManifest.xml dosyasına EKLE :
    //      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    //      if (NtHelper.isNetworkAvailable(this)) {
    //              // İnternet bağlantısı mevcut
    //      } else {
    //              // İnternet bağlantısı yok
    //      }
    // İnternet bağlantısını kontrol etme metodu
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }








    // Tarih formatlama metotları
    // KULLANIMI:
    //      String formattedDate = NtHelper.formatDate(System.currentTimeMillis(), "dd/MM/yyyy");
    public static String formatDate(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }



    // Saat formatlama metotları
    // KULLANIMI:
    //      String formattedTime = NtHelper.formatTime(System.currentTimeMillis(), "HH:mm:ss");
    public static String formatTime(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }





    // Toast UZUN mesaj gösterme metodu
    // KULLANIMI:
    //      NtHelper.showLongToast(this, "Uzun mesaj");
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Toast KISA mesaj gösterme metodu
    // KULLANIMI:
    //      NtHelper.showToast(this, "Kısa mesaj");
    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }








    // View görünürlüğünü değiştirme metodu
    // KULLANIMI:
    //      NtHelper.setViewVisibility(myView, true);  // Görünür yap
    //      NtHelper.setViewVisibility(myView, false); // Gizle
    public static void setViewVisibility(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }















    // İnternet durumu kontrolü, internet çeşidi kontrolü, internet izin kontrolü
    // KULLANIMI:
    //      NtHelper.NetworkStatus networkStatus = NtHelper.checkNetworkStatus(this);
    //          Log.d("MainActivity", "İnternet bağlantısı: " + networkStatus.isConnected);
    //          Log.d("MainActivity", "İnternet türü: " + networkStatus.networkType);
    //          Log.d("MainActivity", "İzin durumu: " + networkStatus.hasPermission);

    //    if (!networkStatus.hasPermission) {
    // Kullanıcıya izni alması gerektiğini bildir
    //      } else if (networkStatus.isConnected) {
    // İnternet bağlantısı var, gerekli işlemleri yap
    //      } else {
    // İnternet bağlantısı yok, kullanıcıyı bilgilendir
    //  }

    // Bu metot, internet bağlantısının olup olmadığını, bağlantı türünü ve gerekli iznin olup olmadığını kontrol eder.
    public static com.mobilprogramlar.FizikFormullerim.NtHelper.NetworkStatus checkNetworkStatus(Context context) {
        // Önce gerekli iznin olup olmadığını kontrol ediyoruz
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
        boolean hasPermission = permission == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            Log.e("NtHelper", "Gerekli izin yok: android.permission.ACCESS_NETWORK_STATE");
            return new com.mobilprogramlar.FizikFormullerim.NtHelper.NetworkStatus(false, "No Permission", false);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Log.e("NtHelper", "ConnectivityManager erişilemedi.");
            return new com.mobilprogramlar.FizikFormullerim.NtHelper.NetworkStatus(false, "No Connectivity Manager", true);
        }

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            Log.e("NtHelper", "Aktif ağ yok.");
            return new com.mobilprogramlar.FizikFormullerim.NtHelper.NetworkStatus(false, "No Network", true);
        }

        //NetworkCapabilities sınıfı kullanılarak internet bağlantısının türü belirlenir (WiFi, Cellular, Ethernet, vs.).
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities == null || !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            Log.e("NtHelper", "İnternet bağlantısı yok.");
            return new com.mobilprogramlar.FizikFormullerim.NtHelper.NetworkStatus(false, "No Internet", true);
        }
        String networkType;
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            networkType = "WiFi";
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            networkType = "Cellular";
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            networkType = "Ethernet";
        } else {
            networkType = "Other";
        }

        return new com.mobilprogramlar.FizikFormullerim.NtHelper.NetworkStatus(true, networkType, true);
    }

    // İnternet durumu sonuçlarını döndüren sınıf,  NetworkStatus sınıfı, bu bilgileri paketleyip döner.
    public static class NetworkStatus {
        public final boolean isConnected;
        public final String networkType;
        public final boolean hasPermission;

        public NetworkStatus(boolean isConnected, String networkType, boolean hasPermission) {
            this.isConnected = isConnected;
            this.networkType = networkType;
            this.hasPermission = hasPermission;
        }
    }





    //   Bir butona tıklama olayında web sayfası açmak
    //   KULLANIMI:
    //        findViewById(R.id.button_open_web).setOnClickListener(v -> {
    //            NtHelper.openWebPage(this, "https://www.example.com");
    //        });
    public static void openWebPage(Context context, String url) {
        if (url == null) {
            Log.e("NtHelper", "Geçersiz URL: null");
            return;
        }
        String t = url.trim();
        if (!t.startsWith("http://") && !t.startsWith("https://")) {
            Log.e("NtHelper", "Geçersiz URL (yalnızca http/https): " + url);
            return;
        }
        // Intent oluşturun ve varsayılan tarayıcıda açılacak şekilde ayarlayın
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(t));
        // URL'yi açmayı dene
        try {
            context.startActivity(webIntent);
        } catch (ActivityNotFoundException e) {
            //ActivityNotFoundException: Bu istisna, cihazda uygun bir tarayıcı bulunamadığında atılır. Bu durumda hata loglanır.
            Log.e("NtHelper", "Web tarayıcı bulunamadı: " + e.getMessage());
        }
    }






    // İnternet bağlantısının olmadığına dair bir Toast mesajı gösterebilir ve daha sonra tekrar denemesi için bir seçenek.
    // Ağ isteği geri dönüş mantığı
    //   KULLANIMI:
    //      handleNetworkRequest(this, () -> {
    //          // Ağ isteği burada yapılır
    //      });
    public static void handleNetworkRequest(Context context, Runnable request) {
        com.mobilprogramlar.FizikFormullerim.NtHelper.NetworkStatus networkStatus = checkNetworkStatus(context);
        if (networkStatus.isConnected) {
            request.run();
        } else {
            showToast(context, "İnternet bağlantısı yok. Lütfen daha sonra tekrar deneyin.");
        }
    }








    // Bildirimler ve gerçek zamanlı veri güncellemeleri için FCM kullanımı ekleyebilirsiniz.
    // UYGULAM İÇİi bildirimler oluşturmak ve göndermek için aşağıdaki metot kullanılabilir:
    //   KULLANIMI:
    //      // Bir butona tıklama olayında bildirim göndermek
    //      findViewById(R.id.button_send_notification).setOnClickListener(v -> {
    //         NtHelper.sendNotification(this, "Başlık", "Mesaj içeriği", R.mipmap.ic_launcher);
    //      });
    //
    // NotificationManager, Android'in bildirim sistemini yöneten bir sınıftır.
    // Bu sınıfı kullanarak bildirim gönderebilir, güncelleyebilir veya iptal edebilirsiniz.
    public static void sendNotification(Context context, String title, String message, int iconResId) {
        // Bildirim oluşturma ve gönderme mantığı burada
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //NotificationCompat.Builder, Android'in daha eski ve yeni sürümleriyle uyumlu bildirimler oluşturmanızı sağlar.
        // Bu sınıf, bildirimlerin içeriğini ve özelliklerini belirlemek için kullanılır.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                //setSmallIcon metodu, bildirimde gösterilecek küçük simgeyi belirler. Bu simge, genellikle uygulamanın logosu veya
                // bildirimle ilgili bir simge olur. R.mipmap.ic_launcher, projenin res/mipmap klasöründe bulunan bir simgedir.
                .setSmallIcon(iconResId)
                //ic_launcher: Uygulamanın ana simgesi. Genellikle 48x48 dp boyutunda olur.
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        //notify metodu, bildirimi gösterir. İlk parametre, bildirimin kimliği olup,
        // bu kimlik bildirimi güncellemek veya iptal etmek için kullanılır.
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }



    //Uygulama kullanımını ve kullanıcı davranışlarını izlemek için analitik araçlarını entegre edebilirsiniz.
    //   KULLANIMI:
    //Bundle params = new Bundle();
    //params.putString("button_name", "example_button");
    //NtHelper.logEvent(this, "button_click", params);
    //public static void logEvent(Context context, String eventName, Bundle parameters) {
    // Firebase Analytics ile olay loglama
    //FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    //firebaseAnalytics.logEvent(eventName, parameters);
    //}






    //Kullanıcı tercihlerini veya diğer kalıcı verileri saklamak için SharedPreferences kullanımı ekleyebilirsiniz.
    //   KULLANIMI:
    //      NtHelper.saveToSharedPreferences(this, "key", "value");
    //      String value = NtHelper.getFromSharedPreferences(this, "key", "default");
    public static void saveToSharedPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getFromSharedPreferences(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }








    // Metni kopyalama metodu
    // KULLANIMI: NtHelper.copyToClipboard(this, "Kopyalanacak metin");
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        showToast(context, "Metin kopyalandı");
    }

    // Metni yapıştırma metodu
    // KULLANIMI: String metin = NtHelper.pasteFromClipboard(this);
    public static String pasteFromClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip() && Objects.requireNonNull(clipboard.getPrimaryClipDescription()).hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item item = Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0);
            return item.getText().toString();
        }
        return "";
    }







    // Cihaz bilgilerini alma metodu
    // KULLANIMI: String cihazBilgileri = NtHelper.getDeviceInfo();
    public static String getDeviceInfo() {
        return "Marka: " + Build.BRAND + ", Model: " + Build.MODEL + ", OS: " + Build.VERSION.RELEASE;
    }




    // Uygulama sürüm bilgilerini alma metodu
    // KULLANIMI:
    //      String appVersion = NtHelper.getAppVersion(this);
    //      Log.d("AppVersion", appVersion);.
    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            long versionCode = packageInfo.getLongVersionCode();
            String versionName = packageInfo.versionName;
            return "Versiyon Adı: " + versionName + ", Versiyon Kodu: " + versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            logErrorDetail("NtHelper", "Uygulama sürümü alınamadı", e);
            return "Versiyon bilgisi alınamadı";
        }
    }
    public static void logErrorDetail(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }







    // Google Play geliştirici sayfasını açan metot
    // KULLANIMI:
    //          NtHelper.openDeveloperPage(context, "MobilUygulamalar.com");
    public static void openDeveloperPage(Context context, String developerName) {
        try {
            // Play Store uygulamasında açmayı dener
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + developerName));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Eğer Play Store uygulaması yoksa, tarayıcıda açar
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + developerName));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    // Bu metodu kullanmak için, bir butona tıklama olayını şu şekilde ayarlayabilirsiniz:
    //      Button openDeveloperPageButton = findViewById(R.id.btn_open_developer_page);
    //          openDeveloperPageButton.setOnClickListener(v -> {
    //              NtHelper.openDeveloperPage(this, "MobilUygulamalar.com");
    //          });











    //KULLANIMI
    //new ToastMesajOzel().gosterTOAST(context.getApplicationContext(),"Sorunun Tamamı",nuri.getSoru(),15000);
    public void gosterTOAST(Context context, String Baslik, String msg,int millisec)
    {
        Handler handler = null;
        final Toast[] toast_sureli = new Toast[1];
        for(int i = 0; i < millisec; i+=8000) {
            LayoutInflater inflater = LayoutInflater.from(context);

            //View layout = inflater.inflate(R.layout.toast_ekrani, null );
            // Doğru kullanım
            View layout = inflater.inflate(R.layout.nthelper_toast_ekrani, null, false);
            TextView baslikText = layout.findViewById(R.id.toast_baslik);
            TextView textMesaj = layout.findViewById(R.id.toast_mesaj);
            baslikText.setText(Baslik);
            textMesaj.setText(msg);
            toast_sureli[0] = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast_sureli[0].setGravity(Gravity.BOTTOM, 0, 0);
            toast_sureli[0].setDuration(Toast.LENGTH_LONG);
            toast_sureli[0].setView(layout);  //setView yani özel toast kullanımdan kaldırıldı. Bunun yerine SnackBar kullan
            toast_sureli[0].show();
            if(handler == null) {
                handler = new Handler();
                handler.postDelayed(() -> toast_sureli[0].cancel(), millisec);
            }
        }
    }























    public void sureliTOAST(Context context, String msg, int millisec) {
        Handler handler = null;
        final Toast[] toast_sureli = new Toast[1];
        for(int i = 0; i < millisec; i+=8000) {
            toast_sureli[0] = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast_sureli[0].show();
            if(handler == null) {
                handler = new Handler();
                handler.postDelayed(() -> toast_sureli[0].cancel(), millisec);
            }
        }
    }









    //KULLANIMI
    //new SnackBarOzel().ozelSnackBar(context.getApplicationContext(),view, nuri.getSoru(),7000);
    public void ozelSnackBar(Context context, View v, String msg, int millisec) {
        Snackbar snackbar_alt = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        View snackBarView_ALT = snackbar_alt.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackBarView_ALT.getLayoutParams();
        params.gravity = Gravity.BOTTOM;

        snackBarView_ALT.setLayoutParams(params);
        snackBarView_ALT.setBackgroundColor(ContextCompat.getColor(context, R.color.blueLight));   //snackbar_alt BACKGROUN COLOR

        //TextView mainTextView =  (snackBarView_ALT).findViewById(R.id.snackbar_text);
        TextView mainTextView = (snackBarView_ALT).findViewById(com.google.android.material.R.id.snackbar_text);
        mainTextView.setMaxLines(10);       //10 satır maksimum
        mainTextView.setTextColor(Color.WHITE);
        mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);  //Mesajı ORTALAR
        mainTextView.setTextSize(14);
        mainTextView.setPadding(0,0,0,0);

        snackbar_alt.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);     //Animasyon veriyor
        snackbar_alt.setDuration(millisec);     //snackbar_alt 5 saniye bekliyor

        //SpannableStringBuilder builder = new SpannableStringBuilder();
        //builder.append("My message ").append(" ");
        //builder.setSpan(new ImageSpan(context, R.drawable.ic_paylas), builder.length() - 1, builder.length(), 0);

        snackbar_alt.setActionTextColor(Color.BLUE);
        //snackbar_alt buttonuna basıldığında
        snackbar_alt.setAction("Tamam", view -> {
        });
        snackbar_alt.show();

        /*
                Snackbar snackbar;
                snackbar = Snackbar.make(view, cevap_Getir(gosterilen_soruID), Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
                snackBarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_background));
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_text));
                snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.uyari_textColor_1));
                snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_background));
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(3000);
                snackbar.setAction("Tamam", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                snackbar.show();
                */
    }




















}