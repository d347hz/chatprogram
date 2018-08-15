package com.example.chatprogram;


import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button sendMessage_btn;
	private EditText message_txt;
	public TextView chat_txt;
	private static SocketConnector socket;
	private IoSession session;
	private PrintWriter out;
	private MainActivity myActivity;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sendMessage_btn = findViewById(R.id.sendMessage_btn);
		message_txt = findViewById(R.id.message_txt);
		chat_txt = findViewById(R.id.chat_txt);
		
		chat_txt.setText("");
		chat_txt.setMovementMethod(new ScrollingMovementMethod());
		
		myActivity = this;
		
		socket = new NioSocketConnector();
		socket.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
		socket.setHandler(new ChatProgramHandler(myActivity));
		myTask mt = new myTask();
		mt.execute();
		
		sendMessage_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(message_txt.getText().length() > 0) {
					sendMessageTask smt = new sendMessageTask();
					smt.execute();
				}
			}
		});
	}
	
	class sendMessageTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			
			session.write(message_txt.getText());
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			message_txt.setText("");
			
			InputMethodManager imm = (InputMethodManager)
			getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		}
	}
	
	class myTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			ConnectFuture future = socket.connect(new InetSocketAddress("87.97.216.145", 8888));
            future.awaitUninterruptibly();
            
            try {
            	session = future.getSession();
            } catch (Exception e) {
            	System.out.println("Error occured");
            }
			
			return null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
