package com.example.mobilevocabulary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobilevocabulary.R;
import com.example.mobilevocabulary.model.MultipleChoiceTopic;
import com.example.mobilevocabulary.util.Misc;

public class ResultActivity extends ActionBarActivity {
	private TextView tvScore;
	private TextView tvElapsedTime;
	private TextView tvTopic;
	MultipleChoiceTopic topic;
	
	private Button btnRetake;
	private Button btnBackToMain;
	
//	private AdView adView;
//	private static final String AD_UNIT_ID = "295c3bc7-ff45-4dac-ae81-447fc4d146eb";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		final Intent intent = getIntent();
		topic = (MultipleChoiceTopic)(intent.getSerializableExtra(ExamMultipleChoiceSwipeActivity.RESULT));
		
		tvTopic = (TextView)findViewById(R.id.tvTopic);
		tvTopic.setText(topic.Text);
		tvScore = (TextView)findViewById(R.id.tvScore);
		tvScore.setText(topic.CorrectCount+"/"+topic.QuestionCount+" ("+(int)(((float)topic.CorrectCount/topic.QuestionCount)*100)+"%) ");
		tvElapsedTime = (TextView)findViewById(R.id.tvElapsedTime);
		tvElapsedTime.setText(Misc.clockTextFromMilliseconds(topic.RemainingTime));
		
		btnRetake = (Button)findViewById(R.id.btnRetake);
		btnRetake.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ExamMultipleChoiceSwipeActivity.class);
				intent.putExtra(TopicSelectionActivity.TOPIC, topic);
				startActivity(intent);
			}
		});
		
		btnBackToMain = (Button)findViewById(R.id.btnBackToMain);
		btnBackToMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		});
		
		// Create an ad.
//	    adView = new AdView(this);
//	    adView.setAdSize(AdSize.BANNER);
//	    adView.setAdUnitId(AD_UNIT_ID);
//	    
//	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl);
//	    layout.addView(adView);
//
//	    // Create an ad request. Check logcat output for the hashed device ID to
//	    // get test ads on a physical device.
//	    AdRequest adRequest = new AdRequest.Builder()
//	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//	        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
//	        .build();
//
//	    // Start loading the ad in the background.
//	    adView.loadAd(adRequest);
	}
	
//	@Override
//	protected void onStart() {
//	    super.onStart();
//	    Thread thr = new Thread(new Runnable() {
//	        @Override
//	        public void run() {
//	        	Context ctx = ResultActivity.this.getApplicationContext();
//	        	AdvertisingIdClient.Info adInfo = null;
//	        	try {
//	        		adInfo = AdvertisingIdClient.getAdvertisingIdInfo(ctx);
//	        	} catch (IllegalStateException e) {
//	        		e.printStackTrace();
//	        	} catch (GooglePlayServicesRepairableException e) {
//	        		e.printStackTrace();
//	        	} catch (IOException e) {
//	        		e.printStackTrace();
//	        	} catch (GooglePlayServicesNotAvailableException e) {
//	        		e.printStackTrace();
//	        	}
//	        	Log.i("aaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaaaa-"+adInfo.getId());
//	        }
//	    });
//
//	    thr.start();
//	}
	
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (adView != null) {
//			adView.resume();
//		}
//	}

//	@Override
//	public void onPause() {
//		if (adView != null) {
//			adView.pause();
//		}
//		super.onPause();
//	}

//	/** Called before the activity is destroyed. */
//	@Override
//	public void onDestroy() {
//		// Destroy the AdView.
//		if (adView != null) {
//			adView.destroy();
//		}
//		super.onDestroy();
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    @Override
    public void onBackPressed() {
        // your code.
    	Intent intent = new Intent(getApplicationContext(), TopicSelectionActivity.class);
    	startActivity(intent);
    }
	
}
