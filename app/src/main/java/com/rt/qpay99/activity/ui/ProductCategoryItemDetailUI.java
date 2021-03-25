package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.FirebaseObj.PriceList;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.GlobalAdapter;
import com.rt.qpay99.adapter.RecColorCodeAdapter;
import com.rt.qpay99.object.GlobalObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProductCategoryItemDetailUI extends SRSBaseActivity {

    String pId,mName,mCategory,sClientTxID,ProductDesc;


    TextView tvProductName,tvPrice,tvSelectMemory,tvColorname,tvDescr,tvDescr1,tvDescr2,tvDescr3,tvDescr4,tvDescr5,tvDescr6;

    RecyclerView mRecyclerViewColor;

    CheckBox chkAccBox;

    Button btnProcessPayment,btnCancel;

    ImageView header_image;

    String imgURL = "";

    GridView rvMemory;
    GridView rvGrade,rvQuantity;

    List<GlobalObj> MemoryObjs;
    List<GlobalObj> GradeObjs;
    List<GlobalObj> ColorObjs;
    List<GlobalObj> QtyObjs;
    String mColor,mGrade,mMemory,mPrice,mFirebaseDBId,accPrice;

    TextView tvSelectColor,tvSelectGrade;

    GlobalAdapter rvGradeAdapter,rvMemoryAdapter;
    boolean isBuyAccessories = false;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_product_category_item_detail_ui;
    }

    @Override
    protected void activityCreated() {
        mContext = this;

        tvProductName =  findViewById(R.id.tvProductName);
        tvPrice =  findViewById(R.id.tvPrice);
        tvColorname =  findViewById(R.id.tvColorname);

        chkAccBox = findViewById(R.id.chkAccBox);



        header_image =  findViewById(R.id.header_image);

        tvDescr =  findViewById(R.id.tvDescr);
        tvDescr1 =  findViewById(R.id.tvDescr1);
        tvDescr2 =  findViewById(R.id.tvDescr2);
        tvDescr3 =  findViewById(R.id.tvDescr3);
        tvDescr4 =  findViewById(R.id.tvDescr4);
        tvDescr5 =  findViewById(R.id.tvDescr5);
        tvDescr6 =  findViewById(R.id.tvDescr6);

        tvSelectColor = findViewById(R.id.tvSelectColor);
        tvSelectMemory = findViewById(R.id.tvSelectMemory);
        tvSelectGrade =  findViewById(R.id.tvSelectGrade);

        rvGrade = findViewById(R.id.rvGrade);
        rvMemory =  findViewById(R.id.rvMemory);

//        rvQuantity=  findViewById(R.id.rvQuantity);
//        GlobalObj c = new GlobalObj("x1", "1", "", "", true);
//        QtyObjs =  new ArrayList<GlobalObj>();
//        QtyObjs.add(c);
//        c = new GlobalObj("x2", "2", "", "", false);
//        QtyObjs.add(c);
//        c = new GlobalObj("x3", "3", "", "", false);
//        QtyObjs.add(c);

        btnCancel =findViewById(R.id.btnCancel);
        btnProcessPayment =findViewById(R.id.btnProcessPayment);

        mRecyclerViewColor = findViewById(R.id.mRecyclerViewColor);
        mRecyclerViewColor.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager1 = new LinearLayoutManager(mContext);
        MyLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewColor.setLayoutManager(MyLayoutManager1);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnProcessPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PaymentProcessUI.class);
                intent.putExtra("mProduct",mCategory);
                intent.putExtra("phoneMemory",mMemory);
                intent.putExtra("phoneColor",mColor);
                intent.putExtra("phoneGrade",mGrade);
                intent.putExtra("dProductPrice",String.valueOf(Integer.parseInt(mPrice) + Integer.parseInt(accPrice)));
                intent.putExtra("imgURL",imgURL );
                intent.putExtra("mFirebaseDBId",mFirebaseDBId );
                intent.putExtra("mName",mName );


                DLog.e(TAG,"mCategory " + mCategory);
                DLog.e(TAG,"mMemory " + mMemory);
                DLog.e(TAG,"mColor " + mColor);
                DLog.e(TAG,"mGrade " + mGrade);
                DLog.e(TAG,"dProductPrice " + String.valueOf(Integer.parseInt(mPrice) + Integer.parseInt(accPrice)));
                DLog.e(TAG,"mPrice " + mPrice);
                DLog.e(TAG,"accPrice " + accPrice);

                DLog.e(TAG,"mFirebaseDBId" +  mFirebaseDBId);


                DLog.e(TAG,"imgURL " + imgURL);

                if(isBuyAccessories){
                    intent.putExtra("phoneModal",mName + "(Accessories)" );
                }else{
                    intent.putExtra("phoneModal",mName );
                }

                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        accPrice="0";

        Bundle extras = getIntent().getExtras();
        mCategory = extras.getString("mCategory");
        pId = extras.getString("pId");
        mName = extras.getString("mName");
        ProductDesc = extras.getString("mDescription");
        mFirebaseDBId= extras.getString("mFirebaseDBId");

        if(pId.equalsIgnoreCase("18")){
            pId="100018";
        }

        if(pId.equalsIgnoreCase("19")){
            pId="100019";
        }

        tvProductName.setText(mName);

        chkAccBox.setVisibility(View.GONE);
        if(mCategory.equalsIgnoreCase(Constants.CAT_PHONE)){
            chkAccBox.setVisibility(View.VISIBLE);
            if(mName.toLowerCase().indexOf("iphone")>-1){
                chkAccBox.setText("Tempered Glass, USB Cable, Power Adaptor, Casing, Box (RM30)");
            }else
            {
                chkAccBox.setText("Earphone, Adaptor, cable, tempered glass Only (RM30)");
            }
        }

        chkAccBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (buttonView.isChecked()) {
//                        phonePrice =  String.valueOf(Integer.parseInt(phonePrice) + Integer.parseInt(accPrice));
                        accPrice="30";
                        isBuyAccessories = true;
                        DLog.e(TAG,"checked");
                    } else {
                        isBuyAccessories = false;
                        accPrice="0";
                        DLog.e(TAG,"unchecked");
                    }
                    tvPrice.setText("RM " +  String.valueOf(Integer.parseInt(mPrice) + Integer.parseInt(accPrice)).toString() + ".00(includes 0% GST)");
                }
            }
        });



