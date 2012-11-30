package edu.sis.catter;

import android.app.Activity;
import android.os.Bundle;


public class GameActivity extends Activity {
    private GamePanel game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GamePanel(this);
        setContentView(game);
    }

    @Override
    public void onBackPressed() {
        game.getMainLoop().setRunning(false);
        super.onBackPressed();
    }
}
