package com.rt.qpay99.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

public class QPointsPageOneFragment extends Fragment {


    public QPointsPageOneFragment() {

    }


    private Context mContext;
    private TextView tvInfo,tvOne,tvTwo;

    private String TAG = this.getClass().getName();
    private String jsonStr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qpoints_page_one,
                container, false);
        mContext = this.getActivity();
        tvInfo = rootView.findViewById(R.id.tvTotalQPoints);
        tvOne = rootView.findViewById(R.id.tvOne);
        tvTwo = rootView.findViewById(R.id.tvTwo);
        getQPoints();

        return rootView;
    }

    void getQPoints(){
        tvOne.setVisibility(View.INVISIBLE);
        tvTwo.setVisibility(View.INVISIBLE);
        new getAgentQPoints().execute();
    }

    private class getAgentQPoints extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Loading...");
            tvOne.setVisibility(View.INVISIBLE);
            tvTwo.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            tvInfo.setText(aVoid);
            tvOne.setVisibility(View.VISIBLE);
            tvTwo.setVisibility(View.VISIBLE);
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

            return jsonStr  + "";
        }
    }



}
