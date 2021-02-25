package com.rt.qpay99.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class TopupAdapter extends BaseAdapter {

	private List<ProductInfo> data = new ArrayList<ProductInfo>();
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources res;

	public TopupAdapter(Context mContext, List<ProductInfo> product) {
		this.data = product;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() { // TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		vHolder holder = new vHolder();
		if (convertView == null) {
			holder = new vHolder();
			convertView = inflater.inflate(
					R.layout.adapter_layout_gridview_button, null);
			holder.mImage = (ImageView) convertView
					.findViewById(R.id.sMenuImage);
			holder.mTitle = (TextView) convertView.findViewById(R.id.sMenuText);
			convertView.setTag(holder);

		} else {
			holder = (vHolder) convertView.getTag();
		}

		ProductInfo product = data.get(position);
		holder.mTitle.setText(product.getName());
		holder.mTitle.setSelected(true);
		// holder.mImage.setImageResource(setImages(product.getName()));
		holder.mImage.setImageResource(ImageUtil.setProductImages(product.getName(),"",mContext));

		return convertView;
	}

	private int setImages(String name) {
		int imgRes=0;
		try{
			if(FunctionUtil.isSet(name)){
				name = name.replace(".","");
				name = name.replace("4","");
				String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
				imgRes = mContext.getResources().getIdentifier(uri, null,
						mContext.getPackageName());
				if (imgRes == 0)
					return R.drawable.ic_no_image;
			}else
				return R.drawable.ic_no_image;

		}catch (Exception e){
			return R.drawable.ic_no_image;
		}
		return imgRes;

	}

	private class vHolder {
		TextView mTitle;
		ImageView mImage;
	}

}
