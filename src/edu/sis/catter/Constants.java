package edu.sis.catter;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;


public class Constants {
    public static final int BACKGROUND = Color.rgb(0x00, 0x34, 0x66);
    public static final Paint NORMAL_FONT = new Paint();
    public static final Paint HEADER_FONT = new Paint();

    static {
        NORMAL_FONT.setColor(Color.WHITE);
        NORMAL_FONT.setTypeface(Typeface.SANS_SERIF);
        NORMAL_FONT.setTextSize(25);
        NORMAL_FONT.setAntiAlias(true);

        HEADER_FONT.setColor(Color.WHITE);
        HEADER_FONT.setTypeface(Typeface.SANS_SERIF);
        HEADER_FONT.setTextSize(40);
        HEADER_FONT.setAntiAlias(true);
    }

    public interface Prefs {
        public static final String CATTER_PREFS_NAME = "catter_prefs";
        public static final String CATTER_SCORE_PREFIX = "catter_score_";
        public static final String CATTER_NAME_PREFIX = "catter_name_";
        public static final String DEFAULT_NAME = "---";
        public static final int DEFAULT_SCORE = 0;
    }
}
