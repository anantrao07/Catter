package edu.sis.catter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceView;


public class HighScoresActivity extends Activity {
    public static final String CATTER_PREFS_NAME = "catter_prefs";
    public static final String CATTER_SCORE_PREFIX = "catter_score_";
    public static final String CATTER_NAME_PREFIX = "catter_name_";
    public static final String DEFAULT_NAME = "---";
    public static final int DEFAULT_SCORE = 0;

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
        SharedPreferences prefs = getSharedPreferences(CATTER_PREFS_NAME,
                Context.MODE_PRIVATE);
        for (int i = 0; i < 10; ++i) {
            StringBuffer text = new StringBuffer();
            text.append(i + 1)
                    .append(". ")
                    .append(prefs.getString(CATTER_NAME_PREFIX + i,
                            DEFAULT_NAME))
                    .append(": ")
                    .append(prefs
                            .getInt(CATTER_SCORE_PREFIX + i, DEFAULT_SCORE));
            scores.add(text.toString());
            Log.d("catter high scores", text.toString());
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

    // TODO paste those two functions below wherever they are needed
    public boolean isScoreBigEnough(int score) {
        SharedPreferences prefs = getSharedPreferences(CATTER_PREFS_NAME,
                Context.MODE_PRIVATE);
        int lowest = prefs.getInt("catter_score_9", DEFAULT_SCORE);
        return score > lowest;
    }

    public void addHighScore(String name, int score) {
        List<Pair<String, Integer>> scoresList = new ArrayList<Pair<String, Integer>>();
        SharedPreferences prefs = getSharedPreferences(CATTER_PREFS_NAME,
                Context.MODE_PRIVATE);
        for (int i = 0; i < 10; ++i) {
            scoresList.add(Pair.create(
                    prefs.getString(CATTER_NAME_PREFIX + i, DEFAULT_NAME),
                    prefs.getInt(CATTER_SCORE_PREFIX + i, DEFAULT_SCORE)));
        }
        scoresList.add(Pair.create(name, score));

        Collections.sort(scoresList, new Comparator<Pair<String, Integer>>() {

            @Override
            public int compare(Pair<String, Integer> lhs,
                    Pair<String, Integer> rhs) {
                if (lhs.second > rhs.second) {
                    return -1;
                } else if (lhs.second < rhs.second) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        Editor e = prefs.edit();
        int i = 0;
        for (Pair<String, Integer> p : scoresList.subList(0, 10)) {
            e.putString(CATTER_NAME_PREFIX + i, p.first);
            e.putInt(CATTER_SCORE_PREFIX + i, p.second);
            ++i;
        }
        e.commit();
    }
}
