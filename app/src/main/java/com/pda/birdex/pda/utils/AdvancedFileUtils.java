package com.pda.birdex.pda.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 高级的文件处理工具
 * 
 * @author bin.wang1
 * 
 */
public class AdvancedFileUtils {
	public static File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir

		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(
				context).getPath()
				: context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}
	/**
	 * Check if external storage is built-in or removable.
	 *
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= 9) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * Get the external app cache directory.
	 *
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	private static File getExternalCacheDir(Context context) {
		// if (BaseVersionUtils.hasFroyo()) {
		// return context.getExternalCacheDir();
		// }

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";


		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}
	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	/**
	 * 
	 * @param path 文件路径
	 * @param fileName 文件名，如xxx.jpg
	 * @param b
	 * @return 返回保存本地成功与否
	 */
	public static boolean saveBitmapToLocal(String path,String fileName,Bitmap b){
		if (b == null) {
			return false;
		}
		
		boolean result = false ;
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileOutputStream fos=null;
			
			try {
				fos = new FileOutputStream(path + fileName);
				b.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				result = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
								
		}	
		return result;
	}

}
