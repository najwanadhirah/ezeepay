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

public class GridViewImageAdapter extends BaseAdapter {
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;

	public GridViewImageAdapter(Context c) {
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
			holder.menuImage = (ImageView) convertView
					.findViewById(R.id.sMenuImage);
			holder.title = (TextView) convertView.findViewById(R.id.sMenuText);

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
	private Integer[] mThumbIds = { R.drawable.digi, R.drawable.digi,
			R.drawable.digi, R.drawable.digi, R.drawable.digi, R.drawable.digi

	};

	private Integer[] menuItems = { R.string.action_help,
			R.string.action_help, R.string.action_help,
			R.string.action_help, R.string.action_help,
			R.string.action_help

	};
}