//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(mName);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sClientTxID = UUID.randomUUID().toString();



        if(!TextUtils.isEmpty(ProductDesc)){
            DLog.e(TAG,"" + ProductDesc);
            String[] array = ProductDesc.split("\\|", -1);
            if (array.length > 0) {
                tvDescr.setText(array[0]);
            }

            if (array.length > 1) {
                tvDescr1.setText(array[1]);
            }

            if (array.length > 2) {
                tvDescr2.setText(array[2]);
            }

            if (array.length > 3) {
                tvDescr3.setText(array[3]);
            }

            if (array.length > 4) {
                tvDescr4.setText(array[4]);
            }

            if (array.length > 5) {
                tvDescr5.setText(array[5]);
            }

            if (array.length > 6) {
                tvDescr6.setText(array[6]);
            }
        }

        new getProductList().execute();





    }

    void refreshUI(){

        DLog.e(TAG,"" + mCategory);


        tvSelectColor.setVisibility(View.GONE);
        mRecyclerViewColor.setVisibility(View.GONE);
        tvSelectMemory.setVisibility(View.GONE);
        rvMemory.setVisibility(View.GONE);
        tvColorname.setVisibility(View.GONE);
        tvSelectGrade.setVisibility(View.GONE);
        rvGrade.setVisibility(View.GONE);
//        rvQuantity.setVisibility(View.GONE);


        if(mCategory.equalsIgnoreCase(Constants.CAT_POWERBANK)){
            tvSelectColor.setVisibility(View.VISIBLE);
            mRecyclerViewColor.setVisibility(View.VISIBLE);
            tvSelectGrade.setVisibility(View.VISIBLE);
            rvGrade.setVisibility(View.VISIBLE);

        }else if(mCategory.equalsIgnoreCase(Constants.CAT_PHONE)){
            tvSelectColor.setVisibility(View.VISIBLE);
            mRecyclerViewColor.setVisibility(View.VISIBLE);
            tvSelectMemory.setVisibility(View.VISIBLE);
            rvMemory.setVisibility(View.VISIBLE);
            tvColorname.setVisibility(View.VISIBLE);
            tvSelectGrade.setVisibility(View.VISIBLE);
            rvGrade.setVisibility(View.VISIBLE);

        }else if(mCategory.equalsIgnoreCase(Constants.CAT_SIMPACK)){
            tvSelectMemory.setVisibility(View.VISIBLE);
            tvSelectMemory.setText("Select Quantity");
            rvMemory.setVisibility(View.VISIBLE);
//            rvQuantity.setVisibility(View.VISIBLE);

        }else if(mCategory.equalsIgnoreCase(Constants.CAT_PRINTER)){


        }else if(mCategory.equalsIgnoreCase(Constants.CAT_MEMORYCARDS)){
            tvSelectMemory.setVisibility(View.VISIBLE);
            rvMemory.setVisibility(View.VISIBLE);

        }else{

        }








    }

    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            DLog.e(TAG, "" + position);
            if (ColorObjs.size() > 0) {
                mColor = ColorObjs.get(position).getmName();
                DLog.e(TAG, "phoneColor " + mColor);
                tvColorname.setText(mColor + ", Grade " + mGrade + ", " + mMemory );
                if (!TextUtils.isEmpty(mFirebaseDBId)){
                    updateHeaderImage();
                }

            }
        }
    }

    void updateHeaderImage(){


        String firebaseId = mFirebaseDBId;
        Long expires = SharedPreferenceUtil.getSessionExpired();

        DLog.e(TAG,"expires " + expires);
        DLog.e(TAG,"System.currentTimeMillis() " + System.currentTimeMillis());

        if (System.currentTimeMillis() < expires) {
            DLog.e(TAG, "get from SharedPreferenceUtil----------------->");
            String str_url = SharedPreferenceUtil.getSharedPreferencedValue(mName);
            File imgFile = new File(str_url);
            if (imgFile.exists()) {
                DLog.e(TAG, "imgFile.exists() ----------------->" + mName);
                DLog.e(TAG, "str_url " + str_url);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                header_image.setImageBitmap(myBitmap);
                return;
            }
        }

        DLog.e(TAG,"downloadImagesAsync --------------------->");
        ImageUtil.downloadImagesAsync(mName,firebaseId, header_image,mContext);
    }

    ProgressDialog pd;
    private class getProductList extends AsyncTask<Void, Void, PriceList> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pd!=null)
                if(pd.isShowing()){
                    pd.dismiss();
                }
            pd = new ProgressDialog(mContext);
            pd.setCancelable(true);
            pd.setMessage("Get Product list....");
            pd.show();


        }

        @Override
        protected void onPostExecute(PriceList priceList) {
            super.onPostExecute(priceList);


            if (ColorObjs.size() > 0 & mRecyclerViewColor != null) {
                DLog.e(TAG, "------------------set adapter " + mColor + ", " + mGrade + ", " + mMemory + " " + mName);
                mColor = ColorObjs.get(0).getmName();
                if(mColor=="NA")
                    mColor="";
                tvColorname.setText(mColor + ", Grade " + mGrade + ", " + mMemory);
                try{
                    int d = ImageUtil.setPhoneImages(mName,mColor,mContext);
//                    header_image.setImageDrawable(getResources().getDrawable(d));
                }catch (Exception e){
                    DLog.e(TAG,"" + e.getMessage());
                }
                mRecyclerViewColor.setAdapter(new RecColorCodeAdapter(ColorObjs, mContext, new mRecyclerViewOnItemClick()));
            }




            if (priceList != null) {
                rvGradeAdapter = new GlobalAdapter(mContext, GradeObjs, Constants.IPHONE);
                rvGrade.setAdapter(rvGradeAdapter);
                rvGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        rvGradeAdapter.setSelectedItem(position);
                        List<GlobalObj> m = (List<GlobalObj>) parent.getAdapter().getItem(position);
                        mGrade = m.get(position).getmName();
                        if(mGrade=="NA")
                            mGrade="";
                        rvGradeAdapter.notifyDataSetChanged();
                        tvColorname.setText(mColor + ", Grade " + mGrade + ", " + mMemory);
                        new ProductCategoryItemDetailUI.getPriceList().execute();
                    }
                });

                rvMemoryAdapter = new GlobalAdapter(mContext, MemoryObjs, Constants.IPHONE_MEMORY,mCategory);
                rvMemory.setAdapter(rvMemoryAdapter);
                rvMemory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        List<GlobalObj> m = (List<GlobalObj>) parent.getAdapter().getItem(position);
                        mMemory = m.get(position).getmName();
                        if(mMemory=="NA")
                            mMemory="";
                        tvColorname.setText(mColor + ", Grade " + mGrade + ", " + mMemory);
                        rvMemoryAdapter.setSelectedItem(position);
                        rvMemoryAdapter.notifyDataSetChanged();
                        new ProductCategoryItemDetailUI.getPriceList().execute();

                    }
                });
            }

