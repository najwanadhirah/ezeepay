package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;

import java.util.Date;

public class ContactUSUI extends FragmentActivity {
	private Button nav_btn_right;
	private TextView nav_text;
	private ImageButton btnRegister;
	private Context mContext;

	private ImageView ic_fb;
	private TextView tvFB;

	public void goToFacebook(String id) {

//		try {
//			getPackageManager().getPackageInfo("com.facebook.katana", 0);
//			Intent facebookPage = new Intent(Intent.ACTION_VIEW,
//					Uri.parse("fb://profile/" + id));
//			startActivity(facebookPage);
//		} catch (Exception e) {
//			Intent launchBrowser = new Intent(Intent.ACTION_VIEW,
//					Uri.parse("http://facebook.com/qpay99"));
//			startActivity(launchBrowser);
//		}
		
		String facebookUrl = "https://www.facebook.com/qpay99";
		try {
		        int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
		        if (versionCode >= 3002850) {
		            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
		            startActivity(new Intent(Intent.ACTION_VIEW, uri));
		        } else {
		            // open the Facebook app using the old method (fb://profile/id or fb://pro
		        }
		} catch (PackageManager.NameNotFoundException e) {
		    // Facebook is not installed. Open the browser
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_register);
		mContext = this;

		ic_fb = (ImageView) findViewById(R.id.ic_fb);
		ic_fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToFacebook("100008140897779");
			}

		});

		tvFB = (TextView) findViewById(R.id.tvFB);
		tvFB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToFacebook("100008140897779");
			}

		});

		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		nav_text = (TextView) findViewById(R.id.nav_text);
		nav_text.setText(R.string.register);

		btnRegister = (ImageButton) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RegisterAlertDialog().show();
			}

		});
	}

	AutoCompleteTextView actv;
	EditText edContact;
	private Spinner spinner1;

	private AlertDialog RegisterAlertDialog() {
		String[] values = { "Perak", "Kedah", "Kelantan", "Melaka",
				"Negeri Sembilan", "Pahang", "Johor", "Perlis", "Pulau Pinang",
				"Sabah", "Sarawak", "Selangor", "Terengganu",
				"WP Kuala Lumpur", "WP Labuan", "WP Putra Jaya" };
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.view_layout_register, null);

		spinner1 = (Spinner) view.findViewById(R.id.spinner1);

		edContact = (EditText) view.findViewById(R.id.edContact);
		//
		// actv = (AutoCompleteTextView) view.findViewById(R.id.acState);
		// final ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(mContext,
		// android.R.layout.simple_list_item_1, values);
		// actv.setAdapter(adapter);
		// actv.setThreshold(256);
		// actv.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// actv.showDropDown();
		// }
		// });

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(Config.EMAIL_SUBJECT)
				.setView(view)
				.setCancelable(false)
				.setNegativeButton(R.string.title_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						})
				.setPositiveButton(R.string.title_send,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								String contact = "";

								if (edContact.getText() != null)
									if (edContact.getText().length() > 6) {
										contact = edContact.getText()
												.toString();
									} else {
										normalAlertDialog(
												"Please insert valid contact no.")
												.show();
										return;
									}
								Intent intentEmail = new Intent(
										Intent.ACTION_SEND);
								intentEmail.putExtra(Intent.EXTRA_EMAIL,
										new String[] { Config.EMAIL_ADD1,
												Config.EMAIL_ADD2 });
								// intentEmail.putExtra(Intent.EXTRA_BCC,
								// new String[] { Config.EMAIL_ADD_BCC });
								intentEmail.putExtra(Intent.EXTRA_SUBJECT,
										Config.EMAIL_SUBJECT);
								intentEmail.putExtra(Intent.EXTRA_TEXT, contact
										+ "\n" + spinner1.getSelectedItem()
										+ "\n" + new Date());
								intentEmail.setType("message/rfc822");
								startActivity(Intent.createChooser(intentEmail,
										"Choose an email provider :"));

							}
						}).create();
	}

	private AlertDialog normalAlertDialog(final String msg) {

		return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
				R.style.AlertDialogCustom))
				// .setTitle(mName)
				.setTitle(R.string.app_name)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								RegisterAlertDialog().show();
							}
						}).create();
	}
}
