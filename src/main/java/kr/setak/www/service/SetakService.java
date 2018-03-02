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
		int successCount = 0;
		int result = setakDao.insertApplyOrder(order);
		if(result == 1){ //주문등록성공
			//주문상품등록
			for(int i=0; i<orderedList.size(); i++){
				orderedList.get(i).setOrderNo(order.getOrderNo());
				int itemRes = setakDao.insertOrderedItem(orderedList.get(i));
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
		
		return map;
	}
}
