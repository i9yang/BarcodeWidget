package com.i9yang.barcode;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.i9yang.barcode.exam.HelloProvider;
import com.i9yang.barcode.exam.HelloService;

public class ExamActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_activity);
	}

	public void startService(View view) {
		startService(new Intent(getBaseContext(), HelloService.class));
	}

	public void stopService(View view) {
		stopService(new Intent(getBaseContext(), HelloService.class));
	}

	public void broadcastIntent(View view) {
		Intent intent = new Intent("com.i9yang.CUSTOM_INTENT");
		sendBroadcast(intent);
	}

	public void onClickAddName(View view) {
		ContentValues values = new ContentValues();
		values.put(HelloProvider.NAME, ((EditText) findViewById(R.id.txtName)).getText().toString());
		values.put(HelloProvider.GRADE, ((EditText) findViewById(R.id.txtGrade)).getText().toString());

		Uri uri = getContentResolver().insert(HelloProvider.CONTENT_URI, values);
		Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
	}

	public void onClickRetrieveStudents(View view) {
		// Retrieve student records
		String URL = "content://com.i9yang.barcode/students";
		Uri students = Uri.parse(URL);
		Cursor c = managedQuery(students, null, null, null, "name");
		if (c.moveToFirst()) {
			do {
				Toast.makeText(this,
					c.getString(c.getColumnIndex(HelloProvider._ID)) +
						", " + c.getString(c.getColumnIndex(HelloProvider.NAME)) +
						", " + c.getString(c.getColumnIndex(HelloProvider.GRADE)),
					Toast.LENGTH_SHORT).show();
			} while (c.moveToNext());
		}
	}

	public void forceCrash(View view) {
		throw new RuntimeException("This is a crash");
	}

	public void call(View view) {
		Uri number = Uri.parse("tel:5551234");
		Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
		startActivity(callIntent);
	}

	public void goFragment(View view) {
		Intent intent = new Intent(this, FragmentActivity.class);
		startActivity(intent);
	}
}
