package com.analogclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Calendar;

/**
 * Created by TigerSheet on 30/11/17.
 */

public class ClockSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
  private SurfaceHolder surfaceHolder;
  private Paint paint = new Paint();
  private Thread clockThread;
  private Context context;
  private int height;
  private int width;

  public ClockSurfaceView(Context context) {
    super(context);
    this.context = context;
    this.surfaceHolder = getHolder();
    this.surfaceHolder.addCallback(this);
    paint.setAntiAlias(true);
    paint.setDither(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(16);
    paint.setColor(Color.RED);
  }

  public void onResume() {
    Log.d("analog clock", "onresume");
    clockThread = new Thread(this);
//    clockThread.start();
  }

  public void onPause() {
    if (clockThread != null) {
      try {
        clockThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    Log.d("analog clock", "ondraw");
    //canvas.drawColor(Color.WHITE);
    paint.setAntiAlias(true);
    paint.setDither(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(16);
    paint.setColor(Color.RED);
    canvas.drawCircle(100, 100, 5, paint);
  }

  @Override public void surfaceCreated(SurfaceHolder holder) {
    Log.d("analog clock", "onsurface created");
    this.height = getHeight();
    this.width = getWidth();
    Canvas canvas = surfaceHolder.lockCanvas();
    /* canvas.drawColor(Color.WHITE);
    drawHourMarkers(canvas, width, height);
    drawMinuteMarkers(canvas, width, height);*/
    /*showHoursHandle(canvas, width, height, 2);
    showMinutesHandle(canvas, width, height, 30);
    showSecondsHandle(canvas, width, height, 45);*/
    surfaceHolder.unlockCanvasAndPost(canvas);
     clockThread.start();
  }

  @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    Log.d("analog clock", "onsurface changed");
  }

  @Override public void surfaceDestroyed(SurfaceHolder holder) {
    Log.d("analog clock", "onsurface destroyed");
  }

  private void drawHourMarkers(Canvas canvas, int width, int height) {
    paint.setStrokeWidth(10);
    int centerX = width / 2;
    int centerY = height / 2;
    int radius = 200;
    for (int i = 0; i < 12; i++) {
      float targetX = centerX + (float) (radius * Math.cos(getAngleInRadians(i * 30)));
      float targetY = centerY + (float) (radius * Math.sin(getAngleInRadians(i * 30)));
      canvas.drawPoint(targetX, targetY, paint);
    }
  }

  private void drawMinuteMarkers(Canvas canvas, int width, int height) {
    paint.setStrokeWidth(5);
    int centerX = width / 2;
    int centerY = height / 2;
    int radius = 200;
    for (int i = 0; i < 60; i++) {
      float targetX = centerX + (float) (radius * Math.cos(getAngleInRadians(i * 6)));
      float targetY = centerY + (float) (radius * Math.sin(getAngleInRadians(i * 6)));
      canvas.drawPoint(targetX, targetY, paint);
    }
  }

  private void showHoursHandle(Canvas canvas, int width, int height, int hour, int minutes,
      int seconds) {
    paint.setStrokeWidth(12);
    int startX = width / 2;
    int startY = height / 2;
    int handleLength = 140;
    float endX = startX + (float) (handleLength * Math.cos(getAngleInRadians(((hour) * 30)
        + getHoursAndSecondsAngleForMinutes(minutes, seconds)
        + 270)));
    float endY = startY + (float) (handleLength * Math.sin(getAngleInRadians(((hour) * 30)
        + getHoursAndSecondsAngleForMinutes(minutes, seconds)
        + 270)));
    canvas.drawLine(startX, startY, endX, endY, paint);
  }

  private int getHoursAndSecondsAngleForMinutes(int minutes, int seconds) {
    int minutesAngle = (int) (minutes * 0.5);
    //int secondsAngle = (int) (seconds * ) Todo add seconds angle too;
    return minutesAngle;
  }

  private void showMinutesHandle(Canvas canvas, int width, int height, int minutes, int seconds) {
    paint.setStrokeWidth(10);
    int startX = width / 2;
    int startY = height / 2;
    int handleLength = 170;
    float endX = startX + (float) (handleLength * Math.cos(
        getAngleInRadians((minutes * 6) + getMinutesAngleForSeconds(seconds) + 270)));
    float endY = startY + (float) (handleLength * Math.sin(
        getAngleInRadians((minutes * 6) + getMinutesAngleForSeconds(seconds) + 270)));
    canvas.drawLine(startX, startY, endX, endY, paint);
  }

  private int getMinutesAngleForSeconds(int seconds) {
    return (int) (seconds * (0.1));
  }

  private void showSecondsHandle(Canvas canvas, int width, int height, int seconds) {
    paint.setStrokeWidth(8);
    int startX = width / 2;
    int startY = height / 2;
    int handleLength = 185;
    float endX = startX + (float) (handleLength * Math.cos(getAngleInRadians((seconds * 6) + 270)));
    float endY = startY + (float) (handleLength * Math.sin(getAngleInRadians((seconds * 6) + 270)));
    canvas.drawLine(startX, startY, endX, endY, paint);
  }

  private double getAngleInRadians(int degrees) {
    return (3.1459 / 180) * degrees;
  }

  @Override public void run() {
    if (surfaceHolder.getSurface().isValid()) {
      while (true) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        Log.d("analog clock", hours + ":" + minutes + ":" + seconds);
        if (surfaceHolder.getSurface().isValid()) {
          Canvas canvas = surfaceHolder.lockCanvas();
          canvas.drawColor(Color.WHITE);
          drawHourMarkers(canvas, width, height);
          drawMinuteMarkers(canvas, width, height);
          showHoursHandle(canvas, width, height, hours, minutes, seconds);
          showMinutesHandle(canvas, width, height, minutes, seconds);
          showSecondsHandle(canvas, width, height, seconds);
          surfaceHolder.unlockCanvasAndPost(canvas);
        } else {
          Log.d("analog clock", "valid false");
        }
      }
    } else {
      Log.d("analog clock", "else");
    }
  }
}
