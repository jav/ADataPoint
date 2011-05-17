package com.devstates.adatapoint;
import java.util.ArrayList;
import java.util.HashMap;

import com.javier.R;
import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;
import com.pras.WorkSheet;
import com.pras.table.Record;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SpreadSheetAdapter extends BaseAdapter {


	private static final String TAG = "SpreadSheetAdapter";
	private SpreadSheet mSs;
	private WorkSheet mWs;
	private Context mContext;
	private boolean mNewSheetOption;
	private String[] mColumns = {"firstcolumn","secondcolumn"};
	private ArrayList<Record> mRecords;
	
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
		if( null == mWs) {
			ArrayList<WorkSheet> wsArr = mSs.getWorkSheet("dataset", true);
			if( null == wsArr ) {
				mSs.addWorkSheet("dataset", new String[]{"firstcolumn", "secondcolumn"});
				wsArr = mSs.getWorkSheet("dataset", true);
			}
			mWs = wsArr.get(0);
			mWs.setColumns(mColumns);
		}
		mRecords = mWs.getRecords();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mRecords.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mRecords.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView("+position+", ... );");
        LinearLayout itemLayout;
        HashMap<String, String> data;

        
        data = mRecords.get(position).getData();
        Log.d(TAG, "getView("+position+", "+data+");");
 
        LayoutInflater inflater = LayoutInflater.from(mContext);
        itemLayout = (LinearLayout) inflater.inflate(R.layout.dataset_item, parent, false);
        
 
        TextView tvTitle = (TextView) itemLayout.findViewById(R.id.txt_dataset);
        tvTitle.setText(data.toString());
 
        // TextView txt_ = (TextView) itemLayout.findViewById(R.id.TweetText);
        // txt_.setText(tweet.getText());
        Log.d(TAG, "getView("+position+", "+data+") - return");
        return itemLayout;
	}

}
