package kr.setak.www.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import kr.setak.www.dao.SetakDao;
import kr.setak.www.domain.Order;
import kr.setak.www.domain.OrderItem;
import kr.setak.www.domain.OrderedItem;
import kr.setak.www.domain.User;
import kr.setak.www.util.UtilSendPush;

@Service
public class SetakService {
	
	@Autowired
	private SetakDao setakDao;
	
	//상품조회
	public List<OrderItem> readAllItemServ(){
		return setakDao.selectAllItems();
	}
	
	//유저정보조회
	public User readUserInfoServ(String userId){
		return setakDao.selectUserInfo(userId);
	}
	
	//로그인
	public Map<String, Object> readLoginServ(String userId, String userHp){
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User();
		user.setUserId(userId);
		user.setUserHp(userHp);
		
		User loginUser = setakDao.selectLogin(user);
		if(loginUser == null){
			user= new User();
			user.setUserId(userId);
			loginUser = new User();
			loginUser = setakDao.selectLogin(user);
			
			if(loginUser == null){
				map.put("result", "noId"); //해당이름의 회원없음
			}else{
				map.put("result", "noPw"); //이름으로 조회된 회원이 있으면 비번(휴대폰번호) 불일치
			}
		}else{
			map.put("result", "success");
			map.put("user", loginUser);
		}
		return map;
	}
	
	//수거신청
	public Map<String, Object> addApplyOrderServ(Order order, String orderList){
		//orderList 배열로 세팅.
		orderList = orderList.replaceAll("[{}]", "");
		orderList = orderList.replaceAll("[\\[\\]]","");
		orderList = orderList.replaceAll("OrderItem", "");
		/*System.out.println("변환 값  확인 : "+orderList);*/
		String[] list = orderList.split(",");
		/*System.out.println("변환 값2  확인 : "+list);*/
		
		String value="";
		List<OrderedItem> orderedList = new ArrayList<OrderedItem>();
		OrderedItem orderedItem = new OrderedItem();
		for(int i=0; i<list.length; i++){
			if(i==0){
				value = list[i].split("=")[1];
				orderedItem.setNo(Integer.parseInt(value));
			}else if(i > 0 && (i%4) == 1){
				value = list[i].split("=")[1];
				orderedItem.setName(value.replaceAll("[\\'\\']", ""));
			}else if(i > 0 && (i%4) == 2){
				value = list[i].split("=")[1];
				orderedItem.setPrice(value.replaceAll("[\\'\\']", ""));
			}else if(i > 0 && (i%4) == 3){
				value = list[i].split("=")[1];
				orderedItem.setOrderCount(Integer.parseInt(value));
				
				orderedList.add(orderedItem);
				orderedItem = new OrderedItem();
			}else if(i > 0 && (i%4) == 0){
				value = list[i].split("=")[1];
				orderedItem.setNo(Integer.parseInt(value));				
			}
		}
		/*System.out.println("배열확인 : "+orderedList);*/
		
		//유저pk조회
		User user = new User();
		user.setUserId(order.getUserId());
		user.setUserHp(order.getUserHp().replaceAll("-", ""));
		User selectUser = setakDao.selectLogin(user);
		
		order.setUserNo(selectUser.getUserNo());
		
		//총가격 세팅
		int totalPrice = 0;
		for(int i=0; i<orderedList.size(); i++){
			totalPrice += Integer.parseInt(orderedList.get(i).getPrice()) * orderedList.get(i).getOrderCount();
		}
		
		order.setTotalPrice(String.valueOf(totalPrice));
		
		//주문등록
		Map<String, Object> map = new HashMap<String, Object>();
		String userProduct = null;
		int successCount = 0;
		int result = setakDao.insertApplyOrder(order);
		if(result == 1){ //주문등록성공
			//주문상품등록
			for(int i=0; i<orderedList.size(); i++){
				orderedList.get(i).setOrderNo(order.getOrderNo());
				int itemRes = setakDao.insertOrderedItem(orderedList.get(i));
				if(i == 0) userProduct += orderedList.get(i).getName();
				else if(i > 0) userProduct = userProduct+", "+orderedList.get(i).getName();
				if(itemRes == 1) successCount++;
			}
			if(orderedList.size() == successCount){
				System.out.println("주문상품 전체등록 성공~!!");
				map.put("result", "success");
			}else{
				System.out.println("주문상품 전체등록 실패~!!");
				map.put("result", "fail");
			}
		}else{
			System.out.println("주문등록 실패~!!");
			map.put("result", "fail");
		}
		
		//푸쉬알림 - 고객
		UtilSendPush sendPush = new UtilSendPush();
		String userTitle = "세탁풍경 수거신청완료";
		String userContent = "수거신청이 완료되었습니다. 담당자 확인 후 요청하신 주소로 수거 하러 가겠습니다. 감사합니다";
		sendPush.androidSendPush(selectUser.getToken(), "applyList", userTitle, userContent);
		
		//푸쉬알림 - 관리자
		List<User> staffList = setakDao.selectStaffNAdmin();
		String userName = selectUser.getUserId();
		String userPhone = selectUser.getUserHp();
		String applyDate = order.getInDate();
		String address = order.getDeliveryAdd();
		
		String title = "[세탁물 수거신청]" + userName +"("+userPhone+")님 신청";
		String content = "신청일-"+applyDate+"\n";
		content += "세탁물-"+userProduct +"\n";
		content += "주소-"+address;
		
		for(int i=0; i<staffList.size(); i++){
			sendPush.androidSendPush(staffList.get(i).getToken(), "staff", title, content);
		}
		
		return map;
	}
	
