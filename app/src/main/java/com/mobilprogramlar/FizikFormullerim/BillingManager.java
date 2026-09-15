package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Collections;
import java.util.List;

/**
 * Google Play tek seferlik (INAPP) satın alma: reklamsız sürüm.
 * Play Console'da aynı ürün kimliğiyle yönetilen ürün gerekir ({@code premium_remove_ads_product_id}).
 */
public class BillingManager implements PurchasesUpdatedListener {

    public interface Listener {
        void onPremiumStateChanged(boolean isAdFree);

        void onBillingMessage(@NonNull String message);
    }

    private final Context appContext;
    private final String productId;
    private final BillingClient billingClient;
    private final FirebaseAnalytics analytics;

    @Nullable
    private ProductDetails cachedProductDetails;
    @Nullable
    private Listener listener;
    private volatile boolean billingUnavailable;
    private volatile boolean connecting;

    public BillingManager(Context context) {
        appContext = context.getApplicationContext();
        productId = appContext.getString(R.string.premium_remove_ads_product_id);
        analytics = FirebaseAnalytics.getInstance(appContext);
        // enableAutoServiceReconnection: Play'sız emülatörde sonsuz retry spam üretir
        billingClient = BillingClient.newBuilder(appContext)
                .setListener(this)
                .enablePendingPurchases(
                        PendingPurchasesParams.newBuilder()
                                .enableOneTimeProducts()
                                .build())
                .build();
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    public void connectAndSync() {
        if (billingUnavailable || connecting || billingClient.isReady()) {
            return;
        }
        connecting = true;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                connecting = false;
                int code = billingResult.getResponseCode();
                if (code == BillingClient.BillingResponseCode.OK) {
                    billingUnavailable = false;
                    queryPurchasesAndApply();
                    queryProductDetails();
                    return;
                }
                if (code == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE) {
                    billingUnavailable = true;
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                connecting = false;
            }
        });
    }

    /** Mevcut satın alımları Play'den okur; premium bayrağını günceller. */
    public void queryPurchasesAndApply() {
        if (!billingClient.isReady()) {
            connectAndSync();
            return;
        }
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                (billingResult, purchases) -> {
                    if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK
                            || purchases == null) {
                        return;
                    }
                    boolean owned = false;
                    for (Purchase p : purchases) {
                        if (p.getProducts() != null && p.getProducts().contains(productId)
                                && p.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                            owned = true;
                            acknowledgeIfNeeded(p);
                        }
                    }
                    if (!owned) {
                        PremiumManager.getInstance(appContext).setAdFree(false);
                    }
                    notifyPremium();
                }
        );
    }

    private void queryProductDetails() {
        List<QueryProductDetailsParams.Product> list = Collections.singletonList(
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(list)
                .build();
        billingClient.queryProductDetailsAsync(params, this::cacheFirstProduct);
    }

    /** Satın alma ekranını açar; önce ürün detayı yoksa yüklenir. */
    public void launchPurchaseFlow(@NonNull Activity activity) {
        if (billingUnavailable) {
            postMessage(appContext.getString(R.string.premium_billing_unavailable));
            return;
        }
        if (!billingClient.isReady()) {
            connectAndSync();
            postMessage(appContext.getString(R.string.premium_billing_not_ready));
            return;
        }
        if (cachedProductDetails == null) {
            List<QueryProductDetailsParams.Product> list = Collections.singletonList(
                    QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
            );
            billingClient.queryProductDetailsAsync(
                    QueryProductDetailsParams.newBuilder().setProductList(list).build(),
                    (billingResult, productDetailsResult) -> {
                        cacheFirstProduct(billingResult, productDetailsResult);
                        if (cachedProductDetails != null) {
                            launchFlow(activity, cachedProductDetails);
                        } else {
                            postMessage(appContext.getString(R.string.premium_product_unavailable));
                        }
                    }
            );
            return;
        }
        launchFlow(activity, cachedProductDetails);
    }

    private void cacheFirstProduct(@NonNull BillingResult billingResult,
                                   @NonNull QueryProductDetailsResult productDetailsResult) {
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            return;
        }
        List<ProductDetails> detailsList = productDetailsResult.getProductDetailsList();
        if (detailsList != null && !detailsList.isEmpty()) {
            cachedProductDetails = detailsList.get(0);
        }
    }

    @Nullable
    private ProductDetails.OneTimePurchaseOfferDetails firstOneTimeOffer(
            @NonNull ProductDetails productDetails) {
        List<ProductDetails.OneTimePurchaseOfferDetails> offers =
                productDetails.getOneTimePurchaseOfferDetailsList();
        if (offers == null || offers.isEmpty()) {
            return null;
        }
        return offers.get(0);
    }

    private void launchFlow(@NonNull Activity activity, @NonNull ProductDetails productDetails) {
        BillingFlowParams.ProductDetailsParams.Builder purchaseParamsBuilder =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails);
        ProductDetails.OneTimePurchaseOfferDetails offer = firstOneTimeOffer(productDetails);
        if (offer != null) {
            String offerToken = offer.getOfferToken();
            if (offerToken != null && !offerToken.isEmpty()) {
                purchaseParamsBuilder.setOfferToken(offerToken);
            }
        }
        BillingFlowParams.ProductDetailsParams purchaseParams = purchaseParamsBuilder.build();
        List<BillingFlowParams.ProductDetailsParams> paramsList =
                Collections.singletonList(purchaseParams);
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(paramsList)
                .build();
        BillingResult result = billingClient.launchBillingFlow(activity, flowParams);
        if (result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            postMessage(appContext.getString(R.string.premium_purchase_failed));
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase p : purchases) {
                if (p.getProducts() != null && p.getProducts().contains(productId)) {
                    acknowledgeIfNeeded(p);
                }
            }
            Bundle b = new Bundle();
            b.putString("item_id", productId);
            analytics.logEvent("premium_purchase_updated", b);
            notifyPremium();
            return;
        }
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            postMessage(appContext.getString(R.string.premium_purchase_cancelled));
            return;
        }
        postMessage(appContext.getString(R.string.premium_purchase_failed));
    }

    private void acknowledgeIfNeeded(@NonNull Purchase purchase) {
        if (purchase.getPurchaseState() != Purchase.PurchaseState.PURCHASED) {
            return;
        }
        if (purchase.isAcknowledged()) {
            PremiumManager.getInstance(appContext).setAdFree(true);
            Bundle b = new Bundle();
            b.putString(FirebaseAnalytics.Param.ITEM_ID, productId);
            analytics.logEvent("premium_unlocked", b);
            notifyPremium();
            return;
        }
        AcknowledgePurchaseParams ackParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        billingClient.acknowledgePurchase(ackParams, billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                PremiumManager.getInstance(appContext).setAdFree(true);
                Bundle b = new Bundle();
                b.putString(FirebaseAnalytics.Param.ITEM_ID, productId);
                analytics.logEvent("premium_unlocked", b);
            }
            notifyPremium();
        });
    }

    private void notifyPremium() {
        if (listener != null) {
            listener.onPremiumStateChanged(PremiumManager.getInstance(appContext).isAdFree());
        }
    }

    private void postMessage(@NonNull String msg) {
        if (listener != null) {
            listener.onBillingMessage(msg);
            return;
        }
        // Ana ekran FAB satın almasında listener yoksa yine bildir
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(appContext, msg, Toast.LENGTH_LONG).show());
    }

    @Nullable
    public String getFormattedPrice() {
        if (cachedProductDetails == null) {
            return null;
        }
        ProductDetails.OneTimePurchaseOfferDetails offer = firstOneTimeOffer(cachedProductDetails);
        if (offer == null) {
            return null;
        }
        return offer.getFormattedPrice();
    }

    public void endConnection() {
        if (billingClient.isReady()) {
            billingClient.endConnection();
        }
    }
}
