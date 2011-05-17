package com.devstates.adatapoint;
import java.util.ArrayList;
import java.util.HashMap;

import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;
import com.pras.WorkSheet;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class SpreadSheetAdapter extends BaseAdapter {


	private static final String TAG = "SpreadSheetAdapter";
	private SpreadSheet mSs;
	private Context mContext;
	private boolean mNewSheetOption;

	SpreadSheetAdapter(Context context, SpreadSheet ss){
		this(context, ss, false);
	}
	
	SpreadSheetAdapter(Context context, SpreadSheet ss, boolean b){
		Log.d(TAG, "SpreadSheetAdapter(context, SpreadSheet, "+b+");");
		mSs = ss;
		mContext = context;
		mNewSheetOption = b;
		fetchList(true);
	}

	
	private void fetchList(boolean b) {
		ArrayList<WorkSheet> wsArr = mSs.getWorkSheet("dataset", true);
		if( null == wsArr ) {
			mSs.addWorkSheet("dataset", new String[]{"firstcolumn", "secondcolumn"});
			wsArr = mSs.getWorkSheet("dataset", true);
			
		}
		WorkSheet ws = wsArr.get(0); 
		Log.d(TAG, wsArr.toString());
		String[] columns = {"firstcolumn","secondcolumn"};
		ws.setColumns(columns);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put("firstcolumn", "firstValue");
		records.put("secondcolumn", "second Value");
		ws.addRecord(mSs.getKey(), records);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
