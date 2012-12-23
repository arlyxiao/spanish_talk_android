package com.example.spanishtalk;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactActivity extends Activity implements OnClickListener, TextWatcher,OnItemClickListener {
	final String 			LOG_TAG			= "SpanishTalk";
	EditText				vSearchBtn;
	ListView				vContactList;
	
	Cursor								contactCursor;
	SimpleCursorAdapter 				contactAdapter;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        
        Log.d(LOG_TAG, "onCreate called.");
        
        vContactList = (ListView) findViewById(R.id.contact_list);
        vSearchBtn = (EditText) findViewById(R.id.contact_search_btn);

		vContactList.setOnItemClickListener((OnItemClickListener) this);
		vSearchBtn.addTextChangedListener((TextWatcher) this);

		ReadContacts("");
    }
    
    void ReadContacts(String sort) {
		// final Uri uri = ContactsContract.Contacts.CONTENT_URI;
		final Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        final String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        //boolean mShowInvisible = false;
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
        String[] selectionArgs = null;
        final String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +  " COLLATE LOCALIZED ASC";

        contactCursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
        String[] fields = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        contactAdapter = new SimpleCursorAdapter(this, 
        		R.layout.contact_item,
        		contactCursor,
                fields, 
                new int[] {R.id.contact_name, R.id.contact_phone_number});

        contactAdapter.setFilterQueryProvider(new FilterQueryProvider() {
     
			public Cursor runQuery(CharSequence constraint) {
				Log.d(LOG_TAG, "runQuery constraint:"+constraint);
				String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'" +
					" AND "+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%"+constraint+"%'";
				String[] selectionArgs = null;//new String[]{"'1'"};//, };
				Cursor cur = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
				return cur;
			}
        
        });
        vContactList.setAdapter(contactAdapter); 
      //  cur.close();
		 
	}

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy called.");
        if (contactCursor!=null) contactCursor.close();
        vSearchBtn.removeTextChangedListener(this);

	}

	public void onClick(View v) {
	}
	
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		Cursor cursor = (Cursor) vContactList.getItemAtPosition(position);
		String number = cursor.getString(cursor.getColumnIndexOrThrow( ContactsContract.CommonDataKinds.Phone.NUMBER ));
 		
		vSearchBtn.setText(number);
	}
	public void afterTextChanged(Editable s) {
		
	}
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (contactAdapter!=null) {
			contactAdapter.getFilter().filter(s);
			vContactList.setAdapter(contactAdapter); 
		}

	}
	
	public void sendSMS(View view)
    {   
		// String number = vSearchBtn.getText().toString();
		String number = "13960418536";
		String message = "one";
        
		sendSMS(number, message);
    }
	
	private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
    }
}
