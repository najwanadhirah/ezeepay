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

public class SettingGridViewImageAdapter extends BaseAdapter {
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;

	public SettingGridViewImageAdapter(Context c) {
		mContext = c;
		inflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return mThumbIds.length;
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
			holder.menuImage = convertView
					.findViewById(R.id.sMenuImage);
			holder.title =  convertView.findViewById(R.id.sMenuText);

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
	private Integer[] mThumbIds = { R.drawable.ic_help, R.drawable.ic_share,
			R.drawable.ic_update, R.drawable.ic_refresh,
			R.drawable.ic_printer_icon, R.drawable.ic_verified_seo,
			R.drawable.ic_printer_icon,
			R.drawable.ic_google_maps_icon_2015,R.drawable.ic_ios_settings,
			R.drawable.ic_logout,

	};

	private Integer[] menuItems = { R.string.setting_help,
			R.string.setting_share, R.string.setting_update_app,
			R.string.setting_refresh_product, R.string.setting_select_printer,
			R.string.setting_verify_password, R.string.setting_test_print,
			R.string.setting_location,	R.string.setting_permission,
			R.string.setting_logout,

	};
}
