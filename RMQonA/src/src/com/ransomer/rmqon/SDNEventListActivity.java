package src.com.ransomer.rmqon;

import java.io.UnsupportedEncodingException;

import com.ransomer.rmqona.R;

import src.com.ransomer.rmqon.MessageConsumer;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.TextView;



/**
 * An activity representing a list of SDNEvents. This activity has different
 * presentations for handsets and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link SDNEventDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SDNEventListFragment} and the item details (if present) is a
 * {@link SDNEventDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link SDNEventListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class SDNEventListActivity extends FragmentActivity implements
		SDNEventListFragment.Callbacks {
	
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	//MQService mService;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdnevent_list);
        Log.d("SDNEventListActivity", "Activity created");
        
        private MessageConsumer mInfo;
        private MessageConsumer mLogService;
        private MessageConsumer mRestlet;
        private MessageConsumer	mServerRouter;
        private MessageConsumer	mVirtualHost;
        
        
        //Get SDN events from a new data model
        SDNEventQueue infoAll = new SDNEventQueue("Info(All)");
        SDNEventQueue debugLogService = new SDNEventQueue("Debug.LogService");
        SDNEventQueue debugRestlet = new SDNEventQueue("Debug.Restlet");
        SDNEventQueue debugServerRouter = new SDNEventQueue("Debug.ServerRouter");
        SDNEventQueue debugVirtualHost = new SDNEventQueue("Debug.VirtualHost");
        
		if (findViewById(R.id.sdnevent_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((SDNEventListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.sdnevent_list))
					.setActivateOnItemClick(true);
		}
		
		
        //startService(new Intent(this,MQService.class));
		//The output TextView we'll use to display messages
        mOutput =  (TextView) findViewById(R.id.sdnevent_detail);
 
        //Create the consumer
        mConsumer = new MessageConsumer("137.140.3.151",
                "sdn_events",
                "topic");
 
        //Connect to broker
        mConsumer.connectToRabbitMQ();
 
        //register for messages
        mConsumer.setOnReceiveMessageHandler(new OnReceiveMessageHandler(){
 
            public void onReceiveMessage(byte[] message) {
                String text = "";
                try {
                    text = new String(message, "UTF8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
 
                mOutput.append("\n"+text);
            }
        });
				
		};
		
			
	
	/**
	 * Callback method from {@link SDNEventListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(SDNEventDetailFragment.ARG_ITEM_ID, id);
			SDNEventDetailFragment fragment = new SDNEventDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.sdnevent_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, SDNEventDetailActivity.class);
			detailIntent.putExtra(SDNEventDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
			
	@Override
    protected void onStart() {
        super.onStart();
        
        Log.d("SDNEventListActivity", "Activity started");
        
    }
	
	
	@Override
	public void onResume() {
		super.onResume();
		mConsumer.connectToRabbitMQ();
		Log.d("SDNEventListActivity", "Activity Resumed");
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		
		Log.d("SDNEventListActivity", "Activity Restarted");
	}
	
	 @Override
	 public void onPause() {
		 super.onPause();
		 mConsumer.dispose();
		 Log.d("SDNEventListActivity", "Activity Paused...");
	     
	 }
	 
	 @Override
	 public void onStop() {
		 super.onStop();
		 
		 
		 
		 Log.d("SDNEventListActivity", "Activity Stopped...");
		 
	 }
	 
	 @Override
	 public void onDestroy() {
		 super.onDestroy();
		
		 Log.d("SDNEventListActivity", "Activity Destroyed...");
	     
	 }
 
}
