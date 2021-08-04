package com.app.estockcard.view.admin.order;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.app.estockcard.R;

public class BadgeDrawable extends Drawable {

    private final Paint mTextPaint;
    private final Rect mTxtRect = new Rect();

    private String mCount = "";
    private boolean mWillDraw;

    public BadgeDrawable(Context context) {
        float mTextSize = context.getResources().getDimension(R.dimen.app_badge_text_size);

        Paint mBadgePaint = new Paint();
        mBadgePaint.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.orange700));
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
        Paint mBadgePaint1 = new Paint();
        mBadgePaint1.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.orange100));
        mBadgePaint1.setAntiAlias(true);
        mBadgePaint1.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(ContextCompat.getColor(context,R.color.light_blue500));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {



        if (!mWillDraw) {
            return;
        }
        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.

        /*Using Math.max rather than Math.min */

        float radius = ((Math.max(width, height) / 2)) / 2;
//        float centerX = (width - radius - 1) +5;
//        float centerY = radius -5;
        float centerX = width/2;
        float centerY = height/2;
//        if(mCount.length() <= 2){
            // Draw badge circle.
//            canvas.drawCircle(centerX, centerY, (int)(radius+7.5), mBadgePaint1);
//            canvas.drawCircle(centerX, centerY, (int)(radius+5.5), mBadgePaint);
            //canvas.drawCircle(centerX, centerY, (int)(radius), mBadgePaint1);
           //canvas.drawCircle(centerX, centerY, (int)(radius), mBadgePaint);
//        }
//        else{
//            canvas.drawCircle(centerX, centerY, (int)(radius+8.5), mBadgePaint1);
//            canvas.drawCircle(centerX, centerY, (int)(radius+6.5), mBadgePaint);
            //canvas.drawCircle(centerX, centerY, (int)(radius), mBadgePaint1);
            //canvas.drawCircle(centerX, centerY, (int)(radius), mBadgePaint);
//	        	canvas.drawRoundRect(radius, radius, radius, radius, 10, 10, mBadgePaint);
//        }
        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        if(mCount.length() > 2)
            canvas.drawText("99+", centerX, textY, mTextPaint);
        else
            canvas.drawText(mCount, centerX, textY, mTextPaint);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(String count) {
        mCount = count;

        // Only draw a badge if there are notifications.
        //mWillDraw = !count.equalsIgnoreCase("0");
        mWillDraw= true;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}