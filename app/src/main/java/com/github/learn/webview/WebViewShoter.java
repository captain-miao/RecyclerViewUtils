package com.github.learn.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author YanLu
 * @since 16/3/12
 */
public class WebViewShoter {
    private static final String TAG = "WebViewShoter";


    /**
     * WevView screenshot
     *  API Level > 19 has bug, see {screenshotFix}
     * @param webView
     * @return
     */
    public static Bitmap screenshot(WebView webView) {
        try {
            float scale = webView.getScale();
            int height = (int) (webView.getContentHeight() * scale + 0.5);
            Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webView.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap screenshotFix(WebView webView) {
        webView.measure(MeasureSpec.makeMeasureSpec(
                        MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(webView.getMeasuredWidth(),
                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int iHeight = bitmap.getHeight();
        canvas.drawBitmap(bitmap, 0, iHeight, paint);
        webView.draw(canvas);
        return bitmap;
    }

    // 保存到sdcard
    public static boolean savePicToFile(Bitmap b, String strFileName, Context context) {
        if (b == null) {
            Log.e(TAG, "bitmap is null ");
            return false;
        }
        FileOutputStream fos = null;
        try {
            File file = new File(getSDCardPath() + "/" + strFileName);
            fos = new FileOutputStream(file);

            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            if(context != null) {
                context.sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
   	 * @return path
   	 */
   	private static String getSDCardPath() {
   		String sdcardDir = null;
   		boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
   		if (sdcardExist) {
   			sdcardDir = Environment.getExternalStorageDirectory().toString();
   		}
   		return sdcardDir;
   	}


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "" , "");

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
    }
}
