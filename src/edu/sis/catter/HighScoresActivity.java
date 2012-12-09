package edu.sis.catter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceView;


public class HighScoresActivity extends Activity {
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
        SharedPreferences prefs = getSharedPreferences(
                Constants.Prefs.CATTER_PREFS_NAME, Context.MODE_PRIVATE);
        for (int i = 0; i < 10; ++i) {
            StringBuffer text = new StringBuffer();
            text.append(i + 1)
                    .append(". ")
                    .append(prefs.getString(Constants.Prefs.CATTER_NAME_PREFIX
                            + i, Constants.Prefs.DEFAULT_NAME))
                    .append(": ")
                    .append(prefs.getInt(Constants.Prefs.CATTER_SCORE_PREFIX
                            + i, Constants.Prefs.DEFAULT_SCORE));
            scores.add(text.toString());
        }
    }

    private class HighScoresPanel extends SurfaceView {

        public HighScoresPanel(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        @Override
        public void onDraw(Canvas c) {
            c.drawColor(Constants.BACKGROUND);
            int i = 1;
            c.drawText("High scores:", 100, 50, Constants.HEADER_FONT);
            for (String s : scores) {
                c.drawText(s, 100, 100 + 30 * i++, Constants.NORMAL_FONT);
            }
        }
    }

}
