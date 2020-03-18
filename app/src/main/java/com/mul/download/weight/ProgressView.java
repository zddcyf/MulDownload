package com.mul.download.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.mul.download.util.ScreenUtils;

/**
 * @ProjectName: MulDownload
 * @Package: com.mul.download.util
 * @ClassName: ProgressView
 * @Author: zdd
 * @CreateDate: 2019/9/4 9:29
 * @Description: 进度条view
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/9/4 9:29
 * @UpdateRemark: 更新说明
 * @Version: v1.0.0
 */
public class ProgressView extends View {
    private Paint bgPaint; // 外围背景画笔
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private float progress;
    private boolean isInit;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isInit = true;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 设置圆环
        bgPaint = new Paint();
        bgPaint.setDither(true);
        // 消除锯齿
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec);
        this.heightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("测试onProgress走进canvas", "走进canvas" + progress);
        drawProgressCircle(canvas);
        drawProgressText(canvas);
    }

    private void drawProgressText(Canvas canvas) {
        String text = String.format("%s", (int)(progress * 100 / 360));

        // 将坐标原点移到控件中心
        bgPaint.setTextSize(12);
        canvas.translate(widthMeasureSpec / 2, heightMeasureSpec / 2);
        bgPaint.setStrokeWidth(0);//设置画笔粗细
        bgPaint.setColor(Color.parseColor("#DBDBDB"));

        // 文字款
        float textWidth = bgPaint.measureText(text);
        // 文字baseline在y轴方向的位置
        float baseLineY = Math.abs(bgPaint.ascent() + bgPaint.descent()) / 2;
        canvas.drawText(text, -textWidth / 2, baseLineY, bgPaint);
    }

    private void drawProgressCircle(Canvas canvas) {
        bgPaint.setStrokeWidth(3);//设置画笔粗细
        bgPaint.setColor(Color.parseColor("#979797"));
        int radius = widthMeasureSpec / 2 - ScreenUtils.px(3);
//        int radius = 0;
        canvas.drawCircle(widthMeasureSpec / 2, heightMeasureSpec / 2, radius, bgPaint);
        if (progress > 0) {
            bgPaint.setColor(Color.parseColor("#18938B"));
        }
        //外圆弧背景
        RectF oval1 = new RectF();
        oval1.left = widthMeasureSpec / 2 - radius;
        oval1.top = widthMeasureSpec / 2 - radius;
        oval1.right = widthMeasureSpec / 2 + radius;
        oval1.bottom = widthMeasureSpec / 2 + radius;
        /**
         * true为不是圆环。false为圆环
         */
//        canvas.drawArc(oval1, 0, progress, true, bgPaint);
        canvas.drawArc(oval1, 0, progress, false, bgPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
        Log.d("测试onProgress进度百分比", progress + "");
    }
}
