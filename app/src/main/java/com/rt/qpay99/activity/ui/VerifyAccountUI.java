package com.rt.qpay99.activity.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.R;
import com.rt.qpay99.innerPrinterUtil.BitmapUtil;
import com.rt.qpay99.object.AgentInfoObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;
import com.watermark.androidwm.bean.WatermarkText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VerifyAccountUI extends SRSBaseActivity {

    static final int REQUEST_IMAGE_CAPTURE_BACKIC = 1;
    static final int REQUEST_IMAGE_CAPTURE_FRONTIC = 2;
    static final int REQUEST_IMAGE_CAPTURE_SELFIE = 12;
    private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";
    private static int REQ_AFTER_CROP_IMAGE_BACKIC = 3;
    private static int REQ_AFTER_CROP_IMAGE_FRONTIC = 4;
    private static int REQ_AFTER_CROP_IMAGE_SELFIE = 14;
    private static int REQ_CODE_PICK_IMAGE = 998;
    private static int REQ_CODE_CROP_IMAGE = 997;
    private static int CAMERA_PIC_REQUEST = 999;
    Button btnSumit;
    ImageView frontIC, backIC, imgSelfie;
    Uri photoURI;
    String photoURIPathF = "", photoURIPathB = "", photoURISelfie = "";
    Button btnFrontIC, btnBackIC, btnselfie;
    AgentInfoObj obj = new AgentInfoObj();
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference mountainsRef;
    StorageReference mountainImagesRef;
    String folderName;
    File photoFileFic = null;
    File photoFileBic = null;
    File photoFileSelfie = null;
    String mCurrentPhotoPath;
    ProgressBar pBar;
    File mCropFrontIC, mCropBackIC, mCropSELFIE;
    Uri cropBuri = null;
    Uri cropFuri = null;
    boolean isBackICCrop, isFrontICCRop, isSelfieCrop;
    WatermarkText watermarkText;
    private ProgressDialog pd;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_verify_account_ui;
    }

    @Override
    protected void activityCreated() {
        mContext = this;
        TAG = this.getClass().getName();
        Toolbar toolbar = findViewById(R.id.tool_bar);
//        getSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        pBar = findViewById(R.id.progress_bar);
        pBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleInverse);
        pBar.setVisibility(ProgressBar.VISIBLE);
        frontIC = findViewById(R.id.frontIC);
        backIC = findViewById(R.id.backIC);
        imgSelfie = findViewById(R.id.imgSelfie);
        btnFrontIC = findViewById(R.id.btnFrontIC);
        btnBackIC = findViewById(R.id.btnBackIc);
        btnFrontIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            isFrontICCRop = false;
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                try {
                                    photoFileFic = createImageFile();

                                    mCropFrontIC = File.createTempFile(
                                            "CROPFRontIC" + "",  /* prefix */
                                            ".jpg",         /* suffix */
                                            getFilesDir()      /* directory */
                                    );
                                    photoURIPathF = photoFileFic.getAbsolutePath();

                                } catch (IOException ex) {
                                    DLog.e(TAG, "btnFrontIC Err" + ex.getMessage());
                                }
                                if (photoFileFic != null) {
                                    photoURI = FileProvider.getUriForFile(mContext,
                                            "rob.payezqp.com.fileprovider",
                                            photoFileFic);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_FRONTIC);
                                }
                            }
                        } catch (ActivityNotFoundException anfe) {
                            Toast toast = Toast.makeText(mContext, "This device doesn't support the crop action!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.PERMISSIONS_REQUEST_CAMERA);  // Comment 26
                        System.out.println("go to requestPermissions");
                        DLog.e(TAG,"go to requestPermissions" );
                    }
                }
            }
        });

        btnBackIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            isBackICCrop = false;
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                try {
                                    photoFileBic = createImageFile();
                                    photoURIPathB = photoFileBic.getAbsolutePath();
                                    mCropBackIC = File.createTempFile(
                                            "CROPBackIC" + "",  /* prefix */
                                            ".jpg",         /* suffix */
                                            getFilesDir()      /* directory */
                                    );
                                } catch (IOException ex) {
                                    DLog.e(TAG, "" + ex.getMessage());
                                }
                                if (photoFileBic != null) {
                                    photoURI = FileProvider.getUriForFile(mContext,
                                            "rob.payezqp.com.fileprovider",
                                            photoFileBic);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    DLog.e(TAG, "=================" + photoURI);
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_BACKIC);
                                }
                            }
                        } catch (ActivityNotFoundException anfe) {
                            Toast toast = Toast.makeText(mContext, "This device doesn't support the crop action!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.PERMISSIONS_REQUEST_CAMERA);  // Comment 26
                        System.out.println("go to requestPermissions");
                        DLog.e(TAG, "go to requestPermissions");
                    }
                }

            }
        });
        btnselfie = findViewById(R.id.btnselfie);
        btnselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            isSelfieCrop = false;
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                try {
                                    photoFileSelfie = createImageFile();
                                    photoURISelfie = photoFileSelfie.getAbsolutePath();
                                    mCropSELFIE = File.createTempFile(
                                            "mCropSELFIE" + "",  /* prefix */
                                            ".jpg",         /* suffix */
                                            getFilesDir()      /* directory */
                                    );
                                } catch (IOException ex) {
                                    DLog.e(TAG, "" + ex.getMessage());
                                }
                                if (photoFileSelfie != null) {
                                    photoURI = FileProvider.getUriForFile(mContext,
                                            "rob.payezqp.com.fileprovider",
                                            photoFileSelfie);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    DLog.e(TAG, "=================" + photoURI);
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_SELFIE);
                                }
                            }
                        } catch (ActivityNotFoundException anfe) {
                            Toast toast = Toast.makeText(mContext, "This device doesn't support the crop action!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.PERMISSIONS_REQUEST_CAMERA);  // Comment 26
                        System.out.println("go to requestPermissions");
                        DLog.e(TAG, "go to requestPermissions");
                    }
                }


            }
        });
        btnSumit = findViewById(R.id.btnSubmit);
        btnSumit.setVisibility(View.VISIBLE);
        if (SharedPreferenceUtil.getAccountStatus().equals("SUCCESS")) {
            btnBackIC.setEnabled(false);
            btnFrontIC.setEnabled(false);
            frontIC.setEnabled(false);
            backIC.setEnabled(false);
            btnSumit.setEnabled(false);
            btnSumit.setVisibility(View.INVISIBLE);
        }

        btnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePhotoAsyc();
            }
        });

    }

    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                setDialog("CAMERA Permission", "App require this permission to continue.", false);
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, Constants.PERMISSIONS_REQUEST_CAMERA
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Change Permissions in Settings");
                    alertDialogBuilder
                            .setMessage("" +
                                    "\nClick SETTINGS to Manually Set\n" + "Permissions to continue.")
                            .setCancelable(false)
                            .setNegativeButton("CANCEL", null)
                            .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 1000);     // Comment 3.
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//                        alertDialogBuilder.setTitle("Second Chance");
//                        alertDialogBuilder
//                                .setMessage("Click RETRY to Set Permissions to Allow\n\n" + "Click EXIT to the Close App")
//                                .setCancelable(false)
//                                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        //ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Integer.parseInt(WRITE_EXTERNAL_STORAGE));
//                                        Intent i = new Intent(MainActivity.this, MainActivity.class);
//                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(i);
//                                    }
//                                })
//                                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        finish();
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                    }
                }
                break;

            case Constants.PERMISSIONS_REQUEST_LOCATION:
                break;


        }
    }

    public String httpPost(String url, String data) {
        HttpURLConnection httpcon;
        String result = null;
        try {
            //Connect
            httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            DLog.e(TAG, "" + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e(TAG, "UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e(TAG, "IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void initData() {
        DLog.e(TAG, "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT);
        DLog.e(TAG, " CHECKING INSIDE INIT DATA" );


        watermarkText = new WatermarkText(getString(R.string.app_name) + " USE ONLY")
                .setPositionX(0.5)
                .setPositionY(0.5)
                .setTextColor(Color.WHITE)
                .setTextShadow(0.1f, 5, 5, Color.GREEN)
                .setTextAlpha(150)
                .setRotation(30)
                .setTextSize(20);


        isBackICCrop = false;
        isFrontICCRop = false;

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        folderName = "QPAY99" + "_" + SharedPreferenceUtil.getsClientID() + "_" +
                SharedPreferenceUtil.getsClientUserName();
        mountainsRef = storageRef.child("SRSNRIC");
        mountainsRef.child(folderName + "/BackIC.jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DLog.e(TAG, "BackIC uri " + uri.toString());
                        Picasso.get()
                                .load(uri).resize(100,100)
                                .into(backIC);

                    }
                });

        mountainsRef.child(folderName + "/Selfie.jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DLog.e(TAG, "uri " + uri.toString());
                        Picasso.get()
                                .load(uri)
                                .into(imgSelfie);

                    }
                });

        mountainsRef.child(folderName + "/FrontIC.jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DLog.e(TAG, "uri " + uri.toString());
                        Picasso.get()
                                .load(uri)
                                .into(frontIC);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_SELFIE && resultCode == Activity.RESULT_OK) {
            isSelfieCrop = true;
            try {
                DLog.e(TAG, "REQUEST_IMAGE_CAPTURE_SELFIE 1" + photoFileSelfie);
//                Uri oriFIle = FileProvider.getUriForFile(mContext,
//                        "my.srs.topup.fileprovider",
//                        photoFileSelfie);

                File imgFile = new File(photoFileSelfie.getAbsolutePath());
                DLog.e(TAG, "imgFile " + imgFile);

                if (imgFile.exists()) {
                    DLog.e(TAG, "imgFile.exists()");
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Bitmap b = BitmapUtil.Watermark(mContext, myBitmap, getString(R.string.app_name) + " USE ONLY");
                    imgSelfie.setImageBitmap(b);

                    FileOutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(photoFileSelfie);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        try {
                            outStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

//                cropCapturedImage(oriFIle, photoFileFic, REQ_AFTER_CROP_IMAGE_FRONTIC);
            } catch (ActivityNotFoundException aNFE) {
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


        if (requestCode == REQUEST_IMAGE_CAPTURE_FRONTIC && resultCode == Activity.RESULT_OK) {
            isFrontICCRop = true;
            try {
                DLog.e(TAG, "REQUEST_IMAGE_CAPTURE_FRONTIC " + photoFileFic);
                DLog.e(TAG, "REQUEST_IMAGE_CAPTURE_FRONTIC b " + photoFileFic.getAbsolutePath());
                Uri oriFIle = FileProvider.getUriForFile(mContext,
                        "rob.payezqp.com.fileprovider",
                        photoFileFic);

                File imgFile = new File(photoFileFic.getAbsolutePath());

                if (imgFile.exists()) {
                    DLog.e(TAG, "imgFile.exists()");

//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.RGB_565;
//                    options.inJustDecodeBounds = true;
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    Bitmap b = BitmapUtil.Watermark(mContext, myBitmap, getString(R.string.app_name) + " USE ONLY");
                    frontIC.setImageBitmap(b);

                    FileOutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(photoFileFic);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        try {
                            outStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

//                cropCapturedImage(oriFIle, photoFileFic, REQ_AFTER_CROP_IMAGE_SELFIE);
            } catch (ActivityNotFoundException aNFE) {
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE_BACKIC && resultCode == Activity.RESULT_OK) {
            isBackICCrop = true;
            try {
                DLog.e(TAG, "REQUEST_IMAGE_CAPTURE_BACKIC 1 " + photoFileBic);
                Uri oriFIle = FileProvider.getUriForFile(mContext,
                        "rob.payezqp.com.fileprovider",
                        photoFileBic);

                File imgFile = new File(photoFileBic.getAbsolutePath());

                if (imgFile.exists()) {
                    DLog.e(TAG, "imgFile.exists()");
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Bitmap b = BitmapUtil.Watermark(mContext, myBitmap, getString(R.string.app_name) + " USE ONLY");
                    backIC.setImageBitmap(b);

                    FileOutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(photoFileBic);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        try {
                            outStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

//                cropCapturedImage(oriFIle, photoFileBic, REQ_AFTER_CROP_IMAGE_BACKIC);
            } catch (ActivityNotFoundException aNFE) {
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


        if (requestCode == REQ_AFTER_CROP_IMAGE_FRONTIC && resultCode == Activity.RESULT_OK) {
            DLog.e(TAG, "Image CROP 1 ==" + mCropFrontIC);
            DLog.e(TAG, "Image CROP 2 ==" + cropFuri);
            Picasso.get()
                    .load(mCropFrontIC)
                    .fit()
                    .into(frontIC);
            isFrontICCRop = true;
        }

        if (requestCode == REQ_AFTER_CROP_IMAGE_BACKIC && resultCode == Activity.RESULT_OK) {
            DLog.e(TAG, "Image CROP 1 ==" + mCropBackIC);
            DLog.e(TAG, "Image CROP 2 ==" + cropBuri);
            Picasso.get()
                    .load(mCropBackIC)
                    .fit()
                    .into(backIC);
            isBackICCrop = true;
        }

        if (requestCode == REQ_AFTER_CROP_IMAGE_SELFIE && resultCode == Activity.RESULT_OK) {
            DLog.e(TAG, "Image CROP 1 ==" + mCropSELFIE);
            DLog.e(TAG, "Image CROP 2 ==" + cropBuri);
            Picasso.get()
                    .load(mCropSELFIE)
                    .fit()
                    .into(backIC);
            isBackICCrop = true;
        }

        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            DLog.e(TAG, "Image CROP==");
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getFilesDir();
        DLog.e(TAG, "==================" + storageDir);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();


        DLog.e(TAG, "mCurrentPhotoPath " + mCurrentPhotoPath);
        return image;
    }

    public void cropCapturedImage(Uri picUri, File mFile, int CropRequest) {

        DLog.e(TAG, "cropCapturedImage2 " + picUri.toString());
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("return-data", true);


        cropIntent.setDataAndType(picUri, "image/*");
        Uri cropuri = null;
        if (CropRequest == REQ_AFTER_CROP_IMAGE_FRONTIC) {
            cropFuri = FileProvider.getUriForFile(mContext,
                    "rob.payezqp.com..fileprovider",
                    mCropFrontIC);
            cropuri = cropFuri;
            DLog.e(TAG, "cropFuri " + cropFuri);

        } else if (CropRequest == REQ_AFTER_CROP_IMAGE_BACKIC) {
            cropBuri = FileProvider.getUriForFile(mContext,
                    "rob.payezqp.com..fileprovider",
                    mCropBackIC);
            cropuri = cropBuri;
            DLog.e(TAG, "cropBuri " + cropBuri);
        }
//        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropuri);
//        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(cropIntent, PackageManager.MATCH_DEFAULT_ONLY);
//        for (ResolveInfo resolveInfo : resInfoList) {
//            String packageName = resolveInfo.activityInfo.packageName;
//            grantUriPermission(packageName, cropuri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
//
//        startActivityForResult(cropIntent, CropRequest);

        DLog.e(TAG, "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
            DLog.e(TAG, "FLAG_GRANT_WRITE_URI_PERMISSION ");
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropuri);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(cropIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, cropuri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

        } else {
            photoURI = Uri.fromFile(mFile);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        }
        startActivityForResult(cropIntent, CropRequest);

    }

    protected int byteSizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return data.getByteCount();
        } else {
            return data.getAllocationByteCount();
        }
    }

    private void UpdatePhotoAsyc() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext);
                SpannableString ss1 = new SpannableString("Please wait ...");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                        ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                        0, ss1.length(), 0);
                pd = new ProgressDialog(mContext);
                pd.setTitle(ss1);
                pd.setMessage("Uploading ...");
                pd.setCancelable(false);
                pd.show();

            }

            @Override
            protected Boolean doInBackground(Void... params) {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                DLog.e(TAG, "" + timeStamp);
                DLog.e(TAG, "photoURIPathB " + photoURIPathB);
//                if (isFrontICCRop)
//                    uploadPhoto(mCropFrontIC.getAbsolutePath(), "FrontIC", timeStamp);
//                if (isBackICCrop)
//                    uploadPhoto(mCropBackIC.getAbsolutePath(), "BackIC", timeStamp);

                if (isFrontICCRop)
                    uploadPhoto(photoFileFic.getAbsolutePath(), "FrontIC", timeStamp);
                if (isBackICCrop)
                    uploadPhoto(photoFileBic.getAbsolutePath(), "BackIC", timeStamp);


                if (isSelfieCrop)
                    uploadPhoto(photoFileSelfie.getAbsolutePath(), "Selfie", timeStamp);


                if (!isFrontICCRop && !isBackICCrop && !isSelfieCrop) {
//                    Toast.makeText(mContext, "Please take a photo to proceed.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                pd = null;
                if (s) {
                    obj.setAgentID(0);
                    obj.setName(SharedPreferenceUtil.getMerchantName());
                    obj.setHPNo(SharedPreferenceUtil.getsClientUserName());
                    obj.setState("");
                    obj.setStatus("INPROGRESS");
                    obj.setDistrict("");
                    obj.setPrefixFilter("");
                    obj.setParentAgentID(SharedPreferenceUtil.getsClientID());
                    obj.setBanner("");
                    obj.setCreatedBy(SharedPreferenceUtil.getServerKey());
                    obj.setCreatedDate("");
                    obj.setLastModifiedBy("");
                    obj.setLastModifiedDate("");
                    obj.setAddress("");
                    obj.setEmail("");
                    obj.setLocation("");
                    new postDataAsync().execute();
                } else {
                    Toast.makeText(mContext, "Please take a photo to proceed.", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    void uploadPhoto(String filepath, String fileName, String timeStamp) {


        DLog.e(TAG, "FirebaseStorage.getInstance()");
        // Create a reference to "mountains.jpg"

        mountainImagesRef = storageRef.child(folderName + "/" + fileName + ".jpg");

        // While the file names are the same, the
        // references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        try {
            DLog.e(TAG, "file name - " + folderName + "/" + fileName + ".jpg");
            DLog.e(TAG, "filepath - " + filepath);

            InputStream stream = new FileInputStream(new File(filepath));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap original = BitmapFactory.decodeStream(stream, null, options);

//            Bitmap original = BitmapFactory.decodeStream(stream);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 30, out);
            byte[] data = out.toByteArray();


            DLog.e(TAG, "file name - " + folderName + "/" + fileName + ".jpg");
            UploadTask uploadTask = mountainsRef.child(folderName + "/" + fileName + ".jpg").putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    DLog.e(TAG, "onFailure==============>");

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    DLog.e(TAG, "=========>" + taskSnapshot.getMetadata().getName());
                }
            });


            mountainsRef.child(folderName + "/" + fileName + ".jpg").getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DLog.e(TAG, "uri " + uri.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        } catch (FileNotFoundException fe) {
            DLog.e(TAG, "" + fe.getMessage());
        }


    }

    private class CheckLoginValidateAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            SharedPreferenceUtil.editAccountStatus(aVoid);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = Config.AZUREMAIN_URL + Config.AGENT_URL;
            HttpHandlerHelper sh = new HttpHandlerHelper();

            DLog.e(TAG, "url : " + url);
            String jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr);

            String result = "";
            try {
                JSONObject json = new JSONObject(jsonStr);
                JSONObject j = json.getJSONObject("Data");
                DLog.e(TAG, "Status -------------->" + j.getString("Status"));
                return j.getString("Status");


            } catch (Exception e) {

            }


            return result;
        }
    }

    private class postDataAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(mContext, "Image Uplaoded", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = Config.AZUREMAIN_URL + Config.AGENT_URL;
            DLog.e(TAG, "url " + url);
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            DLog.e(TAG, "json " + json);
            String r = httpPost(url, json);
            DLog.e(TAG, "" + r);
            return r;
        }
    }


}