	//웹, 물품등록
	public Map<String, Object> addSetakItemServ(OrderItem orderItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(setakDao.insertSetakItem(orderItem) == 1) {
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//웹, 물품수정
	public Map<String, Object> modifySetakItemServ(OrderItem orderItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(setakDao.updateSetakItem(orderItem) == 1) {
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//웹, 물품삭제
	public Map<String, Object> removeSetakItemServ(int no) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(setakDao.deleteSetakItem(no) == 1) {
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//웹, 하나의 물품조회
	public OrderItem readItemServ(int no) {
		return setakDao.selectItem(no);
	}
	
	//웹, 스탭조회
	public List<User> readStaffAllServ(){
		return setakDao.selectStaffAll();
	}
	
	//웹, 스탭등록
	public Map<String, Object> addStaffServ(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(setakDao.insertStaff(user) == 1) {
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//웹, 스탭수정
	public Map<String, Object> modifyStaffServ(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(setakDao.updateStaff(user) == 1) {
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//웹, 스탭삭제
	public Map<String, Object> removeStaffServ(int userNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(setakDao.deleteStaff(userNo) == 1) {
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//웹, 하나의스탭조회
	public User readStaffServ(int userNo){
		return setakDao.selectStaff(userNo);
	}
	
	//토큰 저장
	public Map<String, Object> modifyTokenServ(int userNo, String token){
		User user = new User();
		user.setToken(token);
		user.setUserNo(userNo);
		
		Map<String, Object> map = new HashMap<String, Object>();
		int result = setakDao.updateUserToken(user);
		if(result == 1) {
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		return map;
	}
	
	//회원가입
	public Map<String, Object> addUserServ(User user){
		Map<String, Object> map = new HashMap<String, Object>();
		//중복회원 있는지 검색
		List<User> userList = setakDao.selectSameUserCheck(user);
		if(userList.size() > 0){
			for(int i=0; i<userList.size(); i++){
				if(user.getUserHp() == userList.get(i).getUserHp()){
					map.put("result", "exist");
				}
			}
		}else if(userList.size() == 0){
			int result = setakDao.insertUser(user);
			if(result == 1){
				map.put("result", "success");
				map.put("userNo", user.getUserNo());
				map.put("userId", user.getUserId());
				map.put("userHp", user.getUserHp());
				map.put("userGrade", "user");
				
				//푸쉬알림 - 고객
				/*UtilSendPush sendPush = new UtilSendPush();
				String userTitle = "세탁풍경 회원가입완료";
				String userContent = "세탁풍경의 가족이 되신걸 환영합니다.";
				sendPush.androidSendPush(user.getToken(), "main", userTitle, userContent);*/
			}else{
				map.put("result", "fail");
			}
		}
		
		
		
		return map;
	}
}
