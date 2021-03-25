package com.rt.qpay99.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Base64;
import android.view.Display;
import android.widget.ImageView;

import com.rt.qpay99.Constants;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Helper.PhotoLoader;
import com.rt.qpay99.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageUtil {

	public static int setImages(String name,Context mContext) {
		int imgRes = 0;
		try {
			if (FunctionUtil.isSet(name)) {
				name = name.replace(".", "");
				name = name.replace("4", "");

				if (name.toLowerCase().indexOf("unifimobile") > -1)
					name = "unifimobile";


				if (name.toLowerCase().indexOf("alorstar") > -1)
					name = "mpalorstar";

				if (name.toLowerCase().indexOf("kotabharu") > -1)
					name = "mpkotabharu";

				if (name.toLowerCase().indexOf("selayang") > -1)
					name = "mpselayang";

				if (name.toLowerCase().indexOf("subangjaya") > -1)
					name = "mpsubangjaya";


				if (name.toLowerCase().indexOf("digi") > -1)
					name = "DIGI";
				if (name.toLowerCase().indexOf("celcom") > -1)
					name = "CELCOM";
				if (name.toLowerCase().indexOf("xox") > -1)
					name = "xox";
				if (name.toLowerCase().indexOf("umobile") > -1)
					name = "UMOBILE";
				if (name.toLowerCase().indexOf("tunetalk") > -1)
					name = "tunetalk";
				if (name.toLowerCase().indexOf("hotlink") > -1)
					name = "maxis";
				if (name.toLowerCase().indexOf("maxis") > -1)
					name = "maxis";
				if (name.toLowerCase().indexOf("hotlink") > -1)
					name = "maxis";
				if (name.toLowerCase().indexOf("airpenangbill") > -1)
					name = "PBAPPBILL";

				if (name.toLowerCase().indexOf("pulsaflexi") > -1)
					name = "idflexi";

				if (name.toLowerCase().indexOf("grab") > -1)
					name = "grabpin";

				if (name.toLowerCase().indexOf("friendi") > -1)
					name = "friendi";

				if (name.toLowerCase().indexOf("maxisbill") > -1)
					name = "maxisbill";

				if (name.toLowerCase().indexOf("onexox") > -1)
					name = "onexox";


				if (name.toLowerCase().indexOf("smartpas") > -1)
					name = "smartpas";

				if (name.toLowerCase().indexOf("webe") > -1)
					name = "webep1";

				if (name.toLowerCase().indexOf("as2in1mobile") > -1)
					name = "as2in1mobile";

				if (name.toLowerCase().indexOf("yespin") > -1)
					name = "yesbill";

				if (name.toLowerCase().indexOf("mcash") > -1)
					name = "mcashwallet";


				String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
				imgRes = mContext.getResources().getIdentifier(uri, null,
						mContext.getPackageName());
				if (imgRes == 0)
					return R.drawable.ic_no_image;
			} else
				return R.drawable.ic_no_image;

		} catch (Exception e) {
			return R.drawable.ic_no_image;
		}
		return imgRes;

	}


	public static void setLocalImage(final String mName ,final String firebaseId, final ImageView coverImageView,final Context mContext){
		Long expires = SharedPreferenceUtil.getSessionExpired();
		if (System.currentTimeMillis() < expires) {
			DLog.e(TAG, "get from SharedPreferenceUtil----------------->");
			String str_url = SharedPreferenceUtil.getSharedPreferencedValue(mName);
			File imgFile = new File(str_url);
			if (imgFile.exists()) {
				DLog.e(TAG, "imgFile.exists() ----------------->" + mName);
				DLog.e(TAG, "str_url " + str_url);
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				coverImageView.setImageBitmap(myBitmap);
				return;
			}
		}else{
			DLog.e(TAG,"-------------> downloadImagesAsync " );
			downloadImagesAsync(mName,firebaseId,coverImageView,mContext);
		}
	}

	public static void downloadImagesAsync(final String imgName ,final String firebaseId, final ImageView coverImageView,final Context mContext) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... voids) {
				HttpHandlerHelper sh = new HttpHandlerHelper();
				String url = "https://srs-mobile.firebaseio.com/BuyProduct/" + firebaseId + "/.json";
				DLog.e(TAG, "url : " + url);
				String jsonStr = sh.makeServiceCall(url);
				DLog.e(TAG, "Response from url: " + jsonStr);
				return jsonStr;
			}


			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				String imgURL = "";
				try {
					JSONObject json = new JSONObject(s);
					imgURL = json.getString("imgURL");
					DLog.e(TAG, "imgURL " + imgURL);
					Picasso.get()
							.load(imgURL)
							.into(new PhotoLoader(imgName, coverImageView));
					coverImageView.setAdjustViewBounds(true);
					SharedPreferenceUtil.editgetSessionExpired(System.currentTimeMillis() + 604800000);

				} catch (Exception e) {

				}
			}
		}.execute();
	}

	public static Drawable ResizeImage(int imageID, Context mContext) {
		// Get device dimensions
		Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
		double deviceWidth = display.getWidth();

		BitmapDrawable bd = (BitmapDrawable) mContext.getResources().getDrawable(
				imageID);
		double imageHeight = bd.getBitmap().getHeight();
		double imageWidth = bd.getBitmap().getWidth();

		double ratio = deviceWidth / imageWidth;
		int newImageHeight = (int) (imageHeight * ratio);

		Bitmap bMap = BitmapFactory.decodeResource( mContext.getResources(), imageID);
		Drawable drawable = new BitmapDrawable(mContext.getResources(),
				getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

		return drawable;
	}

	public static void shareBitmap(Context mContext, Bitmap bitmap, String fileName) {
		try {
			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			DLog.e(TAG,"SDK_INT " + Build.VERSION.SDK_INT);
			DLog.e(TAG,"LOLLIPOP_MR1 " + Build.VERSION_CODES.LOLLIPOP_MR1);

			File file = new File(mContext.getCacheDir(), "images");
			file.mkdirs(); // don't forget to make the directory
			FileOutputStream stream = new FileOutputStream(file + fileName); // overwrites this image every time
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			stream.close();
			DLog.e(TAG,"FileProvider2");
			File imagePath = new File(mContext.getCacheDir(), "images");
			File newFile = new File(imagePath, fileName);
			Uri contentUri = FileProvider.getUriForFile(mContext, "com.rt.srs.mobile.fileprovider", newFile);

			if (contentUri != null) {
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				shareIntent.setDataAndType(contentUri, mContext.getContentResolver().getType(contentUri));
				shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
				mContext.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
			}

		} catch (Exception e) {
			DLog.e(TAG,"" + e.getMessage());
			e.printStackTrace();
		}

	}

	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API19(Context context, Uri uri){
		String filePath = "";
		String wholeID = DocumentsContract.getDocumentId(uri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				column, sel, new String[]{ id }, null);

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}
		cursor.close();
		return filePath;
	}


	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(
				context,
				contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		if(cursor != null){
			int column_index =
					cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;
	}

	public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index
				= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	private Bitmap decodeUri(Uri selectedImage,Context mContext) throws FileNotFoundException {

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImage), null, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 140;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE
					|| height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImage), null, o2);

	}

	private static String TAG = "ImageUtil";

	public static String getColorCode(String color) {
		String colorCode = "#fff";

		switch (color.toLowerCase()) {
			case "black":
				colorCode = "#000";
				break;
			case "silver":
				colorCode = "#C0C0C0";
				break;

			case "space gray":
				colorCode = "#696969";
				break;

			case "rose gold":
				colorCode = "#E6BE8A";
				break;

			case "gold":
				colorCode = "#FFDF00";
				break;

			case "jet black":
				colorCode = "#000";
				break;

			case "matt gold":
				colorCode = "#D4AF37";
				break;

			case "green":
				colorCode = "#008000";
				break;
			case "pink":
				colorCode = "#FFC0CB";
				break;

			case "yellow":
				colorCode = "#FFFF00";
				break;

			case "blue":
				colorCode = "#0000FF";
				break;

			default:
				colorCode = "#fff";
		}


		return colorCode;
	}

	public static int getCircleColor(String color){

		DLog.e(TAG,"color " + color );
		if (color.toLowerCase().equalsIgnoreCase("white")) {
			return R.drawable.circle_color_white;
		}

		if (color.toLowerCase().equalsIgnoreCase("space gray")) {
			return R.drawable.circle_color_sparygray;
		}

		if (color.toLowerCase().equalsIgnoreCase("rose gold")) {
			return R.drawable.circle_color_rosegold;
		}

		if (color.toLowerCase().equalsIgnoreCase("gold")) {
			return R.drawable.circle_color_gold;
		}

		if (color.toLowerCase().equalsIgnoreCase("jet black")) {
			return R.drawable.circle_color_jetblack;
		}

		if (color.toLowerCase().equalsIgnoreCase("matt gold")) {
			return R.drawable.circle_color_mattgold;
		}

		if (color.toLowerCase().equalsIgnoreCase("green")) {
			return R.drawable.circle_color_green;
		}

		if (color.toLowerCase().equalsIgnoreCase("pink")) {
			return R.drawable.circle_color_pink;
		}

		if (color.toLowerCase().equalsIgnoreCase("yellow")) {
			return R.drawable.circle_color_yellow;
		}

		if (color.toLowerCase().equalsIgnoreCase("silver")) {
			return R.drawable.circle_color_silver;
		}

		if (color.toLowerCase().equalsIgnoreCase("black")) {
			return R.drawable.circle_color_black;
		}

		if (color.toLowerCase().equalsIgnoreCase("blue")) {
			return R.drawable.circle_color_blue;
		}

		return R.drawable.circle_color_white;
	}


	public static int setProductImages(String name, String mCategory, Context mContext) {
		DLog.e(TAG, "setProductImages ---------------------->" + name);
		int imgRes = 0;

		String e_name = name;
		try {
			if (FunctionUtil.isSet(name)) {
				name = name.replace(".", "");
				name = name.replace("4", "");

				if (e_name.toLowerCase().indexOf("pulsaflexi") > -1)
					name = "idflexi";

				if (name.toLowerCase().indexOf("yes") > -1) {
					name = "yesbill";
				}

				if (name.toLowerCase().indexOf("mol") > -1) {
					name = "razerpay";
				}

				if (name.toLowerCase().indexOf("molpin") > -1) {
					name = "razerpay";
				}

				if (name.toLowerCase().indexOf("razer") > -1) {
					name = "razerpay";
				}

				if (name.toLowerCase().indexOf("unifimobile") > -1)
					name = "unifimobile";

				if (name.toLowerCase().indexOf("alorstar") > -1)
					name = "mpalorstar";

				if (name.toLowerCase().indexOf("kotabharu") > -1)
					name = "mpkotabharu";

				if (name.toLowerCase().indexOf("selayang") > -1)
					name = "mpselayang";

				if (name.toLowerCase().indexOf("subangjaya") > -1)
					name = "mpsubangjaya";

				if (name.toLowerCase().indexOf("digi") > -1)
					name = "DIGI";
				if (name.toLowerCase().indexOf("celcom") > -1)
					name = "CELCOM";
				if (name.toLowerCase().indexOf("xox") > -1)
					name = "xox";
				if (name.toLowerCase().indexOf("umobile") > -1)
					name = "UMOBILE";
				if (name.toLowerCase().indexOf("tunetalk") > -1)
					name = "tunetalk";

				if (name.toLowerCase().indexOf("maxis") > -1)
					name = "maxis";

				if (name.toLowerCase().indexOf("hotlink") > -1)
					name = "maxis";

				if (name.toLowerCase().indexOf("airpenangbill") > -1)
					name = "PBAPPBILL";

				if (name.toLowerCase().indexOf("pulsa") > -1)
					name = "idflexi";

				if (name.toLowerCase().indexOf("grab") > -1)
					name = "grabpin";

				if (name.toLowerCase().indexOf("friendi") > -1)
					name = "friendi";

				if (name.toLowerCase().indexOf("maxisbill") > -1)
					name = "maxisbill";

				if (name.toLowerCase().indexOf("smartpas") > -1)
					name = "smartpas";

				if (name.toLowerCase().indexOf("webe") > -1)
					name = "webep1";

				if (name.toLowerCase().indexOf("as2in1mobile") > -1)
					name = "as2in1mobile";

				if (name.toLowerCase().indexOf("yespin") > -1)
					name = "yesbill";

				if (name.toLowerCase().indexOf("altel") > -1)
					name = "altel";

				if (name.toLowerCase().indexOf("mcash") > -1)
					name = "mcashwallet";

				if (name.toLowerCase().indexOf("redone") > -1)
					name = "redone";

				if (e_name.toLowerCase().indexOf("onexox") > -1)
					name = "onexox";

				if (e_name.toLowerCase().indexOf("hotlinkshare") > -1)
					name = "hotlinkshare";

				if (e_name.toLowerCase().indexOf("maxisbill") > -1)
					name = "maxisbill";
				if (e_name.toLowerCase().indexOf("maxisonebill") > -1)
					name = "maxisbill";

				if (e_name.toLowerCase().indexOf("grabdriver") > -1)
					name = "grabdriver";



				if (name.toLowerCase().indexOf("indonesia") > -1)
					name = "idflexi";

				if (name.toLowerCase().indexOf("nepal") > -1)
					name = "npflexi";

				if (name.toLowerCase().indexOf("bangladesh") > -1)
					name = "bdflexi";

				if (name.toLowerCase().indexOf("india") > -1)
					name = "inflexi";

				if (name.toLowerCase().indexOf("myanmar") > -1)
					name = "mmflexi";

				if (name.toLowerCase().indexOf("srsprinter") > -1)
					name = "srsprinter";

				if (name.toLowerCase().indexOf("printer") > -1)
					name = "srsprinter";

				if (name.toLowerCase().indexOf("steamwallet") > -1)
					name = "steam";

				if (name.toLowerCase().indexOf("steam") > -1)
					name = "steam";

				if (name.toLowerCase().indexOf("playstation") > -1)
					name = "playstation";

				if (name.toLowerCase().indexOf("garena") > -1)
					name = "garena";

				if (name.toLowerCase().indexOf("berrypoint") > -1)
					name = "berrypoints";

				if (name.toLowerCase().indexOf("acash") > -1)
					name = "acash";

				if (name.toLowerCase().indexOf("mycard") > -1)
					name = "mycard";

				if (name.toLowerCase().indexOf("offgamers") > -1)
					name = "offgamers";

//                if (name.toLowerCase().indexOf("aeoncs") > -1)
//                    name = "aeoncreditservice";

				String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
				DLog.e(TAG, "uri " + uri);
				if (mCategory.equalsIgnoreCase(Constants.IMG_TOPUP)) {
					uri = "drawable/ic_" + name.toString().toLowerCase() + "_w_icon";
					imgRes = mContext.getResources().getIdentifier(uri, null,
							mContext.getPackageName());
					if (imgRes == 0) {
						uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
					}
				}

				imgRes = mContext.getResources().getIdentifier(uri, null,
						mContext.getPackageName());
				if (imgRes == 0)
					return R.drawable.ic_no_image;
			} else
				return R.drawable.ic_no_image;


		} catch (Exception e) {
			return R.drawable.ic_no_image;
		}
		return imgRes;

	}
	public static int setPhoneImages(String phoneModal, String ColorCode, Context mContext) {
		try {
			String name = phoneModal.replace(" ", "");
			ColorCode = ColorCode.replace(" ", "");


			DLog.e(TAG, "name " + name);
			DLog.e(TAG, "ColorCode " + ColorCode);

			String uri = "drawable/" + name.toLowerCase().replace("plus", "") + "_" + ColorCode.toLowerCase();

			DLog.e(TAG, "------" + uri);
			int imgRes = mContext.getResources().getIdentifier(uri, null,
					mContext.getPackageName());

			DLog.e(TAG, "imgRes " + imgRes);

			if (imgRes == 0) {
				if (name.equalsIgnoreCase("iPhone5s")) {
					DLog.e(TAG, "Default iphone5s_white ");
					return R.drawable.iphone5s_white;
				}


				if (name.equalsIgnoreCase("iPhone5")) {
					DLog.e(TAG, "Default iphone5s_white ");
					return R.drawable.iphone5s_white;
				}


				if (name.equalsIgnoreCase("iPhone6")) {
					DLog.e(TAG, "Default iphone6_gold ");
					return R.drawable.iphone6_gold;
				}


				if (name.equalsIgnoreCase("iPhone6s")) {
					DLog.e(TAG, "Default iphone6s_gold ");
					return R.drawable.iphone6s_gold;
				}


				if (name.equalsIgnoreCase("iPhone4s")) {
					DLog.e(TAG, "Default iphone4s_black ");
					return R.drawable.iphone4s_black;
				}


				if (name.equalsIgnoreCase("iPhone5c")) {
					DLog.e(TAG, "Default iphone5c_white ");
					return R.drawable.iphone5c_white;
				}


				if (name.toLowerCase().indexOf("sony") > -1) {
					DLog.e(TAG, "Default sonyxperiaz3_black ");
					return R.drawable.sonyxperiaz3_black;
				}

				if (name.toLowerCase().indexOf("oneplus") > -1) {
					DLog.e(TAG, "Default OnePlus 5T ");
					return R.drawable.oneplus5t;
				}


				imgRes = R.drawable.iphone6s_gold;
			}

			return imgRes;
		} catch (Exception e) {
			return R.drawable.iphone7p_silver;
		}
	}

	public static InputStream Bitmap2IS(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();
		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();

		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;
	}

	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();
		float bitmapRatio = width / height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
														 int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}


}
