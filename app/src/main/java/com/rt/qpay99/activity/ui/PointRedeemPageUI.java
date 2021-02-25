package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.PointRedeemRecycleAdapter;
import com.rt.qpay99.object.RedeemObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PointRedeemPageUI extends SRSBaseActivity {

    RecyclerView recRedeemList;

    List<RedeemObj> redeems = new ArrayList<>();
    RedeemObj obj;

    TextView tvInfo;
    String jsonStr;


    @Override
    protected void onResume() {
        super.onResume();
        new getAgentQPoints().execute();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_point_redeem_page_ui;
    }

    @Override
    protected void activityCreated() {
        mContext = this;
        recRedeemList = findViewById(R.id.recRedeemList);
        recRedeemList.setItemAnimator(new DefaultItemAnimator());
        recRedeemList.setLayoutManager(new LinearLayoutManager(mContext ));
        getSupportActionBar().setTitle("QPoints Redemptions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvInfo = findViewById(R.id.tvInfo);

    }

    @Override
    protected void initData() {

//        new getAgentQPoints().execute();
        new getRedeemList().execute();

    }


    ProgressDialog pd;
    RedeemObj product;
    String mCategory = "QPOINTS";
    private class getRedeemList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Get list....");
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (redeems.size() > 0) {
                recRedeemList.setAdapter(new PointRedeemRecycleAdapter(mContext,new mRecyclerViewOnItemClick(),redeems));
            }

            if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.QPoint_REDEEMPRODUCT + mCategory;
//            url = "http://localhost:54505/api/Productlist/ProductRedeem/" + mCategory;
            DLog.e(TAG, "url: " + url);
            String jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr);

            try {
                JSONObject json = new JSONObject(jsonStr);
                JSONArray cast = json.getJSONArray("Data");
                DLog.e(TAG, "" + cast.toString());
                for (int i = 0; i < cast.length(); i++) {
                    product = new RedeemObj();
                    JSONObject js = cast.getJSONObject(i);
                    try{
                        DLog.e(TAG, "pId " + js.getInt("ProductID"));
                        DLog.e(TAG, "Name " + js.getString("Name"));
                        DLog.e(TAG, "Description " + js.getString("Description"));
                        DLog.e(TAG, "RedeemPoints " + js.getInt("RedeemPoints"));
                        DLog.e(TAG, "PromoDiscount " + js.getInt("PromoDiscount"));
                        DLog.e(TAG, "Status " + js.getString("Status"));
                        DLog.e(TAG, "isPromo " + js.getInt("isPromo"));
                        DLog.e(TAG, "FirebaseDBId " + js.getString("FirebaseDBId"));
                        DLog.e(TAG, "Category " + js.getString("Category"));
                        DLog.e(TAG, "ProductURL " + js.getString("ProductURL"));


                        product.setmProductId(js.getInt("ProductID"));
                        product.setRedeemPoints(js.getInt("RedeemPoints"));
                        product.setPromoDiscount(js.getInt("PromoDiscount"));
                        product.setIsPromo(js.getInt("isPromo"));

                        product.setmName(js.getString("Name"));
                        product.setDescription(js.getString("Description"));
                        product.setStatus(js.getString("Status"));
                        product.setFirebaseDBId(js.getString("FirebaseDBId"));
                        product.setCategory(js.getString("Category"));
                        product.setProductURL(js.getString("ProductURL"));

                        redeems.add(product);

                    }catch (Exception e){
                        Toast.makeText(mContext,"Error occured. Please try again later!",Toast.LENGTH_SHORT).show();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class getAgentQPoints extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Loading...");
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            tvInfo.setText(aVoid);

        }

        @Override
        protected String doInBackground(Void... voids) {
            String Id =  FunctionUtil.getsEncK("QP@99_QP01nt" + SharedPreferenceUtil.getsClientID() );
            DLog.e(TAG,"qKey " + Id);
            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.AgentQPointsURL + Id + "/"  + SharedPreferenceUtil.getsClientID();
            DLog.e(TAG, "url: " + url);
             jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr.toString());

            jsonStr = jsonStr.replace("\"","");
            jsonStr = jsonStr.replace("\n","");

            return jsonStr  + " QPoints";
        }
    }

    public String  createEncryptedKey() {
        try{

            String SaltKey = "qp@ay&KEY";
            byte[] salt = SaltKey.getBytes();


            String encryptKey = "QPoint_AgentID_4";
//            byte[] salt = new byte[]{0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76};
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(encryptKey.toCharArray(), salt, 1024, 128);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            System.out.println("qKey:-----------------------" + Base64.encodeToString(secret.getEncoded(), Base64.DEFAULT));

            String cleartext = "47d7xxfEFNAJ/RJ8mdUMAQ==";
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] ciphertext = cipher.doFinal(cleartext.getBytes("UTF-8"));
            System.out.println("qKey IV:" + Base64.encodeToString(iv, Base64.DEFAULT));
            System.out.println("qKey Cipher text:" + Base64.encodeToString(ciphertext, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }


    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            DLog.e(TAG, "mRecyclerViewOnItemClick");
//            Toast.makeText(mContext,"Coming soon",Toast.LENGTH_SHORT).show();

           try{
               RedeemObj o = redeems.get(position);

               if(o.getStatus().equalsIgnoreCase("INACTIVE")){
                   Toast.makeText(mContext,o.getmName() +" coming soon",Toast.LENGTH_SHORT).show();
                   return;
               }


               DLog.e(TAG,"" + o.getRedeemPoints());
               DLog.e(TAG,"" + o.getmProductId());
               DLog.e(TAG,"" + jsonStr);
               DLog.e(TAG,"-->" + String.valueOf(o.getmProductId()));
               if(o.getRedeemPoints() <= Integer.parseInt(jsonStr)){
                   Intent intent = new Intent(mContext,PointRedeemDetailPageUI.class);
                   intent.putExtra("mName", o.getmName());
                   intent.putExtra("mProductId",o.getmProductId());
                   intent.putExtra("QPoints", o.getRedeemPoints());
                   intent.putExtra("ProductURL", o.getProductURL());
                   startActivity(intent);
               }else{
                    Toast.makeText(mContext,"Not enough QPoints to redeem!",Toast.LENGTH_SHORT).show();
               }

           }catch (Exception ex){
               Toast.makeText(mContext,"Please contact admin!!!",Toast.LENGTH_SHORT).show();
           }
        }
    }


}
