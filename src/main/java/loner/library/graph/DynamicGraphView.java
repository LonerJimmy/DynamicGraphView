package loner.library.graph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loner on 2016/10/26.
 */

public class DynamicGraphView extends SurfaceView implements SurfaceHolder.Callback {

    private final float SLEEP_TIME = 3.0f;

    private GraphPaint mPaint;
    private List<NewReportInfo> mList = new ArrayList<>();
    private float width;
    private float height;
    private Context mContext;
    //圆圈半径
    private float radius;
    private int rate = 0;

    private long interval;

    private DrawThread mThread;
    private final Object mSurfaceLock = new Object();

    DimenUtil mDimenUtil;

    //圆圈和线的paint
    Paint pointPaint;
    float x;

    private int background, complete, error;
    private int max, min;
    private int multiple;


    private float tmpCompleteY = 0;
    private float tmpOverTimeY = 0;
    private int tmpNum = 0;

    public DynamicGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public DynamicGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicGraphView(Context context) {
        this(context, null, 0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化背景
        width = getWidth();
        height = getHeight();

        x = (float) (width / 4.0);

        //开始动态变化
        mThread = new DrawThread(holder);
        mThread.setRun(true);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //changed的时候，获取surfaceview宽高等
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        synchronized (mSurfaceLock) {
            mThread.setRun(false);
        }
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        mContext = context;

        TypedArray type = context.obtainStyledAttributes(attributeSet, R.styleable.DynamicGraphAttrs);
        background = type.getColor(R.styleable.DynamicGraphAttrs_defaultBackground, 0xffDDDDDD);
        complete = type.getColor(R.styleable.DynamicGraphAttrs_complete, 0xff008AF1);
        error = type.getColor(R.styleable.DynamicGraphAttrs_error, 0xffFF7800);
        interval = type.getInteger(R.styleable.DynamicGraphAttrs_interval, 60);
        mPaint = new GraphPaint(background, complete, error);

        mDimenUtil = DimenUtil.getInstance(mContext);

        radius = DimenUtil.getInstance(context).dip2px(3);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
    }


    private void drawAnimation(Canvas canvas) {
        canvas.drawColor(0xffffffff);
        float sum = interval / SLEEP_TIME;

        if (rate >= sum) {
            drawPoints(canvas, sum - 1);
            mThread.setRun(false);
            return;
        }

        drawPoints(canvas, sum);

        rate++;
    }

    private void drawPoints(Canvas canvas, float sum) {

        pointPaint.setStrokeWidth(mDimenUtil.dip2px(2));
        //画线
        for (int i = 0; i < mList.size(); i++) {
            tmpNum = mList.get(i).getCompleteAmount();
            if (mList.get(i).getCompleteAmount() > 1000) {
                tmpNum = 1000;
            }
            drawLines(canvas, sum, i);
        }

        for (int i = 0; i < mList.size(); i++) {
            tmpNum = mList.get(i).getCompleteAmount();
            if (mList.get(i).getCompleteAmount() > 1000) {
                tmpNum = 1000;
            }
            drawCircles(canvas, sum, i);
            drawFullCircles(canvas, sum, i);
            drawWords(canvas, sum, i);
        }

    }

    private float differenceX(int x) {
        return (String.valueOf(x).length()) * (mDimenUtil.dip2px(3.75f));
    }

    /**
     * 画
     *
     * @param i
     */
    private void drawWords(Canvas canvas, float sum, int i) {
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(0xff333333);
        pointPaint.setStrokeWidth(1);
        pointPaint.setTextSize(mDimenUtil.sp2px(12.0f));

        if (i == 3) {
            return;
        }

        canvas.drawText("" + mList.get(i).getCompleteAmount(),
                mDimenUtil.dip2px(40) + x * i - differenceX(mList.get(i).getCompleteAmount()),
                mDimenUtil.dip2px((float) ((300 - rate * ((tmpNum - min) / multiple) / sum) / 2.00)) - mDimenUtil.dip2px(16),
                pointPaint);
        canvas.drawText("" + mList.get(i).getOverTimeAmount(),
                mDimenUtil.dip2px(40) + x * i - differenceX(mList.get(i).getOverTimeAmount()),
                mDimenUtil.dip2px((float) ((300 - rate * ((mList.get(i).getOverTimeAmount() - min) / multiple) / sum) / 2.00)) - mDimenUtil.dip2px(16),
                pointPaint);
    }

    /**
     * 画内部实心圆
     *
     * @param i
     */
    private void drawFullCircles(Canvas canvas, float sum, int i) {

        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setStrokeWidth(mDimenUtil.dip2px(2));
        pointPaint.setColor(0xffffffff);

        canvas.drawCircle(mDimenUtil.dip2px(40) + x * i,
                mDimenUtil.dip2px((float) ((300 - rate * ((tmpNum - min) / multiple) / sum) / 2.00) - 10),
                mDimenUtil.dip2px(0.8f),
                pointPaint);

        canvas.drawCircle(mDimenUtil.dip2px(40) + x * i,
                mDimenUtil.dip2px((float) ((300 - rate * ((mList.get(i).getOverTimeAmount() - min) / multiple) / sum) / 2.00) - 10),
                mDimenUtil.dip2px(0.8f),
                pointPaint);
    }

    /**
     * 画外面的圆
     *
     * @param i
     */
    private void drawCircles(Canvas canvas, float sum, int i) {
        //完成单
        pointPaint.setStrokeWidth(mDimenUtil.dip2px(2));
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setColor(mPaint.getComplete());
        if (i == 3) {
            pointPaint.setColor(background);
        }

        canvas.drawCircle(mDimenUtil.dip2px(40) + x * i,
                mDimenUtil.dip2px((float) ((300 - rate * ((tmpNum - min) / multiple) / sum) / 2.00) - 10),
                radius,
                pointPaint);

        //超时单
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(mPaint.getError());
        if (i == 3) {
            pointPaint.setColor(background);
        }

        canvas.drawCircle(mDimenUtil.dip2px(40) + x * i,
                mDimenUtil.dip2px((float) ((300 - rate * ((mList.get(i).getOverTimeAmount() - min) / multiple) / sum) / 2.00) - 10),
                radius,
                pointPaint);
    }

    /**
     * 画线
     *
     * @param i
     */
    private void drawLines(Canvas canvas, float sum, int i) {

        //完成单
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setColor(mPaint.getComplete());
        if (i == 3) {
            pointPaint.setColor(background);
        }

        if (i > 0) {
            canvas.drawLine(mDimenUtil.dip2px(40) + x * (i - 1),
                    tmpCompleteY,
                    mDimenUtil.dip2px(40) + x * i,
                    mDimenUtil.dip2px((float) ((300 - rate * ((tmpNum - min) / multiple) / sum) / 2.00) - 10),
                    pointPaint);
        }

        //超时单
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(mPaint.getError());
        if (i == 3) {
            pointPaint.setColor(background);
        }

        if (i > 0) {
            canvas.drawLine(mDimenUtil.dip2px(40) + x * (i - 1),
                    tmpOverTimeY,
                    mDimenUtil.dip2px(40) + x * i,
                    mDimenUtil.dip2px((float) ((300 - rate * ((mList.get(i).getOverTimeAmount() - min) / multiple) / sum) / 2.00) - 10),
                    pointPaint);
        }

        tmpCompleteY = mDimenUtil.dip2px((float) ((300 - rate * ((tmpNum - min) / multiple) / sum) / 2.00) - 10);
        tmpOverTimeY = mDimenUtil.dip2px((float) ((300 - rate * ((mList.get(i).getOverTimeAmount() - min) / multiple) / sum) / 2.00) - 10);
    }

    private class DrawThread extends Thread {
        private SurfaceHolder mHolder;
        private boolean mIsRun = false;

        public DrawThread(SurfaceHolder holder) {
            mHolder = holder;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (mSurfaceLock) {
                    if (!mIsRun) {
                        return;
                    }
                    Canvas drawCanvas = mHolder.lockCanvas();
                    if (drawCanvas != null) {
                        drawAnimation(drawCanvas);
                        mHolder.unlockCanvasAndPost(drawCanvas);
                    }
                }
                try {
                    Thread.sleep((int) SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setRun(boolean mIsRun) {
            this.mIsRun = mIsRun;
        }
    }

    public void setPoint(List<NewReportInfo> list) {
        mList = list;

        max = mList.get(0).getCompleteAmount();
        min = mList.get(0).getOverTimeAmount();
        for (int x = 0; x < mList.size(); x++) {
            if (mList.get(x).getCompleteAmount() > max) {
                max = mList.get(x).getCompleteAmount();
            }
            if (mList.get(x).getOverTimeAmount() > max) {
                max = mList.get(x).getOverTimeAmount();
            }
            if (mList.get(x).getCompleteAmount() < min) {
                min = mList.get(x).getCompleteAmount();
            }
            if (mList.get(x).getOverTimeAmount() < min) {
                min = mList.get(x).getOverTimeAmount();
            }

            if (max > 1000) {
                max = 1000;
            }
        }

        multiple = (int) Math.ceil((max - min) / (DimenUtil.getInstance(mContext).dip2px(60)));
    }

    public void start() {
        getHolder().addCallback(this);
    }

}
