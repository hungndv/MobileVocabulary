/**
 * 
 */
package com.example.mobilevocabulary.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.example.mobilevocabulary.R;

/**
 * @author hungndv
 *
 */
public class Dialogs {
	
	public static void showFailureDailog(Context context, String title, String message){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title)
		.setMessage(message)
		.setCancelable(true)
		.setNegativeButton(context.getResources().getString(R.string.button_ok), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
