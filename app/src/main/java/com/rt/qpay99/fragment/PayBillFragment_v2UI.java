package com.rt.qpay99.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.activity.ui.QueryAcountUI;
import com.rt.qpay99.activity.ui.UltilitiesBillDetailUI;
import com.rt.qpay99.adapter.BuyTopupRecycleAdapter;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class PayBillFragment_v2UI extends Fragment {

    private String TAG = this.getClass().getName();
    private RecyclerView gdTopUp;
    List<ProductInfo> gdTopUpInfo;
    private Context mContext;
    ScrollTextView scrolltext;

    public PayBillFragment_v2UI() {
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
        DLog.e(TAG, " onCreateView " + Config.MOVING_TEXT);
        View rootView = inflater.inflate(R.layout.view_layout_topup_recycleview,
                container, false);
        mContext = this.getActivity();

        scrolltext =  rootView.findViewById(R.id.scrolltext);
        scrolltext.setText(Config.MOVING_TEXT);
        scrolltext.startScroll();



        gdTopUp =  rootView.findViewById(R.id.gdTopup);
        gdTopUpInfo = SharedPreferenceUtil.getPRODUCT_PAYBILL_PREFERENCE();
        gdTopUp.setHasFixedSize(true);
        int numberOfColumns = FunctionUtil.calculateNoOfColumns(getActivity(),120);
        gdTopUp.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        gdTopUp.setAdapter(new BuyTopupRecycleAdapter(mContext, new mRecyclerViewOnItemClick(), Constants.IMG_OTHERS,gdTopUpInfo));




        return rootView;
    }

    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            DLog.e(TAG, "ProductGridviewOnItemClickListener");
            ProductInfo product = new ProductInfo();
            product = gdTopUpInfo.get(position);

            if(product==null)return;



            Intent intent = new Intent(mContext, UltilitiesBillDetailUI.class);


            String productName =product.getName();
            if((productName.equalsIgnoreCase("UMOBILEBILL"))
                    ||(productName.equalsIgnoreCase("CELCOMBILL"))
                    ||(productName.equalsIgnoreCase("DIGIBILL"))
                    ||(productName.equalsIgnoreCase("MAXISBILL"))
                    ||(productName.equalsIgnoreCase("XOXBILL"))
                    ||(productName.equalsIgnoreCase("REDONEBILL"))
                    ||(productName.equalsIgnoreCase("REDTONEBILL"))) {
                intent.putExtra("CATEGORY", "POSTPAID");
            }else{

                intent.putExtra("CATEGORY", "UTILITIES");
            }


            if((productName.equalsIgnoreCase("ASTROBILL"))
                    || (productName.equalsIgnoreCase("AIRPAHANGBILL"))
                    ){
                intent = new Intent(mContext, QueryAcountUI.class);
            }


            DLog.e(TAG,"" + product.getDenominationDescription());
            intent.putExtra("Denomination", product.getDenomination());
            intent.putExtra("Description", product.getDescription());
            intent.putExtra("Instruction", product.getInstruction());
            intent.putExtra("Keyword", product.getKeyword());
            intent.putExtra("Name", product.getName());
            intent.putExtra("MaxLen", product.getMaxLen());
            intent.putExtra("MinLen", product.getMinLen());
            intent.putExtra("pId", String.valueOf(product.getpId()));

            mContext.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        DLog.e(TAG, " onResume");
        scrolltext.setText(Config.MOVING_TEXT);
    }

}
