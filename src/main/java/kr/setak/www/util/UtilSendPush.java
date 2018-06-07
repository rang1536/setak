package kr.setak.www.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class UtilSendPush {
	public void androidSendPush(String token, String activityName, String title, String content){ 
		/*try { 
		String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID 
		boolean SHOW_ON_IDLE = false;    //옙 활성화 상태일때 보여줄것인지 
		int LIVE_TIME = 1;    //옙 비활성화 상태일때 FCM가 메시지를 유효화하는 시간 
		int RETRY = 2;    //메시지 전송실패시 재시도 횟수 
				 
		String simpleApiKey = "AAAAMmJ8IVQ:APA91bHy8nv_ET6kfmKx9Gdw-x53ftHy2uhsuAByY6gHQJVkgchfq9glWzW3BIviqcyP_dADhI_C6mAb4HhwDY7ViVyFeKMn9163pzWWasYRWMU3GNe3odi9sO0h8d1J2pS2fcJkAxPx"; 
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
		System.out.println("성공 : "+result.getSuccess());
		System.out.println("에러 : "+result.getErrorCodeName());
		System.out.println("뭐든 : "+result.getCanonicalRegistrationId());
		} catch (IOException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
			System.out.println(e.getMessage());
		} */
		
        /*String token = tokenList.get(count).getDEVICE_ID();*/
        
        final String apiKey = "AAAAMmJ8IVQ:APA91bHy8nv_ET6kfmKx9Gdw-x53ftHy2uhsuAByY6gHQJVkgchfq9glWzW3BIviqcyP_dADhI_C6mAb4HhwDY7ViVyFeKMn9163pzWWasYRWMU3GNe3odi9sO0h8d1J2pS2fcJkAxPx";
        try{
        	URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);

            conn.setDoOutput(true);
            
            /*String userId =(String) request.getSession().getAttribute("ssUserId");*/

            // 이렇게 보내면 주제를 ALL로 지정해놓은 모든 사람들한테 알림을 날려준다.
            /*String input = "{\"notification\" : {\"title\" : \"여기다 제목 넣기 \", \"body\" : \"여기다 내용 넣기\"}, \"to\":\"/topics/ALL\"}";*/
           
            // 이걸로 보내면 특정 토큰을 가지고있는 어플에만 알림을 날려준다  위에 둘중에 한개 골라서 날려주자
            String input = "{\"notification\" : {\"title\" : \""+ title +"\", \"body\" : \""+content+"\"}, \"to\":\""+token+"\", \"click_action\":\"android.intent.action.MAIN\" }";

            OutputStream os = conn.getOutputStream();
            
            // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
            os.write(input.getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + input);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        

	} 

}
