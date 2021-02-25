package com.rt.qpay99.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.activity.ui.ProductDetailUI_v2;
import com.rt.qpay99.activity.ui.ProductInternationalDetailUI_v2;
import com.rt.qpay99.adapter.QPromoListRecycleAdapter;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class QPromoListFragment extends Fragment {


    public QPromoListFragment() {

    }

    private String TAG = this.getClass().getName();
    RecyclerView rcvQPromoList;
    private Context mContext;
    List<ProductInfo> InfoList;
    QPromoListRecycleAdapter mQPromoListRecycleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qpromolist,
                container, false);
        mContext = this.getActivity();

        rcvQPromoList = rootView.findViewById(R.id.rcvQPromoList);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvQPromoList.setLayoutManager(layoutManager);
        InfoList = SharedPreferenceUtil.getPRODUCT_QPROMO_PREFERENCE();
        DLog.e(TAG, "InfoList.size() " + InfoList.size());
        if (InfoList != null) {
            if(InfoList.size()>0)
                rcvQPromoList.setAdapter(new QPromoListRecycleAdapter(mContext, new mRecyclerViewOnItemClick(), InfoList));
        }


        return rootView;
    }


    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            DLog.e(TAG, "ProductGridviewOnItemClickListener");
            ProductInfo product = new ProductInfo();
            product = InfoList.get(position);

            if (product == null) return;


            Intent intent = new Intent(mContext, ProductDetailUI_v2.class);


            if (product.getName().indexOf("FLEXI") > 0) {
                intent = new Intent(mContext, ProductInternationalDetailUI_v2.class);
            }


            DLog.e(TAG, "" + product.getDenominationDescription());
            intent.putExtra("Tax", product.getTax());
            intent.putExtra("Denomination", product.getDenomination());
            intent.putExtra("DenominationDescription", product.getDenominationDescription());
            intent.putExtra("Description", product.getDescription());
            intent.putExtra("Instruction", product.getInstruction());
            intent.putExtra("Keyword", product.getKeyword());
            intent.putExtra("Name", product.getName());
            intent.putExtra("pId", String.valueOf(product.getpId()));
            intent.putExtra("MaxLen", product.getMaxLen());
            intent.putExtra("MinLen", product.getMinLen());

            mContext.startActivity(intent);
        }
    }


}
