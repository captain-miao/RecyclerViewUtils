package com.github.captain_miao.recyclerviewutils.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：EasySidebar
 * Created by：CaMnter
 * Time：2016-04-05 21:18
 *
 * from https://github.com/CaMnter/EasyRecyclerViewSidebar
 */
public class EasySidebar extends View {

    private static final int DEFAULT_VIEW_BACKGROUND = 0x40000000;
    private static final int DEFAULT_FONT_COLOR = 0xff444444;
    private static final int DEFAULT_FONT_SIZE = 14;

    private static final int MAX_SECTION_COUNT = 30;

    private Paint paint;
    private TextView floatView;
    private float sectionHeight;
    private float allSectionHeight;
    private float sectionFontSize;

    private List<String> sections;

    private OnClickSectionListener onClickSectionListener;


    public EasySidebar(Context context) {
        super(context);
        this.init(context, null);
    }


    public EasySidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }


    public EasySidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EasySidebar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.sectionFontSize = this.sp2px(context, DEFAULT_FONT_SIZE);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setColor(DEFAULT_FONT_COLOR);
        this.paint.setTextSize(this.sectionFontSize);
        this.sections = new ArrayList<>();
    }


    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();
        int viewHalfWidth = viewWidth / 2;
        this.sectionHeight = viewHeight / MAX_SECTION_COUNT;
        if (this.sections.size() > 0) {
            this.allSectionHeight = this.sectionHeight * this.sections.size();
            float top = viewHeight / 2 - allSectionHeight / 2 + this.sectionHeight / 2 -
                    this.sectionFontSize / 2;
            for (int i = 0; i < this.sections.size(); i++) {
                String section = this.sections.get(i);
                canvas.drawText(section, viewHalfWidth,
                        top + this.sectionHeight * i, this.paint);
            }
        } else {
            this.sectionHeight = 0;
            this.allSectionHeight = 0;
        }
    }


    @Override public boolean onTouchEvent(MotionEvent event) {
        if (this.sections == null || this.sections.size() < 1) return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setBackgroundColor(DEFAULT_VIEW_BACKGROUND);
                this.floatView.setVisibility(VISIBLE);
                this.showFloatView(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                this.showFloatView(event);
                return true;
            case MotionEvent.ACTION_UP:
                this.setBackgroundColor(Color.TRANSPARENT);
                hideFloatView();
                return true;
            case MotionEvent.ACTION_CANCEL:
                this.setBackgroundColor(Color.TRANSPARENT);
                hideFloatView();

                return true;
        }
        return super.onTouchEvent(event);
    }


    private int getSectionIndex(float y) {
        float currentY = y - (this.getHeight() - this.allSectionHeight) / 2;
        int index = (int) (currentY / this.sectionHeight);
        if (index <= 0) {
            return 0;
        }
        if (index >= this.sections.size()) {
            index = this.sections.size() - 1;
        }
        return index;
    }


    private void hideFloatView(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                floatView.setVisibility(INVISIBLE);
            }
        }, 300);
    }
    private void showFloatView(MotionEvent event) {
        int sectionIndex = this.getSectionIndex(event.getY());
        if (this.sections == null || this.sections.size() < 1 ||
                sectionIndex >= this.sections.size()) {
            return;
        }
        String floatText = this.sections.get(sectionIndex);

        this.floatView.setText(floatText);
        if (this.onClickSectionListener != null) {
            this.onClickSectionListener.onClickSection(sectionIndex);
        }
    }


    private int sp2px(Context context, float sp) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5F);
    }


    public void setSections(List<String> sections) {
        this.sections.clear();
        this.sections.addAll(sections);
    }


    public TextView getFloatView() {
        return floatView;
    }


    public void setFloatView(TextView floatView) {
        this.floatView = floatView;
    }


    public OnClickSectionListener getOnClickSectionListener() {
        return onClickSectionListener;
    }


    public void setOnClickSectionListener(OnClickSectionListener onClickSectionListener) {
        this.onClickSectionListener = onClickSectionListener;
    }


    public interface OnClickSectionListener {
        /**
         * On touch section
         *
         * @param sectionIndex sectionIndex
         */
        void onClickSection(int sectionIndex);
    }
}
