/**
 * 
 */
package com.example.mobilevocabulary.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilevocabulary.R;

/**
 * @author hungndv
 *
 */
@SuppressLint("DefaultLocale") public class FileChooserDialogFragment extends DialogFragment {
	private ListView lvBrowser;
	private File currentDir;
	private FileArrayAdapter adapter;
	public String CanonicalFilePath;
	public String CanonicalDirPath;
	public boolean IsDirectoryChooser = false;
	public String FileType = "";
	private Button btnCancel;
	private Button btnOK;
	private TextView tvPath;

	/** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.file_chooser_dialog, container, false);

		btnCancel = (Button)layoutView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		
		btnOK = (Button)layoutView.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onDialogPositiveClick(FileChooserDialogFragment.this);
			}
		});
		
		tvPath = (TextView)layoutView.findViewById(R.id.tvPath);
		
		lvBrowser = (ListView)layoutView.findViewById(R.id.lvBrowser);
		lvBrowser.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tvName = (TextView)arg1.findViewById(R.id.tvName);
				Option o = (Option)tvName.getTag();
				File newDir = new File(o.getPath());
				if (!newDir.exists()){
					Log.i("", "The path[" + o.path + "] doesn't exist.");
				} else {
					String tmpPath = null;
					try {
						tmpPath = newDir.getCanonicalPath();
						//getDialog().setTitle(tmpPath);
						tvPath.setText(tmpPath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (newDir.isDirectory()){
						currentDir = newDir;
						CanonicalDirPath = tmpPath;
						fill(currentDir);
					} else {
						CanonicalFilePath = tmpPath;
					}
					
				}
				
				
			}
			
		});
		
		currentDir = Environment.getExternalStorageDirectory();
		try {
			CanonicalDirPath = currentDir.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//getDialog().setTitle(CanonicalDirPath);
		tvPath.setText(CanonicalDirPath);
		
		fill(currentDir);
		
		// Inflate the layout to use as dialog or embedded fragment		
		return layoutView;
	}
	
	 /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

	private void fill(File f){
		File[] dirs = f.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory())
					return true;
				return pathname.getName().toLowerCase().endsWith(FileType.toLowerCase());
			}
		});
		
		List<Option>dir = new ArrayList<Option>();
		List<Option>fls = new ArrayList<Option>();
		try{
			for(File ff: dirs){
				if(ff.isDirectory())
					dir.add(new Option(ff.getName(), "", ff.getCanonicalPath()));
				else{
					if (!IsDirectoryChooser){
						fls.add(new Option(ff.getName(), "[File] ", ff.getCanonicalPath()));
					}
				}
			}
		}catch(Exception e){
		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		
		if(!f.getPath().equalsIgnoreCase("/"))
			dir.add(0, new Option("[..]", "", f.getParent()));
		
		adapter = new FileArrayAdapter(getActivity(), R.layout.file_chooser_item, dir);
		lvBrowser.setAdapter(adapter);
	}

	public class FileArrayAdapter extends ArrayAdapter<Option>{

		private Context c;
		private int id;
		private List<Option>items;

		public FileArrayAdapter(Context context, int textViewResourceId,
				List<Option> objects) {
			super(context, textViewResourceId, objects);
			c = context;
			id = textViewResourceId;
			items = objects;
		}
		public Option getItem(int i)
		{
			return items.get(i);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(id, null);
			}
			final Option o = items.get(position);
			if (o != null) {
				TextView tvName = (TextView) v.findViewById(R.id.tvName);
				TextView tvType = (TextView) v.findViewById(R.id.tvType);

				if(tvName!=null)
					tvName.setText(o.getName());
				if(tvType!=null)
					tvType.setText(o.getType());
				tvName.setTag(o);				
			}
			return v;
		}
	}

	@SuppressLint("DefaultLocale") public class Option implements Comparable<Option>{
		private String name;
		private String type;
		private String path;

		public Option(String n, String t, String p){
			name = n;
			type = t;
			path = p;
		}
		public String getName(){
			return name;
		}
		public String getType(){
			return type;
		}
		public String getPath(){
			return path;
		}
		@SuppressLint("DefaultLocale") @Override
		public int compareTo(Option o){
			if(this.name != null)
				return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
			else 
				throw new IllegalArgumentException();
		}
	}
}
