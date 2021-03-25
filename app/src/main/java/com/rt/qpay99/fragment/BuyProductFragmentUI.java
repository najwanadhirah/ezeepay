package com.rt.qpay99.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.activity.ui.ProductCategoryItemListUI;
import com.rt.qpay99.adapter.BuyProductCategoryRecycleAdapter;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuyProductFragmentUI extends Fragment {

    private String TAG = this.getClass().getName();


    ScrollTextView scrolltext;
    ArrayList<ProductInfo> productInfoList;
    private Context mContext;
    RecyclerView gridview;
    String mCategory;



    public BuyProductFragmentUI() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DLog.e(TAG, " onCreate ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.view_layout_buyproduct,
                container, false);
        gridview =  rootView.findViewById(R.id.gvBuyProduct);
        gridview.setHasFixedSize(true);
        int numberOfColumns = FunctionUtil.calculateNoOfColumns(getActivity(),120);
        gridview.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        mContext = this.getActivity();
        new   getCategoryList().execute();

        return rootView;
    }

    ProgressDialog pd;
    ProductInfo product;
    private class getCategoryList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Get Item list....");
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (productInfoList.size() > 0) {
                DLog.e(TAG, "productList.size() =============" + productInfoList.size());
                gridview.setAdapter(new BuyProductCategoryRecycleAdapter(mContext, new mRecyclerViewOnItemClick(), Constants.IMG_OTHERS,productInfoList));
            }

            if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.productCategoryURL;
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
                    DLog.e(TAG, "pcId " + js.getString("pcId"));
                    DLog.e(TAG, "Name " + js.getString("Name"));
                    mCategory = js.getString("Name").replace(" " ,"");
                    DLog.e(TAG, "Status " + js.getString("Status"));
                    DLog.e(TAG, "Description " + js.getString("Description"));
                    DLog.e(TAG, "FirebaseDBId " + js.getString("FirebaseDBId"));

                    product.setpId(Integer.parseInt(js.getString("pcId")));
                    product.setFirebaseDBId(js.getString("FirebaseDBId"));
                    product.setName(js.getString("Name"));
                    product.setDescription(js.getString("Description"));
                    product.setStatus(js.getString("Status"));
                    if(product.getStatus().equalsIgnoreCase("ACTIVE"))
                        productInfoList.add(product);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            Intent intent = null;
            DLog.e(TAG,"" + position	);
            Context mContext = getActivity();
                intent = new Intent(mContext, ProductCategoryItemListUI.class);
                intent.putExtra("mCategory", productInfoList.get(position).getName().replace(" ",""));
                DLog.e(TAG,"" + productInfoList.get(position).getName());
                mContext.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        DLog.e(TAG, " onResume");

    }

}
