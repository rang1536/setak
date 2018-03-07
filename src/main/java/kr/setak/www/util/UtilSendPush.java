package kr.setak.www.util;

import java.io.IOException; 
 
 
import com.google.android.gcm.server.Message; 
import com.google.android.gcm.server.Result; 
import com.google.android.gcm.server.Sender; 

public class UtilSendPush {
	public void androidSendPush(String token, String activityName, String title, String content){ 
		try { 
		String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID 
		boolean SHOW_ON_IDLE = false;    //옙 활성화 상태일때 보여줄것인지 
		int LIVE_TIME = 1;    //옙 비활성화 상태일때 FCM가 메시지를 유효화하는 시간 
		int RETRY = 2;    //메시지 전송실패시 재시도 횟수 
				 
		String simpleApiKey = "AIzaSyB0er6AjPNVYE-vdzzsm3v2DWHw7KFH6pQ"; 
		String gcmURL = "https://android.googleapis.com/fcm/send"; 
		 
		 
		if(content==null || content.equals("")){ 
		content=""; 
		} 
		 		     
		content = new String(content.getBytes("UTF-8"), "UTF-8");   //메시지 한글깨짐 처리 
		Sender sender = new Sender(simpleApiKey); 
		Message message = new Message.Builder() 
			.collapseKey(MESSAGE_ID) 
			.delayWhileIdle(SHOW_ON_IDLE) 
			.timeToLive(LIVE_TIME) 
			.addData("activityName", activityName) 
			.addData("title",title) 
			.addData("content",content) 
			.build(); 
		Result result = sender.send(message,token,RETRY); 
		} catch (IOException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
	} 

}
