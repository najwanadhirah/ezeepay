package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.object.QPointTxObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShakeAndWinUI extends SRSBaseActivity implements SensorEventListener {


    SensorManager sm;
    Button btnSubmit;
    TextView tvInfo;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_shake_and_win_ui;
    }

    @Override
    protected void activityCreated() {
        getSupportActionBar().setTitle("QPAY99 SHAKE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvInfo = findViewById(R.id.tvInfo);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new addQTxLogAsync().execute();
            }
        });
    }

    @Override
    protected void initData() {
        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

//    @Override
//    protected void onDestroy() {
//        sm.unregisterListener(this);
//        super.onDestroy();
//
//    }

    @Override
    protected void onStop() {
        sm.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        DLog.e(TAG,"onSensorChanged ");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float value[] = event.values;
            float x = value[0];
            float y = value[1];
            float z = value[2];

            float movement_shake = ((x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH *
                    SensorManager.GRAVITY_EARTH));

            if (movement_shake >= 15) {
//                btn.setImageDrawable(getResources().getDrawable(R.drawable.kunfu2));
//                String winPoints = shakeWin();
//                DLog.e(TAG, "------------------" + winPoints);
//                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();

                DLog.e(TAG,"TEST-shakeWin " + shakeWin());

                long date = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = sdf.format(date);
                String lastShake = SharedPreferenceUtil.getShakeNwinTime();

                DLog.e(TAG, "lastShake " + lastShake);
                if (lastShake.equalsIgnoreCase(dateString)) {
                    DLog.e(TAG, "Please try again tomorrow");
                } else {
                    SharedPreferenceUtil.editShakeNwinTime(dateString);
                    new addQTxLogAsync().execute();
                }

//                if(Config.isDebug)
//                    new addQTxLogAsync().execute();

            }
        }
    }

    private String shakeWin() {
        int lucky_number = getRandomNumber(0, 15);
        try {


            DLog.e(TAG, "" + lucky_number);
//            if ((lucky_number == 8)
//                    ||(lucky_number == 18)
//                    ||(lucky_number == 28)
//                    ||(lucky_number == 38)
//                    ||(lucky_number == 48)
//                    ||(lucky_number == 58)
//                    ||(lucky_number == 68)
//                    ||(lucky_number == 78)
//                    ||(lucky_number == 88)
//                    ||(lucky_number == 98)
//                    ) {
//                return String.valueOf(lucky_number+100);
//            }

        } catch (Exception e) {
            return "10";
        }
        return String.valueOf(lucky_number);
    }

    private int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class addQTxLogAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Submiting....");
            pd.show();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();

            Toast.makeText(mContext,"Congratulation, you get " + aVoid + " QPoints",Toast.LENGTH_SHORT).show();
            tvInfo.setText("Congratulation, you get " + aVoid + " QPoints");
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = Config.QPointTx;
//            url = "http://localhost:54505/api/QPointTx";
            DLog.e(TAG, "url " + url);
            String shakePoint = shakeWin();
            Gson gson = new Gson();
            QPointTxObj obj = new QPointTxObj(SharedPreferenceUtil.getsClientID(), Integer.parseInt(shakePoint), SharedPreferenceUtil.getsClientUserName(), "SHAKE WIN", "ACTIVE");
            List<QPointTxObj> objs = new ArrayList<QPointTxObj>();
            objs.add(obj);
            String json = gson.toJson(objs);
            DLog.e(TAG, "json " + json);

            String r = httpPost(url, json);
            DLog.e(TAG, "" + r);
            return shakePoint;
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
            DLog.e(TAG, "httpPost result " + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e(TAG, "UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e(TAG, "IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
