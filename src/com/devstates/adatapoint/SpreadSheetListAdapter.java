package com.devstates.adatapoint;

import java.util.ArrayList;

import com.javier.R;
import com.pras.SpreadSheetFactory;
import com.pras.SpreadSheet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpreadSheetListAdapter extends BaseAdapter {

	private static final String TAG = "SpreadSheetListAdapter";
	private SpreadSheetFactory mSpf;
	private ArrayList<SpreadSheet> mSpreadSheets;
	private String mSheetPrefix = "ADataSet_";
	private Context mContext;
	private boolean mNewSheetOption;

	SpreadSheetListAdapter(Context context, SpreadSheetFactory spf){
		this(context, spf, false);
	}
	
	SpreadSheetListAdapter(Context context, SpreadSheetFactory spf, boolean b){
		Log.d(TAG, "SpreadSheetAdapter(context, SpreadSheetFactor, "+b+");");
		mSpf = spf;
		mContext = context;
		mNewSheetOption = b;
		fetchList(true);
	}
	
	public int getCount() {
		Log.d(TAG, "getCount();");
		if(mNewSheetOption)			
			return mSpreadSheets.size()+1;
		return mSpreadSheets.size();
	}

	public Object getItem(int arg0) {
		Log.d(TAG, "getItem("+arg0+");");
		if(mNewSheetOption)	{
			if(0 == arg0) {
				return "New spread sheet...";
			}
			else
				return mSpreadSheets.get(arg0-1).getTitle();
		}
		return mSpreadSheets.get(arg0).getTitle();
		
	}

	public long getItemId(int arg0) {
		Log.d(TAG, "getItemId("+arg0+");");
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView("+position+", ... );");
        LinearLayout itemLayout;
        String title;
        if(mNewSheetOption ) {
        	if( 0 == position) {
        		title = new String("New spread sheet...");
        	}
        	else {
        		SpreadSheet sp = mSpreadSheets.get(position-1);
        		title = sp.getTitle();
        	}
        	
        }
        else {
        	SpreadSheet sp = mSpreadSheets.get(position);
        	title = sp.getTitle();
        }
        Log.d(TAG, "getView("+position+", "+title+");");
 
        LayoutInflater inflater = LayoutInflater.from(mContext);
        itemLayout = (LinearLayout) inflater.inflate(R.layout.dataset_item, parent, false);
        
 
        TextView tvTitle = (TextView) itemLayout.findViewById(R.id.txt_dataset);
        tvTitle.setText(title);
 
        // TextView txt_ = (TextView) itemLayout.findViewById(R.id.TweetText);
        // txt_.setText(tweet.getText());
        Log.d(TAG, "getView("+position+", "+title+") - return");
        return itemLayout;
	}

	public boolean hasStableIds(){
		Log.d(TAG, "hasStableIds();");
		return false;
	}
	
	private void fetchList(boolean refresh){
		Log.d(TAG, "fetchList("+refresh+");");
		mSpreadSheets = mSpf.getAllSpreadSheets(refresh, mSheetPrefix, false);
	}
	
}
