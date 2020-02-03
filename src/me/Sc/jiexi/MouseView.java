package me.Sc.jiexi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 模拟鼠标视图，测试用
 * Created by Administrator on 2017/7/5.
 */

public class MouseView extends FrameLayout {

    private int mOffsetX;
    private int mOffsetY;

    private ImageView mMouseView;

    private Bitmap mMouseBitmap;

    private int mMouseX = 0;
    private int mMouseY = 0;

    //鼠标移动距离  px
    private int mMoveDis = 15;
    /**
     * mHandler
     */
    private Handler mHandler = new Handler();
    /**
     * 隐藏鼠标线程
     */
    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            mMouseView.setVisibility(GONE);
        }
    };


    public MouseView(@NonNull Context context) {
        super(context, null);
        init();
    }

    public MouseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化鼠标
     */
    private void init() {
        Drawable drawable = getResources().getDrawable(
                R.drawable.shubiao);
        mMouseBitmap = drawableToBitamp(drawable);
        mMouseView = new ImageView(getContext());
        mMouseView.setImageBitmap(mMouseBitmap);
        addView(mMouseView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mOffsetX = mMouseBitmap.getWidth();
        mOffsetY = mMouseBitmap.getHeight();

        mMouseX = 100;
        mMouseY = 100;

        setMouseShow();
    }

    /**
     * 生成一个鼠标图片
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, 96*2/3, 96*2/3, true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mMouseView != null) {
            mMouseView.layout(mMouseX, mMouseY, mMouseX + mMouseView.getMeasuredWidth(), mMouseY + mMouseView.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMouseView != null && mMouseBitmap != null) {
            mMouseView.measure(MeasureSpec.makeMeasureSpec(mMouseBitmap.getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mMouseBitmap.getHeight(), MeasureSpec.EXACTLY));
        }
    }

    /**
     * 设置鼠标显示，不移动鼠标10秒后隐藏
     */
    private void setMouseShow() {
        mMouseView.setVisibility(VISIBLE);
        mHandler.removeCallbacks(hideRunnable);
        mHandler.postDelayed(hideRunnable, 10000);
    }

    /**
     * 按键监听
     * <p>
     * 模拟鼠标点击要 发送 ACTION_DOWN ACTION_UP 两个事件才会生效
     *
     * @param webView
     * @param event
     */
    @SuppressWarnings("deprecation")
    public void moveMouse(com.tencent.smtt.sdk.WebView webView, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            setMouseShow();
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (mMouseX - mMoveDis >= 0) {
                        mMouseX -= mMoveDis;
                    } else {
                        mMouseX = 0;
                    }
                    sendMotionEvent(webView, mMouseX, mMouseY, MotionEvent.ACTION_HOVER_MOVE);
                    requestLayout();
                    break;

                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (mMouseX + mMoveDis + mOffsetX <= getMeasuredWidth()) {
                        mMouseX += mMoveDis;
                    } else {
                        mMouseX = getMeasuredWidth() - mOffsetX;
                    }
                    sendMotionEvent(webView, mMouseX, mMouseY, MotionEvent.ACTION_HOVER_MOVE);
                    requestLayout();
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (mMouseY - mMoveDis >= -10) {
                        mMouseY -= mMoveDis;
                    } else {
                        mMouseY = 0;
                        if (webView.getScrollY() - mMoveDis >= 0) {
                            webView.scrollBy(0, -mMoveDis);
                        }
                    }
                    sendMotionEvent(webView, mMouseX, mMouseY, MotionEvent.ACTION_HOVER_MOVE);
                    requestLayout();
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (mMouseY + mMoveDis + mOffsetY <= getMeasuredHeight()) {
                        mMouseY += mMoveDis;
                    } else {
                        mMouseY = getMeasuredHeight() - mOffsetY;
                        if ((webView.getContentHeight() * webView.getScale() - webView.getHeight()) - webView.getScrollY() >= -10) {
                            webView.scrollBy(0, mMoveDis);
                        }
                    }
                    sendMotionEvent(webView, mMouseX, mMouseY, MotionEvent.ACTION_HOVER_MOVE);
                    requestLayout();
                    break;

                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    sendMotionEvent(webView, mMouseX + 5, mMouseY + 5, event.getAction());
                    break;

                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        System.exit(0);
                    }
                    break;

                default:
                    return;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                    event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                sendMotionEvent(webView, mMouseX + 5, mMouseY + 5, event.getAction());
            }
        }
    }

    /**
     * 发送一个模拟按键事件
     *
     * @param webView
     * @param x
     * @param y
     * @param action
     */
    private void sendMotionEvent(com.tencent.smtt.sdk.WebView webView, int x, int y, int action) {
        MotionEvent motionEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, x, y, 0);
        if (action == MotionEvent.ACTION_HOVER_MOVE) {
            motionEvent.setSource(InputDevice.SOURCE_CLASS_POINTER);
            webView.dispatchGenericMotionEvent(motionEvent);
        } else {
            webView.dispatchTouchEvent(motionEvent);
        }
        motionEvent.recycle();
    }


}