package utils.image;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by julianschembri on 15/02/16.
 */
public class ImageAnalyzer {

    public static boolean isDark(Bitmap bitmap) {
        boolean dark=false;

        float darkThreshold = bitmap.getWidth()*bitmap.getHeight()*0.45f;
        int darkPixels=0;

        int[] pixels = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());

        for(int pixel : pixels){
            int r = Color.red(pixel);
            int g = Color.green(pixel);
            int b = Color.blue(pixel);
            double luminance = (0.299*r+0.0f + 0.587*g+0.0f + 0.114*b+0.0f);
            if (luminance<150) {
                darkPixels++;
            }
        }

        if (darkPixels >= darkThreshold) {
            dark = true;
        }
        return dark;
    }
}
