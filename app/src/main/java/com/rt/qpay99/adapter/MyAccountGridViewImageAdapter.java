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

public class MyAccountGridViewImageAdapter extends BaseAdapter {
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;

	public MyAccountGridViewImageAdapter(Context c) {
		mContext = c;
		inflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return mThumbIds.length -1 ; // -1 disable facebook icon
	}

	@Override
	public Object getItem(int position) {
		return null;
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
					R.layout.adapter_layout_gridview_button, null);
			holder.menuImage =  convertView
					.findViewById(R.id.sMenuImage);
			holder.title = convertView.findViewById(R.id.sMenuText);

			convertView.setTag(holder);

		} else {
			holder = (MenuViewHolder) convertView.getTag();
		}

		// holder.title.setText("OK");
		holder.menuImage.setImageResource(mThumbIds[position]);
		holder.title.setText(menuItems[position]);
		return convertView;
	}

	private class MenuViewHolder {
		public TextView title;
		public ImageView menuImage;
	}

	// references to our images
	private Integer[] mThumbIds = { R.drawable.ic_check_balance,
			R.drawable.ic_check_transaction, R.drawable.ic_report,
			R.drawable.ic_bank, R.drawable.ic_bank,R.drawable.ic_agentinfo,
			R.drawable.ic_dn,R.drawable.ic_bank,R.drawable.ic_fb_icon,

	};

	private Integer[] menuItems = { R.string.my_acc_check_bal,
			R.string.my_acc_check_transaction, R.string.my_acc_report,
			R.string.my_acc_bank_in,R.string.my_acc_bank_in_list, R.string.my_acc_agent_info, R.string.my_acc_DN,R.string.my_acc_share_credit,
			R.string.my_acc_update_fb_appid,

	};
}
