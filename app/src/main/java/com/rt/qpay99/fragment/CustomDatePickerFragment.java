package com.rt.qpay99.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.rt.qpay99.util.DLog;

import java.util.Calendar;

public class CustomDatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	private DatePickedListener mListener;

	public DatePickedListener getmListener() {
		return mListener;
	}

	public void setmListener(DatePickedListener mListener) {
		this.mListener = mListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		Calendar c = Calendar.getInstance();
		DLog.e("TAG", "y " + year);
		DLog.e("TAG", "m " + month);
		DLog.e("TAG", "d " + day);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);

		if (mListener != null) {
			mListener.onDatePicked(c);
		}

	}

	public static interface DatePickedListener {
		public void onDatePicked(Calendar time);
	}
}