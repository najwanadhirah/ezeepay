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
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.activity.ui.IDMoneyPinUI;
import com.rt.qpay99.activity.ui.PinDetailUI;
import com.rt.qpay99.adapter.BuyTopupRecycleAdapter;
import com.rt.qpay99.bluetooth.service.BluetoothService;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class PINFragment_v2UI extends Fragment {

    private String TAG = this.getClass().getName();
    private RecyclerView gdTopUp;
    List<ProductInfo> gdTopUpInfo;
    private Context mContext;
    ScrollTextView scrolltext;
    private PrintDataService printDataService = null;
    public PINFragment_v2UI() {
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
        gdTopUpInfo = SharedPreferenceUtil.getPRODUCT_PIN_PREFERENCE();
        gdTopUp.setHasFixedSize(true);
        DLog.e(TAG,"---------------------------" + gdTopUpInfo.size());
        int numberOfColumns = FunctionUtil.calculateNoOfColumns(getActivity(),120);
        gdTopUp.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        gdTopUp.setAdapter(new BuyTopupRecycleAdapter(mContext, new mRecyclerViewOnItemClick(), Constants.IMG_OTHERS,gdTopUpInfo));


//        topupAdapter = new TopupAdapter(this.getActivity(), gdTopUpInfo);
//        gdTopUp.setAdapter(topupAdapter);
//        gdTopUp.setOnItemClickListener(new ProductGridviewOnItemClickListener(
//                this.getActivity(), getFragmentManager()));



        return rootView;
    }

    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {

            ProductInfo product = new ProductInfo();
            product = gdTopUpInfo.get(position);

            DLog.e(TAG, "P " + product.getDenomination().toString());

            Intent intent;
            intent = new Intent(mContext, PinDetailUI.class);
            if (product.getName().equalsIgnoreCase("IDMONEYPIN")) {
                intent = new Intent(mContext, IDMoneyPinUI.class);
            }
            intent.putExtra("Tax", product.getTax());
            intent.putExtra("Denomination", product.getDenomination());
            intent.putExtra("Description", product.getDescription());
            intent.putExtra("Instruction", product.getInstruction());
            intent.putExtra("Keyword", product.getKeyword());
            intent.putExtra("Name", product.getName());
            intent.putExtra("pId", String.valueOf(product.getpId()));

            if (SharedPreferenceUtil.isRequiredPrinter()) {
                if (SRSApp.printerMacAdd.length() < 1) {
                    BluetoothService.setmContext(mContext);
                    BluetoothService.getBOundedDevices(mContext);
                    return;
                } else {
                    if (!PrintDataService.isPrinterConnected()) {
                        printDataService = new PrintDataService(mContext,
                                SRSApp.printerMacAdd);
                        if (!printDataService.connect()) {
                            BluetoothService.setmContext(mContext);
                            BluetoothService.getBOundedDevices(mContext);
                        }
                        return;
                    }
                }
            }



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
