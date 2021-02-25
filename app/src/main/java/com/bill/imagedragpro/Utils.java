package com.bill.imagedragpro;

import android.graphics.Color;

/**
 * author : Bill
 * date : 2021/2/25
 * description :
 */
public class Utils {

    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

}
