package edu.sis.catter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;


public class HighScoresActivity extends Activity {
    private static final String DEFAULT_NAME = "---";
    private static final int DEFAULT_SCORE = 0;

    private HighScoresPanel scoresPanel;
    private List<String> scores = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scoresPanel = new HighScoresPanel(this);
        setContentView(scoresPanel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("catter_prefs",
                Context.MODE_PRIVATE);
        for (int i = 0; i < 10; ++i) {
            StringBuffer text = new StringBuffer();
            text.append(i + 1).append(". ")
                    .append(prefs.getString("catter_name_" + i, DEFAULT_NAME))
                    .append(": ")
                    .append(prefs.getInt("catter_score_" + i, DEFAULT_SCORE));
            scores.add(text.toString());
            Log.d("scores", text.toString());
        }
    }

    private class HighScoresPanel extends SurfaceView {
        private Paint paint;

        public HighScoresPanel(Context context) {
            super(context);
            setWillNotDraw(false);
            paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.SANS_SERIF);
            paint.setTextSize(25);
            paint.setAntiAlias(true);
        }

        @SuppressLint("DrawAllocation")
        @Override
        public void onDraw(Canvas c) {
            c.drawColor(Color.BLACK);
            int i = 1;
            c.drawText("High scores:", 100, 30, paint);
            for (String s : scores) {
                c.drawText(s, 100, 80 + 30 * i++, paint);
            }
        }
    }
}