//
//            if (pd != null)
//                if (pd.isShowing())
//                    pd.dismiss();

            if (priceList != null) {
                new getPriceList().execute();
            }else{
              if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();
            }

        }

        @Override
        protected PriceList doInBackground(Void... params) {

            MemoryObjs = new ArrayList<GlobalObj>();
            GradeObjs = new ArrayList<GlobalObj>();
            ColorObjs = new ArrayList<GlobalObj>();

            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.ItemPriceURL + pId;
            DLog.e(TAG, "url : " + url);
            String jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr);
            PriceList mPriceList = null;

            HashMap<String, GlobalObj> meMap = new HashMap<String, GlobalObj>();
            HashMap<String, GlobalObj> meMap2 = new HashMap<String, GlobalObj>();
            HashMap<String, GlobalObj> meMap3 = new HashMap<String, GlobalObj>();
            try {
                JSONObject json = new JSONObject(jsonStr);
                JSONArray cast = json.getJSONArray("Data");
                if (cast == null) return null;
                DLog.e(TAG, "" + cast.toString());

                if(cast.length()==0) return null;

                for (int i = 0; i < cast.length(); i++) {
                    mPriceList = new PriceList();
                    JSONObject js = cast.getJSONObject(i);
                    DLog.e(TAG, "PriceId " + js.getString("PriceId"));
                    DLog.e(TAG, "Price " + js.getString("Price"));
                    DLog.e(TAG, "Type " + js.getString("Type"));
                    DLog.e(TAG, "Status " + js.getString("Status"));
                    mPriceList.setPriceId(js.getString("PriceId"));
                    mPriceList.setType(js.getString("Type"));
                    mPriceList.setPrice(js.getString("Price"));
                    mPriceList.setStatus(js.getString("Status"));
                    mPriceList.setColor(js.getString("Color"));
                    mPriceList.setGrade(js.getString("Grade"));


                    DLog.e(TAG, "" + js.getString("Type"));
                    GlobalObj g = new GlobalObj(js.getString("Type"), "", "", "", false);
                    meMap.put(js.getString("Type"), g);

                    DLog.e(TAG, "" + js.getString("Grade"));
                    GlobalObj p = new GlobalObj(js.getString("Grade"), "", "", "", false);
                    meMap2.put(js.getString("Grade"), p);

                    DLog.e(TAG, "" + js.getString("Color"));
                    GlobalObj c = new GlobalObj(js.getString("Color"), "", "", "", false, ImageUtil.getColorCode(js.getString("Color")));
                    meMap3.put(js.getString("Color"), c);
                }

                MemoryObjs = new ArrayList<GlobalObj>(meMap.values());
                GradeObjs = new ArrayList<GlobalObj>(meMap2.values());
                ColorObjs = new ArrayList<GlobalObj>(meMap3.values());



                List<GlobalObj> GradeSort = GradeObjs;
                Collections.sort(GradeSort, new Comparator<GlobalObj>() {
                    public int compare(GlobalObj one, GlobalObj other) {
                        return one.getmName().compareTo(other.getmName());
                    }
                });

                List<GlobalObj> MemorySort = MemoryObjs;
                Collections.sort(MemorySort, new Comparator<GlobalObj>() {
                    public int compare(GlobalObj one, GlobalObj other) {
                        return one.getmName().compareTo(other.getmName());
                    }
                });

                mMemory = MemoryObjs.get(0).getmName();
                mColor = ColorObjs.get(0).getmName();
                mGrade = GradeObjs.get(0).getmName();



                DLog.e(TAG, "------" + MemoryObjs.size());
                if (MemoryObjs.size() == 0) return null;


                DLog.e(TAG, "phoneMemoryObjs " + MemoryObjs.size());
                DLog.e(TAG, "phoneColorObjs " + ColorObjs.size());
                DLog.e(TAG, "phoneGradeObjs " + GradeObjs.size());


                return mPriceList;


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class getPriceList extends AsyncTask<Void, Void, PriceList> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pd==null)
                pd = new ProgressDialog(mContext);
            pd.setCancelable(true);
            pd.setMessage("Get price list....");

            if(!pd.isShowing())
                pd.show();

        }

        @Override
        protected void onPostExecute(PriceList mPriceList) {
            super.onPostExecute(mPriceList);


//            gvGrade.setEnabled(true);
//            gvMemory.setEnabled(true);
//            gvColor.setEnabled(true);
            chkAccBox.setEnabled(true);
            btnProcessPayment.setEnabled(true);
            btnProcessPayment.setText("Process to checkout");
            if (mPriceList == null) {
                tvPrice.setText("Out of stock");
                btnProcessPayment.setText("Out of stock");
                btnProcessPayment.setEnabled(false);
                chkAccBox.setEnabled(false);
                if (pd != null)
                    if (pd.isShowing())
                        pd.dismiss();
                return;
            }

            if (mPriceList == null) {
                tvPrice.setText("Out of stock");
                btnProcessPayment.setText("Out of stock");
                btnProcessPayment.setEnabled(false);
                chkAccBox.setEnabled(false);
                if (pd != null)
                    if (pd.isShowing())
                        pd.dismiss();

                return;
            }

            if (mPriceList.getStatus().equalsIgnoreCase("ACTIVE")) {
//                btnNext.setEnabled(true);
                mPrice =  mPriceList.getPrice();
                tvPrice.setText("RM " + String.valueOf(Integer.parseInt(mPrice) + Integer.parseInt(accPrice)) + ".00(includes 0% GST)");
                if(Integer.parseInt(mPrice) < 1){
                    btnProcessPayment.setEnabled(false);
                    chkAccBox.setEnabled(false);
                }
            } else {
                tvPrice.setText("Out of stock");
                btnProcessPayment.setEnabled(false);
                btnProcessPayment.setText("Out of stock");
                chkAccBox.setEnabled(false);
            }

            if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();

            if (!TextUtils.isEmpty(mFirebaseDBId)){
                updateHeaderImage();
            }

            refreshUI();


        }

        @Override
        protected PriceList doInBackground(Void... params) {
            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.ItemPriceURL + pId + "/" + Uri.encode(mColor) + "/" + mGrade + "/" + mMemory;
            DLog.e(TAG, "url : " + url);
            String jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr);
            PriceList mPriceList = null;
            try {
                JSONObject json = new JSONObject(jsonStr);
                JSONArray cast = json.getJSONArray("Data");
                DLog.e(TAG, "" + cast.toString());
                for (int i = 0; i < cast.length(); i++) {
                    mPriceList = new PriceList();
                    JSONObject js = cast.getJSONObject(i);
                    DLog.e(TAG, "PriceId " + js.getString("PriceId"));
                    DLog.e(TAG, "Price " + js.getString("Price"));
                    DLog.e(TAG, "Type " + js.getString("Type"));
                    DLog.e(TAG, "Status " + js.getString("Status"));
                    mPriceList.setPriceId(js.getString("PriceId"));
                    mPriceList.setType(js.getString("Type"));
                    mPriceList.setPrice(js.getString("Price"));
                    mPriceList.setStatus(js.getString("Status"));
//                    if (js.getString("Status").equalsIgnoreCase("ACTIVE")) {
//
//                    }
//                    else{
//
//                    }
                }
                return mPriceList;


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
