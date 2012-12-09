package edu.sis.catter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Pair;


public class GameActivity extends Activity {
    private GamePanel game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GamePanel(this);
        setContentView(game);
    }

    @Override
    public void onPause() {
        game.getMainLoop().setRunning(false);
        super.onPause();
    }

    public boolean isScoreBigEnough(int score) {
        SharedPreferences prefs = getSharedPreferences(
                HighScoresActivity.CATTER_PREFS_NAME, Context.MODE_PRIVATE);
        int lowest = prefs.getInt("catter_score_9",
                HighScoresActivity.DEFAULT_SCORE);
        return score > lowest;
    }

    public void addHighScore(String name, int score) {
        List<Pair<String, Integer>> scoresList = new ArrayList<Pair<String, Integer>>();
        SharedPreferences prefs = getSharedPreferences(
                HighScoresActivity.CATTER_PREFS_NAME, Context.MODE_PRIVATE);
        for (int i = 0; i < 10; ++i) {
            scoresList.add(Pair.create(prefs.getString(
                    HighScoresActivity.CATTER_NAME_PREFIX + i,
                    HighScoresActivity.DEFAULT_NAME), prefs.getInt(
                    HighScoresActivity.CATTER_SCORE_PREFIX + i,
                    HighScoresActivity.DEFAULT_SCORE)));
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
            e.putString(HighScoresActivity.CATTER_NAME_PREFIX + i, p.first);
            e.putInt(HighScoresActivity.CATTER_SCORE_PREFIX + i, p.second);
            ++i;
        }
        e.commit();
    }
}
