package src.com.ransomer.rmqon;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.rabbitmq.client.ConnectionFactory;

public class MQService extends Service{
	
	/**
	 * Base class for objects that connect to a RabbitMQ Broker
	 */
	public abstract class IConnectToRabbitMQ {
	      public String mServer;
	      public String mExchange;
	 
	      protected Channel mModel = null;
	      protected Connection  mConnection;
	 
	      protected boolean Running ;
	 
	      protected String MyExchangeType ;
	      
	      /**
	       * default constructor:
	       * @param server The server address
	       * @param exchange The named exchange
	       * @param exchangeType The exchange type name
	       */
	      public IConnectToRabbitMQ(String server, String exchange, String exchangeType)
	      {
	          mServer = server;
	          mExchange = exchange;
	          MyExchangeType = exchangeType;
	      }
    
	    //close channel and connection
	      public void Dispose()
	      {
	          Running = false;
	 
	            try {///if there is an open connection, close it
	                if (mConnection!=null)
	                    mConnection.close();
	                //if there is an open channel, close it
	                if (mModel != null)
	                    mModel.abort();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	 
	      }
	      
	      /**
	       * Connect to the broker and create the exchange
	       * @return success
	       */
	      public boolean connectToRabbitMQ()
	      {
	    	  ///Check if a reference to the channel exists and if it's open
	          if(mModel!= null && mModel.isOpen() )//already declared
	              return true;
	          try
	          {
	        	MyApplication.mFactory = new ConnectionFactory();
	      	    MyApplication.mFactory.setHost("137.140.3.151");
	      	    MyApplication.mFactory.setUsername("guest");
	      	    MyApplication.mFactory.setPassword("guest");
	              //factory.setPort(5672);
	              System.out.println(""+MyApplication.mFactory.getHost()+MyApplication.mFactory.getPort()+MyApplication.mFactory.getRequestedHeartbeat()+MyApplication.mFactory.getUsername());
	              MyApplication.mConnection = MyApplication.mFactory.newConnection();
	              mModel = mConnection.createChannel();
	              mModel.exchangeDeclare(mExchange, MyExchangeType, true);
	              
	 
	              return true;
	          }
	          catch (Exception e)
	          {
	              e.printStackTrace();
	              return false;
	          }
	      
	      
	      
	/*
	MQBinder provides getService() method for clients to retrieve the current
    instance of MQService
    */
	public class MQBinder extends Binder {
		MQService getService() {
		
		//Return this instance of MQService so clients can call public methods
	    return MQService.this;
	    }
	}
	
	@Override
     public void onCreate() {
           super.onCreate();
          
           
           Log.d("MQService", "Service created");
           Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();
           
                                
     }
	
	public void connectToRabbitMQ() throws IOException
    {
      
        	Log.d("MQService", "Attempting to connect to RabbitMQ broker...");
        	MyApplication.mFactory = new ConnectionFactory();
	      	MyApplication.mFactory.setHost("137.140.3.151");
	      	MyApplication.mFactory.setUsername("test");
	      	MyApplication.mFactory.setPassword("testrabbitmq");
	        MyApplication.mFactory.setPort(5672);
            
            MyApplication.mConnection = MyApplication.mFactory.newConnection();
        
    }
	public Runnable mRunnable = new Runnable(){  
		public void run() {

			try {
				connectToRabbitMQ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

		}
	};
     
	// For time consuming and long tasks you can launch a new thread here...
     public int onStartCommand(Intent intent, int flags, int startId)
     {
    	 
    	 if (sRunning) {
            // sRunning = true;
        	 Log.d("MQService", "MQService Already Started, start id " + startId + ": " + intent);

         }
    	 
    	 else{
    	
	    	 Toast.makeText(this, " MQService Started", Toast.LENGTH_LONG).show();
	    	 Thread ConnectToRabbitMQ = new Thread(mRunnable);
	         ConnectToRabbitMQ.start();
	         sRunning = true;
            
    	 }	 
    	 return START_STICKY;
     }
  
     @Override
     public void onDestroy() {
           super.onDestroy();
           
           Log.d("MQService", "Service Destroyed");
       
	
     }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}  
  
}