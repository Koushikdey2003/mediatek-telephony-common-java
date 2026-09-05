package com.mediatek.internal.telephony.subscription;

import com.android.internal.telephony.subscription.SubscriptionInfoInternal;

/* JADX INFO: loaded from: classes.dex */
public class MtkSubscriptionInfoInternal extends SubscriptionInfoInternal {
    private final int mIsEnhancedVonrShowUiEnabled;

    private MtkSubscriptionInfoInternal(Builder builder) {
        super(builder);
        this.mIsEnhancedVonrShowUiEnabled = builder.mIsEnhancedVonrShowUiEnabled;
    }

    public int getEnhancedVonrShowUiEnabled() {
        return this.mIsEnhancedVonrShowUiEnabled;
    }

    public String toString() {
        return "[MtkSubscriptionInfoInternal: isEnhancedVonrShowUiEnabled=" + this.mIsEnhancedVonrShowUiEnabled + "]" + super.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtkSubscriptionInfoInternal that = (MtkSubscriptionInfoInternal) o;
        if (this.mIsEnhancedVonrShowUiEnabled == that.mIsEnhancedVonrShowUiEnabled && super.equals(o)) {
            return true;
        }
        return false;
    }

    public static class Builder extends SubscriptionInfoInternal.Builder {
        private int mIsEnhancedVonrShowUiEnabled;

        public Builder() {
            this.mIsEnhancedVonrShowUiEnabled = -1;
        }

        public Builder(SubscriptionInfoInternal info) {
            super(info);
            this.mIsEnhancedVonrShowUiEnabled = -1;
            if (info instanceof MtkSubscriptionInfoInternal) {
                this.mIsEnhancedVonrShowUiEnabled = ((MtkSubscriptionInfoInternal) info).mIsEnhancedVonrShowUiEnabled;
            }
        }

        public Builder setEnhancedVonrShowUiEnabled(int isEnhancedVonrShowUiEnabled) {
            this.mIsEnhancedVonrShowUiEnabled = isEnhancedVonrShowUiEnabled;
            return this;
        }

        public SubscriptionInfoInternal build() {
            return new MtkSubscriptionInfoInternal(this);
        }
    }
}
