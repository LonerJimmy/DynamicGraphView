package loner.library.graph;

import android.content.Context;

/**
 * Created by loner on 2016/10/26.
 */
public class DimenUtil {

    private static DimenUtil mInstance;
    private Context mContext;

    public static DimenUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DimenUtil.class) {
                if (mInstance == null) {
                    mInstance = new DimenUtil(context);
                }
            }
        }
        return mInstance;
    }

    private DimenUtil(Context context) {
        mContext = context;
    }

    public int px2dip(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue / scale + 0.5f);
    }

    public float dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public float sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }
}
