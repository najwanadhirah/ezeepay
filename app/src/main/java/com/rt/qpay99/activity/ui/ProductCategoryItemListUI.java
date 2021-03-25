package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.BuyProductItemListRecycleAdapter;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductCategoryItemListUI extends SRSBaseActivity {

    TextView tvInfo;
    RecyclerView rcvProductList;
    ArrayList<ProductInfo> productInfoList;

    String mCategory;

    LinearLayout llicon;
    ImageButton ibIOS,ibAndroid;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_product_category_item_list_ui;
    }

    @Override
    protected void activityCreated() {
        tvInfo =  findViewById(R.id.tvInfo);
        llicon = findViewById(R.id.llicon);
        ibIOS = findViewById(R.id.ibIOS);
        ibAndroid = findViewById(R.id.ibAndroid);

        rcvProductList =  findViewById(R.id.rcvProductList);
        int numberOfColumns = FunctionUtil.calculateNoOfColumns(this,120);
        rcvProductList.setLayoutManager(new GridLayoutManager(this, numberOfColumns));


        ibIOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IOSSelected = true;
                ibIOS.setSelected(true);
                ibAndroid.setSelected(false);
                new getCategoryList().execute();

            }
        });

        ibAndroid = findViewById(R.id.ibAndroid);
        ibAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IOSSelected = false;
                ibIOS.setSelected(false);
                ibAndroid.setSelected(true);
                new getCategoryList().execute();

            }
        });
    }

    @Override
    protected void initData() {
        extras = getIntent().getExtras();
        if(extras!=null) {
            mCategory = extras.getString("mCategory");
        }
        llicon.setVisibility(View.GONE);
        if(mCategory.equalsIgnoreCase(Constants.CAT_PHONE)){
            IOSSelected=true;
            llicon.setVisibility(View.VISIBLE);
        }

//        tvInfo.setText("Please select " + mCategory);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(mCategory);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        tvProductName.setText(mName);


        new getCategoryList().execute();
    }

    ProgressDialog pd;
    ProductInfo product;
    boolean IOSSelected = false;
    private class getCategoryList extends AsyncTask<Void, Void, Void> {
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


            if (productInfoList.size() > 0) {
                DLog.e(TAG, "productList.size() =============" + productInfoList.size());
                rcvProductList.setAdapter(new BuyProductItemListRecycleAdapter(mContext, new mRecyclerViewOnItemClick(), Constants.CAT_PRINTER,productInfoList));
            }

            if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.PrinterListURLItemListURL + mCategory;
            String jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr);
            productInfoList = new ArrayList<ProductInfo>();
            try {
                JSONObject json = new JSONObject(jsonStr);
                JSONArray cast = json.getJSONArray("Data");
                DLog.e(TAG, "" + cast.toString());
                for (int i = 0; i < cast.length(); i++) {
                    product = new ProductInfo();
                    JSONObject js = cast.getJSONObject(i);
                  try{
                      DLog.e(TAG, "pId " + js.getString("pId"));
                      DLog.e(TAG, "Name " + js.getString("Name"));
                      DLog.e(TAG, "Status " + js.getString("Status"));
                      DLog.e(TAG, "Description " + js.getString("Description"));
                      DLog.e(TAG, "FirebaseDBId " + js.getString("FirebaseDBId"));

                      product.setpId(Integer.parseInt(js.getString("pId")));
                      product.setFirebaseDBId(js.getString("FirebaseDBId"));
                      product.setName(js.getString("Name"));
                      product.setDescription(js.getString("Description"));
                      product.setStatus(js.getString("Status"));
                      product.setModel(js.getString("Modal"));

                      if (js.getString("Status").equalsIgnoreCase("ACTIVE")) {
                          if(mCategory.equalsIgnoreCase(Constants.CAT_PHONE)){
                              if (IOSSelected) {
                                  if (product.getModel().equalsIgnoreCase("ios")) {
                                      productInfoList.add(product);
                                      DLog.e(TAG, "product " + product.getName());

                                  }

                              } else {
                                  if (product.getModel().equalsIgnoreCase("android")){
                                      DLog.e(TAG, "product " + product.getName());
                                      productInfoList.add(product);
                                  }
                              }
                          }else
                          {
                              productInfoList.add(product);
                          }
                      }



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


    private final int Printer_SMALL=0;
    private final int Printer_BIG=1;

    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            Intent intent = null;
            DLog.e(TAG,"" + position	);
            ProductInfo product = productInfoList.get(position);

            intent = new Intent(mContext, ProductCategoryItemDetailUI.class);
            intent.putExtra("mCategory", mCategory);
            DLog.e(TAG,"getpId " +  product.getpId());
            DLog.e(TAG,"getName " +  product.getName());
            DLog.e(TAG,"getDescription " +  product.getDescription());
            intent.putExtra("mName", product.getName());
            intent.putExtra("pId", String.valueOf(product.getpId()));
            intent.putExtra("mDescription", product.getDescription());
            intent.putExtra("mFirebaseDBId", product.getFirebaseDBId());
            mContext.startActivity(intent);

        }
    }


}
