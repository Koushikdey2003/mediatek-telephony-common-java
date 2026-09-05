package com.mediatek.internal.telephony.subscription;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import com.android.internal.telephony.subscription.SubscriptionDatabaseManager;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.util.function.TriConsumer;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionInfoInternal;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class MtkSubscriptionDatabaseManager extends SubscriptionDatabaseManager {
    private static final String LOG_TAG = "MTK_SDM";
    private static final Map<String, Function<MtkSubscriptionInfoInternal, ?>> MTK_SUBSCRIPTION_GET_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("vonr_ui_enabled", new Function() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda2
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((MtkSubscriptionInfoInternal) obj).getEnhancedVonrShowUiEnabled());
        }
    }));
    private static final Map<String, TriConsumer<MtkSubscriptionDatabaseManager, Integer, Integer>> MTK_SUBSCRIPTION_SET_INTEGER_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("vonr_ui_enabled", new TriConsumer() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda3
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MtkSubscriptionDatabaseManager) obj).setEnhancedVonrShowUiEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }));

    public MtkSubscriptionDatabaseManager(Context context, Looper looper, SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback callback) {
        super(context, looper, callback);
    }

    protected SubscriptionInfoInternal.Builder onCreateSubscriptionInfoInternalBuilder(SubscriptionInfoInternal subInfo) {
        return new MtkSubscriptionInfoInternal.Builder(subInfo);
    }

    protected SubscriptionInfoInternal createSubscriptionInfoFromCursor(Cursor cursor) {
        SubscriptionInfoInternal subInfo = super.createSubscriptionInfoFromCursor(cursor);
        MtkSubscriptionInfoInternal.Builder builder = new MtkSubscriptionInfoInternal.Builder(subInfo).setEnhancedVonrShowUiEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("vonr_ui_enabled")));
        return builder.build();
    }

    protected Object getSubscriptionInfoFieldByColumnName(SubscriptionInfoInternal subInfo, String columnName) {
        Map<String, Function<MtkSubscriptionInfoInternal, ?>> map = MTK_SUBSCRIPTION_GET_METHOD_MAP;
        if (map.containsKey(columnName) && (subInfo instanceof MtkSubscriptionInfoInternal)) {
            return map.get(columnName).apply((MtkSubscriptionInfoInternal) subInfo);
        }
        return super.getSubscriptionInfoFieldByColumnName(subInfo, columnName);
    }

    public void setSubscriptionProperty(int subId, String columnName, Object value) {
        int intValue;
        Map<String, TriConsumer<MtkSubscriptionDatabaseManager, Integer, Integer>> map = MTK_SUBSCRIPTION_SET_INTEGER_METHOD_MAP;
        if (map.containsKey(columnName)) {
            if (value instanceof String) {
                intValue = Integer.parseInt((String) value);
            } else if (value instanceof Integer) {
                intValue = ((Integer) value).intValue();
            } else {
                throw new ClassCastException("columnName=" + columnName + ", cannot cast " + value.getClass() + " to integer.");
            }
            map.get(columnName).accept(this, Integer.valueOf(subId), Integer.valueOf(intValue));
            return;
        }
        super.setSubscriptionProperty(subId, columnName, value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnhancedVonrShowUiEnabled(int subId, int isEnhancedVonrShowUiEnabled) {
        writeDatabaseAndCacheHelper(subId, "vonr_ui_enabled", Integer.valueOf(isEnhancedVonrShowUiEnabled), new BiFunction() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((MtkSubscriptionInfoInternal.Builder) obj).setEnhancedVonrShowUiEnabled(((Integer) obj2).intValue());
            }
        });
    }

    private <T> void writeDatabaseAndCacheHelper(final int subId, final String columnName, final T newValue, final BiFunction<MtkSubscriptionInfoInternal.Builder, T, MtkSubscriptionInfoInternal.Builder> builderSetMethod) {
        final ContentValues contentValues = new ContentValues();
        this.mReadWriteLock.writeLock().lock();
        try {
            SubscriptionInfoInternal oldSubInfo = (SubscriptionInfoInternal) this.mAllSubscriptionInfoInternalCache.get(Integer.valueOf(subId));
            if (oldSubInfo == null) {
                throw new IllegalArgumentException("Subscription doesn't exist. subId=" + subId + ", columnName=" + columnName);
            }
            this.mAllSubscriptionInfoInternalCache.forEach(new BiConsumer() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    this.f$0.lambda$writeDatabaseAndCacheHelper$1(subId, columnName, newValue, builderSetMethod, contentValues, (Integer) obj, (SubscriptionInfoInternal) obj2);
                }
            });
        } finally {
            this.mReadWriteLock.writeLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeDatabaseAndCacheHelper$1(final int subId, String columnName, Object newValue, BiFunction builderSetMethod, ContentValues contentValues, Integer id, SubscriptionInfoInternal subInfo) {
        if (id.intValue() == subId && !Objects.equals(getSubscriptionInfoFieldByColumnName(subInfo, columnName), newValue)) {
            MtkSubscriptionInfoInternal.Builder builder = new MtkSubscriptionInfoInternal.Builder(subInfo);
            MtkSubscriptionInfoInternal.Builder builder2 = (MtkSubscriptionInfoInternal.Builder) builderSetMethod.apply(builder, newValue);
            contentValues.putObject(columnName, newValue);
            if (updateDatabase(id.intValue(), contentValues) > 0) {
                this.mAllSubscriptionInfoInternalCache.put(id, builder2.build());
                this.mCallback.invokeFromExecutor(new Runnable() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$writeDatabaseAndCacheHelper$0(subId);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeDatabaseAndCacheHelper$0(int subId) {
        this.mCallback.onSubscriptionChanged(subId);
    }

    public void setCarrierName(int subId, String carrierName) {
        if (carrierName == null) {
            carrierName = "";
        }
        super.setCarrierName(subId, carrierName);
    }
}
