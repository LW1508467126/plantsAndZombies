package cn.itsource.game.pz.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.Display;
import cn.itsource.game.pz.Configs;

public class ConfigUtils {
	// 获取屏幕的宽高
	public static void init(Activity activity) {
		Display defaultDisplay = activity.getWindowManager()
				.getDefaultDisplay();
		Configs.screenWidth = defaultDisplay.getWidth();
		Configs.screenHeight = defaultDisplay.getHeight();
	}

	// 根据指定的图片宽高处理图片的横纵比例
	// int w, int h是手机屏幕的宽高
	public static Bitmap resizeBitmap(Bitmap bitmap, int imageWidth,
			int imageHeight) {
		if (bitmap != null) {
			// 获取原生图片的宽高
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 计算横纵比例
			float scaleWidth = ((float) imageWidth) / width;// 480/900=0.53
			float scaleHeight = ((float) imageHeight) / height;// 320/600=0.53
			// 矩阵对象
			Matrix matrix = new Matrix();
			// matrix.postScale(float x,float
			// y),注意中间的参数，如果x>1的话，那么就是放大，如果x<1的话，就是缩小
			// 对图片进行缩放
			matrix.postScale(scaleWidth, scaleHeight);
			// 根据原图安装缩放对象的比例进行一定缩放
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
					height, matrix, true);
			return resizedBitmap;
		} else {
			throw new RuntimeException("传入的bitmap为空");
		}
	}

	// 根据已经计算出来横纵比例的值处理后续图片
	public static Bitmap resizeBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			// 获取原生图片的宽高
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 矩阵对象
			Matrix matrix = new Matrix();
			// matrix.postScale(float x,float
			// y),注意中间的参数，如果x>1的话，那么就是放大，如果x<1的话，就是缩小
			// 对图片进行缩放
			matrix.postScale(Configs.scaleWidth, Configs.scaleHeight);
			// 根据原图安装缩放对象的比例进行一定缩放
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
					height, matrix, true);
			return resizedBitmap;
		} else {
			throw new RuntimeException("传入的bitmap为空");
		}
	}

}
