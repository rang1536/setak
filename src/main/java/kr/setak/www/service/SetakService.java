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
	
	//마스터로그인
	public Map<String, Object> masterLoginServ(String loginUserId){
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User();
		user.setUserHp(loginUserId);
		user.setUserGrade("master");
		
		List<User> list = setakDao.selectMasterLogin(user);
		System.out.println("리스트확인 : "+list);
		if(list.size() == 1) {
			map.put("result", "login");
			System.out.println("로그인");
		}else if(list.size() == 0) {
			map.put("result", "noData");
			System.out.println("데이터없음");
		}else if(list.size() > 1) {
			map.put("result", "manyData");
			System.out.println("너무많음");
		}
		return map;
	}
	
	
	//로그인
	public Map<String, Object> readLoginServ(String userId, String userHp, int userNo){
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User();
		user.setUserId(userId);
		user.setUserHp(userHp);
		
		System.out.println("세팅값 확인 : "+user);
		User loginUser = new User();
		if(userNo == 0) {
			loginUser = setakDao.selectLogin(user);
		}else if(userNo != 0) {
			loginUser = setakDao.selectLogin2(userNo);
		}
		
		System.out.println("loginUser 확인 : "+loginUser);
		
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
			System.out.println("로그인성공");
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
		
		//유저 조회
		User selectUser = setakDao.selectUserForApp(order.getUserNo());
		System.out.println("selectUser확인 : "+selectUser);
		
		//주소, 세탁상태등 저장
		selectUser.setUserAdd(order.getOrderAdd()); //주소세팅
		selectUser.setSangseAdd(order.getSangseAdd());
		selectUser.setUserHp(order.getUserHp());
		selectUser.setOrderCount(selectUser.getOrderCount()+1);
		int updateResult = setakDao.updateSetakState(selectUser);
		
		//총가격 세팅
		int totalPrice = 0;
		for(int i=0; i<orderedList.size(); i++){
			totalPrice += Integer.parseInt(orderedList.get(i).getPrice()) * orderedList.get(i).getOrderCount();
		}
		
		order.setTotalPrice(String.valueOf(totalPrice));
		
		//주문등록
		Map<String, Object> map = new HashMap<String, Object>();
		String userProduct = "";
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
		String userTitle = "수거신청완료 - 당겨주세요 ▼";
		String userContent = "수거신청이 완료되었습니다. \n 수거하실 기사님을 배정중입니다.\n 감사합니다";
		sendPush.androidSendPush(selectUser.getToken(), "applyList", userTitle, userContent);
		
		order = setakDao.selectOrderByOrderNo(order.getOrderNo());
		//푸쉬알림 - 관리자
		List<User> staffList = setakDao.selectStaffNAdmin();
		String userName = selectUser.getUserId();
		String userPhone = selectUser.getUserHp();
		String applyDate = order.getInDate();
		String address = order.getDeliveryAdd();
		
		String title = "세탁물 수거신청\n" + userName +"("+userPhone+")님 신청 ▼";
		String content = "신청일-"+applyDate+"\n";
		content += "세탁물-"+userProduct +"\n";
		content += "주소-"+address;
		
		for(int i=0; i<staffList.size(); i++){
			sendPush.androidSendPush(staffList.get(i).getToken(), "staff", title, content);
		}
		
		return map;
	}
	
	//웹, 단체푸쉬발송
	public Map<String, Object> sendPushServ(String sendMsg){
		Map<String, Object> map = new HashMap<String, Object>();
		List<User> list = setakDao.selectUserAll();
		
		UtilSendPush sendPush = new UtilSendPush();
		String title = "세탁풍경 알림 - 당겨주세요 ▼";
		/*sendPush.androidSendPush("c9btZruq3Rg:APA91bEweKgNWgQb_xWtkcH3EaWpRrW6gXYzpa8ac_PKq1HNz6HDuKgJbrUShgogOvBhFXp3kwStWm1ssUtzg2e4FatuzBt8xT98ocRdwbYl15kJ3QMcfsgGccw3Jja29t_ZAUwXUHOZ", "all", title, sendMsg);*/
		for(int i=0; i<list.size(); i++){
			System.out.println(i+"번째 발송 토큰확인 : "+list.get(i).getToken());
			sendPush.androidSendPush(list.get(i).getToken(), "all", title, sendMsg);
		}
		map.put("result", "success");
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
	public Map<String, Object> modifyTokenServ(User user, String token, String type){
		Map<String, Object> map = new HashMap<String, Object>();
		if(type.equals("faceBook")) {
			if(user.getGender().equals("male")) {
				user.setGender("M");
			}else {
				user.setGender("F");
			}
			
		}
		int result = 0;
		int count = 0;
		if(type.equals("faceBook")) {
			count = setakDao.selectUserByToken(user);
		}else if(type.equals("kakao")) {
			count = setakDao.selectUserByToken(user);
		}else {
			count = setakDao.selectUserByToken(user);
		}
		
		User loginUser = new User();
		if(count == 0) { //신규기기 인서트
			result = setakDao.insertUserToken(user);
			System.out.println("토큰 등록완료~!!");
		}else if(count > 0){ //기존기기
			if(type.equals("login")) {
				result = setakDao.updateUserToken(user);
				System.out.println("토큰 수정완료1~!!");
			}else {
				result = setakDao.updateUserTokenBySns(user);
				System.out.println("토큰 수정완료2~!!");
				loginUser = setakDao.selectUserBySnsId(user);
			}							
		}
		
		if(result == 1 && count == 0) {
			map.put("result", "newUser");
			map.put("userNo", user.getUserNo());
			map.put("userId", user.getUserId());
			map.put("userGrade", "user");
			System.out.println("신규유저~!!");
			
			//푸쉬알림 - 고객
			/*UtilSendPush sendPush = new UtilSendPush();
			String userTitle = "세탁풍경";
			String userContent = "세탁풍경의 가족이 되신걸 환영합니다.";
			sendPush.androidSendPush(user.getToken(), "main", userTitle, userContent);*/
			
		}else if(result == 1 && count > 0){
			map.put("result", "success");
			map.put("userNo", loginUser.getUserNo());
			map.put("userId", loginUser.getUserId());
			map.put("userGrade", "user");
			System.out.println("기존유저~!!");
		}else if(result == 0){
			map.put("result", "fail");
			System.out.println("수정실패??~!!");
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
				map.put("userHp", user.getUserHp().replaceAll("-", ""));
				map.put("userGrade", "user");
				
				//푸쉬알림 - 고객
				UtilSendPush sendPush = new UtilSendPush();
				String userTitle = "세탁풍경 알림 - 당겨주세요 ▼";
				String userContent = "세탁풍경의 가족이 되신걸 환영합니다.";
				sendPush.androidSendPush(user.getToken(), "main", userTitle, userContent);
			}else{
				map.put("result", "fail");
			}
		}
		return map;
	}
	
	//하나의 회원조회
	public Map<String, Object> readUserForAppServ(int userNo){
		User user = setakDao.selectUserForApp(userNo);
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(user.getUserAdd() == null) user.setUserAdd("none");
		map.put("user", user);
		
		return map;
	}
	
	//회원탈퇴
	public Map<String, Object> removeUserServ(User user){
		int result = setakDao.deleteUser(user);
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(result == 1) map.put("result", "success");
		else map.put("result", "fail");
		
		return map;
	}
	
	//세탁주문조회 (완료아님)
	public Map<String, Object> readSetakListServ(User user){
		List<Order> list = setakDao.selectOrderList(user.getUserNo());
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(list != null) {
			for(int i=0; i<list.size(); i++) {
				List<OrderedItem> orderList = setakDao.selectOrderedItemAll(list.get(i).getOrderNo());
				list.get(i).setOrderList(orderList);
			}
		}
		
		System.out.println("세탁물 조회결과 최종확인 :"+list);
		map.put("list", list);
		return map;
	}
	
	//세탁주문조회 (완료)
	public Map<String, Object> readCompleteListServ(User user, String type){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Order> list = new ArrayList<Order>();
		if(type.equals("none")) {
			list = setakDao.selectCompleteList(user.getUserNo());								
		}else if(type.equals("staff")) {
			list = setakDao.selectListByStaffNo(user.getUserNo());					
		}
		
		
		if(list != null) {
			for(int i=0; i<list.size(); i++) {
				List<OrderedItem> orderList = setakDao.selectOrderedItemAll(list.get(i).getOrderNo());
				list.get(i).setOrderList(orderList);
				list.get(i).setUserHp(setakDao.selectHpForApp(list.get(i).getUserNo()));
			}
		}
		map.put("list", list);
		return map;
	}
	
	//회원정보수정
	public Map<String, Object> modifyUserServ(User user){
		Map<String, Object> map= new HashMap<String, Object>();
		//기존데이터 중에 같은 핸드폰번호 존재하는지 확인
		List<User> list = setakDao.selectUserByHp(user.getUserHp());
		if(list.size() >= 1) {
			for(int i=0; i<list.size();i++) {
				if(list.get(0).getUserNo() != user.getUserNo()) {
					map.put("result", "exist");
				}
			}			
		}
		
		int result = setakDao.updateUserInfoByApp(user);
		
		if(result == 1) map.put("result", "success");
		else if(result == 0) map.put("result", "fail");
		return map;
	}
	
	//배정대기중 목록 조회
	public Map<String, Object> readListStatOrderServ(){
		Map<String, Object> map= new HashMap<String, Object>();
		List<Order> list = setakDao.selectOrderByStateStay();
		
		System.out.println("수거대기 목록확인 : "+list);
		if(list != null) {
			for(int i=0; i<list.size(); i++) {
				List<OrderedItem> orderList = setakDao.selectOrderedItemAll(list.get(i).getOrderNo());
				list.get(i).setOrderList(orderList);
			}
		}
		map.put("list", list);
		return map;
	}
	
	//수거기사배정
	public Map<String, Object> staffCheckServ(User user, Order order){
		Map<String, Object> map= new HashMap<String, Object>();
		//먼저 배정기사 있는지 확인
		Order orderCheck = setakDao.selectOrderByOrderNo(order.getOrderNo());
		if(!orderCheck.getState().equals("1")) { //대기상태가 아닐때
			/*if(orderCheck.getState().equals("2")) {
				order.setReceiveUserNo(user.getUserNo());
				order.setState("3");
			}*/
			map.put("result", "exist");
		}else { //대기상태일때
			order.setReceiveUserNo(user.getUserNo());
			order.setState("2");
			
			//수거 스탭 선택 후 주문 상태변경 하기
			int result = setakDao.updateOrderStaffCheck(order);
			
			//기사배정 푸쉬보내기
			if(result == 1) {
				User userInfo = setakDao.selectUserForApp(orderCheck.getUserNo());
				User staff = setakDao.selectStaff(user.getUserNo());
				//푸쉬알림 - 고객
				UtilSendPush sendPush = new UtilSendPush();
				String userTitle = "세탁풍경 알림 - 당겨주세요 ▼";
				String userContent = "수거하실 기사님("+staff.getUserId()+"-"+staff.getUserHp()+")이 배정되었습니다.";
				sendPush.androidSendPush(userInfo.getToken(), "applyList", userTitle, userContent);
				
				//푸쉬알림 - 관리자
				List<User> staffList = setakDao.selectStaffNAdmin();
				
				String title = "세탁물 수거신청 배정완료 알림";
				String content = staff.getUserId()+"님으로  수거배정이 완료되었습니다 \n";
				content += "고객이름 - "+userInfo.getUserId()+"\n";
				content += "휴대폰 - "+userInfo.getUserHp();
				
				for(int i=0; i<staffList.size(); i++){
					sendPush.androidSendPush(staffList.get(i).getToken(), "staff", title, content);
				}
				map.put("result", "success");
			}else {
				map.put("result", "fail");
			}
		}
		
		return map;
	}
	
	//세탁물 상태변경
	public Map<String, Object> staffCheck2Serv(User user, Order order){
		Map<String, Object> map= new HashMap<String, Object>();
		//먼저 주문상태 확인
		Order orderCheck = setakDao.selectOrderByOrderNo(order.getOrderNo());
		orderCheck.setState(String.valueOf(Integer.parseInt(orderCheck.getState())+1));
		
		int result = setakDao.updateOrderStaffCheck(orderCheck);
		
		if(result == 1) {
			User userInfo = setakDao.selectUserForApp(orderCheck.getUserNo());
			//푸쉬알림 - 고객
			UtilSendPush sendPush = new UtilSendPush();
			String userTitle = "세탁풍경 알림 - 당겨주세요 ▼";
			String userContent = "";
			if(Integer.parseInt(orderCheck.getState()) == 3) {
				userContent = "수거된 세탁물을 세탁을 시작합니다";
			}else if(Integer.parseInt(orderCheck.getState()) == 4) {
				userContent = "세탁이 완료되었습니다.";
			}
			
			sendPush.androidSendPush(userInfo.getToken(), "applyList", userTitle, userContent);
			
			map.put("result", "success");
		}else {
			map.put("result", "fail");
		}
		
		return map;
	}
	
	//유저체크
	public Map<String, Object> userCheckServ(int userNo){
		Map<String, Object> map= new HashMap<String, Object>();
		int count = setakDao.selectUserCntByCheck(userNo);
		
		if(count == 0) {
			map.put("result", "none");
		}else {
			map.put("result", "exist");
		}
		return map;
	}
	
	//모든세탁물 조회
	public List<Order> readAllOrderServ(){
		List<Order> list = setakDao.selectAllOrder();
		if(list != null) {
			for(int i=0; i<list.size(); i++) {
				list.get(i).setOrderList(setakDao.selectOrderedItemAll(list.get(i).getOrderNo())); //신청물품
				User user = new User();
				user = setakDao.selectUserForApp(list.get(i).getUserNo());
				if(user != null) {
					list.get(i).setUserId(user.getUserId());
					list.get(i).setUserHp(user.getUserHp());
				}
				//System.out.println(i+" 번째 리스트확인 : "+list.get(i));
			}
		}
		
		
		return list;
	}
}
