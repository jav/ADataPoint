package com.devstates.adatapoint;

import java.util.ArrayList;
import java.util.HashMap;

import com.javier.R;
import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;
import com.pras.WorkSheet;
import com.pras.table.Record;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddADataPoint extends Activity implements OnClickListener {
	private static final String TAG = "AddADataPoint";
	private TextView txt_dataset;
	private DigitalClock dc_clock;
	private EditText et_value;
	private ListView lv_records;
	private Button btn_add_data_point;
	private String[] mColumns = {"firstcolumn","secondcolumn"};
	private ArrayList<Record> mRecords;
	private SpreadSheet mSs;
	private WorkSheet mWs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
        setContentView(R.layout.add_a_data_point);
		Log.d(TAG, "setContentView(R.layout.add_a_data_point);");
		
		txt_dataset = (TextView) findViewById(R.id.txt_data_set_name);
		et_value = (EditText) findViewById(R.id.et_value);
		dc_clock = (DigitalClock) findViewById(R.id.dc_time);
		lv_records = (ListView) findViewById(R.id.lv_records);
		btn_add_data_point = (Button) findViewById(R.id.btn_add_data_point);
		
		if( null == txt_dataset ) Log.d(TAG, "txt_dataset is null;");
		else Log.d(TAG, "txt_dataset is NOT null;");
		txt_dataset.setText(getIntent().getExtras().getString("sheet_name"));
		Toast.makeText(this, getIntent().getExtras().getString("sheet_name"), Toast.LENGTH_LONG);

		btn_add_data_point.setOnClickListener(this);
		
		Log.d(TAG, "Toast.makeText(this, getIntent().getExtras().getString(sheet_name), Toast.LENGTH_LONG);");
		
		SpreadSheetFactory spf = SpreadSheetFactory.getInstance();
		ArrayList<SpreadSheet> spreadsheets = spf.getSpreadSheet(getIntent().getExtras().getString("sheet_name"), true);
		mSs = spreadsheets.get(0);
		lv_records.setAdapter( new SpreadSheetAdapter(this, mSs ) );
		
		recordDataPoint(1);
	}
	
	void recordDataPoint(long val){
		if( null == mWs) {
			ArrayList<WorkSheet> wsArr = mSs.getWorkSheet("dataset", true);
			if( null == wsArr ) {
				mSs.addWorkSheet("dataset", new String[]{"firstcolumn", "secondcolumn"});
				wsArr = mSs.getWorkSheet("dataset", true);
			}
			mWs = wsArr.get(0);
			mWs.setColumns(mColumns);
		}
		HashMap<String, String> record = new HashMap<String, String>();
		record.put("firstcolumn", Long.toString( System.currentTimeMillis() ) );
		record.put("secondcolumn", Long.toString(val) );
		mWs.addRecord(mSs.getKey(), record);
		
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_add_data_point:
				recordDataPoint( Long.parseLong(et_value.getText().toString())  );
				break;
		default:
			break;
		}
		
	}
}
