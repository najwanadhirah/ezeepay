package com.rt.qpay99.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rt.qpay99.R;

import java.util.List;

public class InternationalProductAdapter extends BaseAdapter {
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;
	private List<String> deno,denoDesc;

	public InternationalProductAdapter(Context c, List<String> deno, List<String> denoDesc) {
		mContext = c;
		inflater = LayoutInflater.from(c);
		this.deno = deno;
		this.denoDesc = denoDesc;
	}

	@Override
	public int getCount() {
		return deno.size();
	}

	@Override
	public Object getItem(int position) {
		return deno.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuViewHolder holder = null;
		if (convertView == null) {
			holder = new MenuViewHolder();
			convertView = inflater.inflate(
					R.layout.adapter_layout_gridview_international, null);
			holder.title = (TextView) convertView.findViewById(R.id.sMenuText);
			convertView.setTag(holder);

		} else {
			holder = (MenuViewHolder) convertView.getTag();
		}

		if(denoDesc!=null){
			holder.title.setText(denoDesc.get(position));
		}else{
			holder.title.setText(deno.get(position));
		}

		return convertView;
	}

	private class MenuViewHolder {
		public TextView title;

	}


}
