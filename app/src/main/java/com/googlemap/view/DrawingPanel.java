package com.googlemap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.googlemap.R;

import java.util.LinkedList;

/**
 * Created by sotsys-074 on 2/1/16.
 */
    public class DrawingPanel extends View implements View.OnTouchListener {
        private static final String TAG = "DrawView";
        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;
        private LinkedList<Path> paths = new LinkedList<Path>();
        private Context mContext;
        private OnDragListener onDragListener;


        private Point pointXMin, pointYmin, pointxMax, pointYmax;

        public interface OnDragListener {
            void onDragEvent(MotionEvent motionEvent);
        }


        public OnDragListener getOnDragListener() {
            return onDragListener;
        }

        public void setOnDragListener(OnDragListener onDragListener) {
            this.onDragListener = onDragListener;
        }

        public DrawingPanel(Context context) {
            super(context);

            mContext = context;

            setFocusable(true);
            setFocusableInTouchMode(true);

            this.setOnTouchListener(this);

            pointXMin = new Point();
            pointYmin = new Point();
            pointxMax = new Point();
            pointYmax = new Point();

            pointXMin.set(0, 0);
            pointYmin.set(0, 0);
            pointxMax.set(0, 0);
            pointYmax.set(0, 0);


            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(10);
            mCanvas = new Canvas();
            mPath = new Path();
            paths.add(mPath);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for (Path p : paths) {
                canvas.drawPath(p, mPaint);
            }
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath = new Path();
            paths.add(mPath);
        }

        @Override
        public boolean onTouch(View arg0, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            if (pointXMin.x == 0) {
                pointXMin.x = (int) x;
                pointXMin.y = (int) y;

                pointxMax.x = (int) x;
                pointxMax.y = (int) y;

                pointYmin.x = (int) x;
                pointYmin.y = (int) y;

                pointYmin.x = (int) x;
                pointYmin.y = (int) y;
            }
            if (pointXMin.x > x) {
                pointXMin.x = (int) x;
                pointXMin.y = (int) y;
            }

            if (pointYmin.y > y) {
                pointYmin.x = (int) x;
                pointYmin.y = (int) y;
            }

            if (pointxMax.x < x) {
                pointxMax.x = (int) x;
                pointxMax.y = (int) y;
            }

            if (pointYmax.y < y) {
                pointYmax.x = (int) x;
                pointYmax.y = (int) y;
            }


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();

                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            onDragListener.onDragEvent(event);
            return true;
        }

        public void clear() {
            paths.clear();

            pointXMin.set(0, 0);
            pointYmin.set(0, 0);
            pointxMax.set(0, 0);
            pointYmax.set(0, 0);


            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(10);
            mCanvas = new Canvas();
            mPath = new Path();
            paths.add(mPath);
        }


        public Point getPointXMin() {
            return pointXMin;
        }

        public void setPointXMin(Point pointXMin) {
            this.pointXMin = pointXMin;
        }

        public Point getPointYmin() {
            return pointYmin;
        }

        public void setPointYmin(Point pointYmin) {
            this.pointYmin = pointYmin;
        }

        public Point getPointxMax() {
            return pointxMax;
        }

        public void setPointxMax(Point pointxMax) {
            this.pointxMax = pointxMax;
        }

        public Point getPointYmax() {
            return pointYmax;
        }

        public void setPointYmax(Point pointYmax) {
            this.pointYmax = pointYmax;
        }
    }
