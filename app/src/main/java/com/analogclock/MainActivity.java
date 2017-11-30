package com.analogclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

  private ClockSurfaceView clockSurfaceView;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    clockSurfaceView = new ClockSurfaceView(this);
    setContentView(clockSurfaceView);
  }

  @Override protected void onPause() {
    super.onPause();
    clockSurfaceView.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
    clockSurfaceView.onResume();
  }
}
