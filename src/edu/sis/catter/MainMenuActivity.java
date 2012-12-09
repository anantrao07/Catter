package edu.sis.catter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;


public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.start_button);
        Button highScoresButton = (Button) findViewById(R.id.high_scores);
        Button exitButton = (Button) findViewById(R.id.exit_button);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent gameScreen = new Intent(getApplicationContext(),
                        GameActivity.class);
                startActivity(gameScreen);
            }
        });

        highScoresButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent scoresScreen = new Intent(getApplicationContext(),
                        HighScoresActivity.class);
                startActivity(scoresScreen);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class MenuSurfaceView extends SurfaceView {
        private String title;
        private int titleXposition;

        public MenuSurfaceView(Context context) {
            super(context);
            init();
        }

        public MenuSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public MenuSurfaceView(Context context, AttributeSet attrs, int i) {
            super(context, attrs, i);
            init();
        }

        private void init() {
            setWillNotDraw(false);
            title = getContext().getString(R.string.title);
            int textWidth = (int) Constants.HEADER_FONT.measureText(title);
            titleXposition = (800 - textWidth) / 2; // hardcoded because
                                                    // getWidth() wouldn't work
                                                    // no matter what
        }

        @Override
        public void onDraw(Canvas c) {
            c.drawColor(Constants.BACKGROUND);
            c.drawText(title, titleXposition, 60, Constants.HEADER_FONT);
        }
    }

}
