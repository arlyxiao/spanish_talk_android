package com.example.spanishtalk;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
		// TODO Auto-generated method stub
	}
	
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		Cursor cursor = (Cursor) vContactList.getItemAtPosition(position);
		String szDisplayName = cursor.getString(cursor.getColumnIndexOrThrow( ContactsContract.Contacts.DISPLAY_NAME));
		String szId = cursor.getString(cursor.getColumnIndexOrThrow( ContactsContract.Contacts._ID));
		int nId = cursor.getInt(cursor.getColumnIndexOrThrow( ContactsContract.Contacts._ID));
		
		Log.d(LOG_TAG, "Item click:"+position+" szId:"+szId+" nId:"+nId+" Data:"+szDisplayName);
		Toast.makeText(getBaseContext(), "Item click:"+position+" szId:"+szId+" nId:"+nId+" Data:"+szDisplayName, Toast.LENGTH_SHORT).show();

	}
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (contactAdapter!=null) {
			contactAdapter.getFilter().filter(s);
			vContactList.setAdapter(contactAdapter); 
		}

	}
}
