package kr.setak.www.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.setak.www.domain.Order;
import kr.setak.www.domain.OrderItem;
import kr.setak.www.domain.OrderedItem;
import kr.setak.www.domain.User;
import kr.setak.www.service.SetakService;

@RestController
public class SetakRestController {
	
	@Autowired
	private SetakService setakService;
	
	@RequestMapping(value="/login.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> loginCtrl(@RequestParam(value="phone")String userId,
			@RequestParam(value="pw")String userHp){
		System.out.println("앱통신 테스트~!!");
		System.out.println("앱에서 넘어온 id값  확인 : "+userId);
		System.out.println("앱에서 넘어온 pw값  확인 : "+userHp);
		
		Map<String, Object> map = new HashMap<String, Object>();
		/*Map<String, Object> resultMap = setakService.readLoginServ(userId, userHp);*/
		
		/*map.put("result", resultMap.get("result"));
		if(resultMap.get("result").equals("success")){
			User user = (User) resultMap.get("user");
			map.put("loginUserNo", user.getUserNo());
			map.put("loginUserId", user.getUserId());
			map.put("loginUserHp", user.getUserHp());
			
		}*/
		map.put("result", "success");
		map.put("loginUserNo", 1);
		map.put("loginUserId", "윤재호");
		map.put("loginUserHp", "01038390401");
		return map;
	}
	
	@RequestMapping(value="/orderItem.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> orderItemCtrl(){
		System.out.println("앱통신 테스트~!!");
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrderItem> list = setakService.readAllItemServ();
		
		System.out.println("조회목록 확인 : "+list);
		/*OrderItem orderItem = new OrderItem();
		orderItem.setNo(1);
		orderItem.setName("티셔츠");
		orderItem.setPrice("10000");
		list.add(orderItem);
		
		orderItem = new OrderItem();
		orderItem.setNo(2);
		orderItem.setName("팬츠");
		orderItem.setPrice("5000");
		list.add(orderItem);*/
		
		map.put("list", list);
		return map;
	}
	
	//회원정보조회
	@RequestMapping(value="/userInfoLoad.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> userInfoLoadCtrl(@RequestParam(value="userId")String userId){
		System.out.println("앱통신 테스트~!!");
		System.out.println("앱에서 넘어온 id값  확인 : "+userId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		User user = setakService.readUserInfoServ(userId);
		map.put("user", user);
		return map;
	}
	
	//수거신청 applyOrder
	@RequestMapping(value="/applyOrder.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> applyOrderCtrl(Order order,
			@RequestParam(value="orderList")String orderList){
		System.out.println("앱통신 테스트~!!");
		System.out.println("앱에서 넘어온 값  확인 : "+order);
		System.out.println("앱에서 넘어온 값  확인 : "+orderList);
		
		Map<String, Object> map = setakService.addApplyOrderServ(order, orderList);
		/*User user = setakService.readUserInfoServ(userId);
		map.put("user", user);*/
		return map;
	}
	
	//웹, 세탁물품 등록 
	@RequestMapping(value="/addSetakItem", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> addSetakItemCtrl(OrderItem orderItem){
		System.out.println("입력 값  확인 : "+orderItem);
		
		Map<String, Object> map = setakService.addSetakItemServ(orderItem);	
		return map;
	}
	
	//웹, 세탁물품 수정위해 아이템정보 조회 readItemInfo
	@RequestMapping(value="/readItemInfo", method= {RequestMethod.POST,RequestMethod.GET})
	public OrderItem readItemInfoCtrl(int no){
		System.out.println("입력 값  확인 : "+no);
		
		OrderItem item = setakService.readItemServ(no);	
		return item;
	}
	
	//웹, 세탁물품 수정 
	@RequestMapping(value="/modifySetakItem", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> modifySetakItemCtrl(OrderItem orderItem){
		System.out.println("입력 값  확인 : "+orderItem);
		
		Map<String, Object> map = setakService.modifySetakItemServ(orderItem);	
		return map;
	}
	
	//웹, 세탁물품 삭제
	@RequestMapping(value="/removeSetakItem", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> removeSetakItemCtrl(@RequestParam(value="no")int no){
		System.out.println("입력 값  확인 : "+no);
		
		Map<String, Object> map = setakService.removeSetakItemServ(no);	
		return map;
	}
	//웹, 스탭등록 
	@RequestMapping(value="/addStaff", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> addSetakItemCtrl(User user){
		System.out.println("입력 값  확인 : "+user);
		
		Map<String, Object> map = setakService.addStaffServ(user);	
		return map;
	}
	
	//웹, 스탭수정 
	@RequestMapping(value="/modifyStaff", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> modifyStaffCtrl(User user){
		System.out.println("입력 값  확인 : "+user);
		
		Map<String, Object> map = setakService.modifyStaffServ(user);	
		return map;
	}
	
	
	//웹, 스탭 수정위해 스탭정보 조회
	@RequestMapping(value="/readStaffInfo", method= {RequestMethod.POST,RequestMethod.GET})
	public User readStaffInfoCtrl(@RequestParam(value="userNo")int userNo){
		System.out.println("입력 값  확인 : "+userNo);
		
		User user = setakService.readStaffServ(userNo);
		return user;
	}
	
	//웹, 스탭삭제
	@RequestMapping(value="/removeStaff", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> removeStaffItemCtrl(@RequestParam(value="userNo")int userNo){
		System.out.println("입력 값  확인 : "+userNo);
		
		Map<String, Object> map = setakService.removeStaffServ(userNo);	
		return map;
	}
}
