/**
 * 
 */
package com.htc.sample.bleexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Displays a pointer.
 *
 */
public class PointerView extends View {
	
	// TODO calibrate based on first value?
	
	// TODO draw line based on previous values?
	
	private static final int POINTER_RADIUS_PX = 50;
	
	private static final int POINTER_STROKE_WIDTH_PX = 20;
	
	private Float mX;
	
	private Float mY;
	
	private Float mZ;
	
	private Float mRange;
	
	private Paint mPaint = new Paint();
	{
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(POINTER_STROKE_WIDTH_PX);
	}

	public PointerView(Context context) {
		super(context);
	}

	public PointerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PointerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void set(final Float aX, final Float aY, final Float aZ, final Float aRange) {
		mX = aX;
		mY = aY;
		mZ = aZ;
		mRange = aRange;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if ( null == mX || null == mY || null == mRange ) {
			//canvas.drawCircle(getWidth()/2, getHeight()/2, POINTER_RADIUS_PX, mPaint);
			return;
		}
		
		final float widthRangeRatio = getWidth() / mRange; // e.g. 1080 pixels / 2 G = 540
		final float heightRangeRatio = getHeight() / mRange; // E.g. 1920 pixels / 2G
		
		// e.g. +2G * 540 pixels/G = 1080 = right side of screen, -2G = 0 = left side
		final float pixelX = (mX + mRange / 2) * widthRangeRatio;
		final float pixelY = (mY + mRange / 2) * heightRangeRatio;
		
		canvas.drawCircle(pixelX, pixelY, POINTER_RADIUS_PX, mPaint);
	}

}
