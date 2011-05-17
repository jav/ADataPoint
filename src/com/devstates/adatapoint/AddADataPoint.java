package com.devstates.adatapoint;

import java.util.ArrayList;

import com.javier.R;
import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddADataPoint extends Activity {
	private static final String TAG = "AddADataPoint";
	private TextView txt_dataset;
	private DigitalClock dc_clock;
	private EditText et_value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
        setContentView(R.layout.add_a_data_point);
		Log.d(TAG, "setContentView(R.layout.add_a_data_point);");
		
		txt_dataset = (TextView) findViewById(R.id.txt_data_set_name);
		et_value = (EditText) findViewById(R.id.et_value);
		dc_clock = (DigitalClock) findViewById(R.id.dc_time);
		
		if( null == txt_dataset ) Log.d(TAG, "txt_dataset is null;");
		else Log.d(TAG, "txt_dataset is NOT null;");
		txt_dataset.setText(getIntent().getExtras().getString("sheet_name"));
		Toast.makeText(this, getIntent().getExtras().getString("sheet_name"), Toast.LENGTH_LONG);

		dc_clock.setEnabled(true);
		dc_clock.setText("broken string?");
		
		Log.d(TAG, "Toast.makeText(this, getIntent().getExtras().getString(sheet_name), Toast.LENGTH_LONG);");
		
		SpreadSheetFactory spf = SpreadSheetFactory.getInstance();
		ArrayList<SpreadSheet> spreadsheets = spf.getSpreadSheet(getIntent().getExtras().getString("sheet_name"), true);
		SpreadSheetAdapter foo = new SpreadSheetAdapter(this, spreadsheets.get(0) );
	}
}
