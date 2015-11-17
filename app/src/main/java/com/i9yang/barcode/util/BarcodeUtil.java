package com.i9yang.barcode.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class BarcodeUtil {
	public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws Exception {
		BitMatrix result = new MultiFormatWriter().encode(contents, format, img_width, img_height);
		Bitmap bitmap = Bitmap.createBitmap(img_width, img_height, Bitmap.Config.ARGB_8888);
		for (int i = 0; i < img_width; i++) {
			for (int j = 0; j < img_height; j++) {
				bitmap.setPixel(i, j, result.get(i, j) ? Color.BLACK : Color.WHITE);
			}
		}

		return bitmap;
	}
}
