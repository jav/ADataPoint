package com.devstates.adatapoint;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.javier.R;
import com.pras.SpreadSheet;
import com.pras.SpreadSheetFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


public class ADataPointMain extends Activity {

    private static final String APPLICATION_NAME = "ADataPointMain";
    private static final String TAG = "**********"+APPLICATION_NAME;
    private static final String PREF = APPLICATION_NAME+"pref";
    private static final String ACCOUNT_TYPE = "com.google";
	private static final int DIALOG_ACCOUNTS = 0;
	private static final int DIALOG_NEW_SHEET = 1;
	private static final int DIALOG_DELETE_SHEET = 2;

	private SharedPreferences mPref;
	
	private TextView txt_mainView;
	private ListView lv_datasets;

	private AccountManager mAccountManager;
	private Account mAccount;
	private SpreadSheetFactory mSpf;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPref = getSharedPreferences(PREF, MODE_PRIVATE);
        mAccountManager = AccountManager.get(this);
        
        lv_datasets = (ListView) findViewById(R.id.lv_datasets);
        txt_mainView = (TextView) findViewById(R.id.txt_app_name);
        txt_mainView.setText("Welcome to " + TAG);
        txt_mainView.append("\n");
        
        mAccount = fetchAccount(mPref.getString("account", null ) );
        if( null == mAccount ) showDialog(DIALOG_ACCOUNTS);
        
        if(null == mAccount) {
        	txt_mainView.setText("Not logged in.");
        }
        else {
        	updateView();
        }
        Log.d(TAG, "onCreate() : end");
    }
    
    private void updateView(){
    	txt_mainView.setText("Logged in as: " + mAccount.name);
    	spreadSheetsToListView(this, lv_datasets, mSpf, mAccount);
    }

	private void spreadSheetsToListView(Activity activity,
			final ListView listView, SpreadSheetFactory spf, Account account) {

		Log.d(TAG, "spreadSheetsToListView( ..., " + account.name + ");");
		
		ProgressDialog dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
		
		mSpf = SpreadSheetFactory.getInstance(account, this, this);
		
        updateDataSetsListView(listView);
        listView.setOnItemClickListener(new OnItemClickListener() {

        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d(TAG, "onItemClick()");
				switch(arg2) {
				case 0:
					showDialog(DIALOG_NEW_SHEET);
					break;
				default:
					Intent i = new Intent(getApplicationContext(), AddADataPoint.class);
					i.putExtra("sheet_name", listView.getItemAtPosition(arg2).toString() );
					Log.d(TAG, "startActivity()");
					startActivity(i);
					//Toast.makeText(getApplicationContext(), "Selected item: " + listView.getItemAtPosition(arg2).toString(), Toast.LENGTH_LONG).show();
					break;				
				}
			}


		});
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d(TAG, "onItemClick()");
				switch(arg2) {
				case 0:
					return true;
				default:
					Toast.makeText(getApplicationContext(), "Remove?: " + arg2, Toast.LENGTH_LONG).show();
					break;				
				}
				return false;
			}
		});
        dialog.dismiss();
        Log.d(TAG, "spreadSheetsToListView( ... ) : end");
	}

	private void newSpreadSheet(String newSheetName) {
		Log.d(TAG, "fetchAccount("+newSheetName+");");
		ProgressDialog dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
		String newSheetNameBase = new String("ADataSet_");
		String newSheetNameComposed = new String(newSheetNameBase + newSheetName);
		if(0 >= newSheetNameComposed.length()) newSheetNameComposed = new String(newSheetNameBase + "newSheet");
		ArrayList<SpreadSheet> spreadSheets = mSpf.getAllSpreadSheets(true, newSheetNameComposed, true);
		int i = 0;
		while (null != spreadSheets) {
			newSheetNameComposed = String.format(newSheetNameBase+newSheetName+"%02d", i++);
			
			Log.d(TAG, "updateDataSetsListview(): newSheetName = " + newSheetNameComposed);
			spreadSheets = mSpf.getAllSpreadSheets(true, newSheetNameComposed, true);

		}

		mSpf.createSpreadSheet(newSheetNameComposed);
		updateDataSetsListView(lv_datasets);
		dialog.dismiss();
		Toast.makeText(this, "Created a new sheet! ("+newSheetNameComposed+")", Toast.LENGTH_LONG).show();
	}
	
	private Account fetchAccount(String string) {
		Log.d(TAG, "fetchAccount("+string+");");
		Account account = null;
		if( null == string) return null;
		
		if( null != string){
			Log.d(TAG, "selectAccount: string is " + string);
			for( Account acc : mAccountManager.getAccountsByType(ACCOUNT_TYPE) ) { 
				if( 0 == acc.name.compareTo(string) ){
					account = acc;
					break;
				}
			}
		}
		if(null != account ) {
			SharedPreferences.Editor editor = mPref.edit();
			editor.putString("account", account.name );
			editor.commit();
		}
		return account ;
	}

	private void updateDataSetsListView(ListView lvDatasets) {
		Log.d(TAG, "updateDataSetsListview()");
        ArrayList<SpreadSheet> spreadSheets = mSpf.getAllSpreadSheets(true, "ADataSet_", false);
        if(null == spreadSheets) spreadSheets = new ArrayList<SpreadSheet>();
        
        String[] sheet_names = new String[spreadSheets.size()+1];
        sheet_names[0] = new String("New sheet...");
        for(int i = 0; i < spreadSheets.size(); i++) {
        	sheet_names[i+1] = spreadSheets.get(i).getTitle();
        	Log.d(TAG, "updateDataSetsListView()" +sheet_names[i+1] + "\n");
        }
		Log.d(TAG, "updateDataSetsListview() : lvDatasets.setAdapter()");
        lvDatasets.setAdapter(new SpreadSheetListAdapter(this, mSpf, true));
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;

		Builder builder = new AlertDialog.Builder(this);
		
		switch(id) {
		case DIALOG_ACCOUNTS:
			Log.d(TAG, "onCreateDialog() : id:"+Integer.toString(id));
			Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

			Log.d(TAG, "onCreateDialog() : accounts: " + Array.getLength(accounts));
			final String[] account_names = new String[Array.getLength(accounts)];
			for(int i = 0; i < Array.getLength(accounts); i++){
				account_names[i] = accounts[i].name;
				Log.d(TAG, "onCreateDialog() : account_names["+i+"]: " + accounts[i].name);

			}
			builder.setCancelable(false);
			builder.setTitle("Select an account");
			builder.setItems( account_names, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        fetchAccount( account_names[item] );
			        updateView();
			    }
			});
			dialog = builder.create();
			break;
		case DIALOG_NEW_SHEET:
			builder.setTitle("Title");
			builder.setMessage("Message");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			builder.setView(input);

			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				newSpreadSheet(value);
			  }
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
			dialog = builder.create();
			break;
		default: 
			dialog = null; 
			break;
			
		
		}
		return dialog;
	}
}