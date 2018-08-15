package com.example.chatprogram;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChatProgramHandler extends IoHandlerAdapter {

	private MainActivity myActivity;
	
	public ChatProgramHandler(MainActivity myActivity) {
		this.myActivity = myActivity;
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);
		System.out.println("connected");
		
		makeToast("Connected");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
		makeToast("Unexpected error occured");
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("Received : " + message);
		
		appendText(myActivity.chat_txt, message.toString());
	}

	private void makeToast(final String text) {
		myActivity.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	Toast.makeText(myActivity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	

	private void appendText(final TextView text,final String value){
		myActivity.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            text.append(value + "\n");
	        }
	    });
	}
	
}
