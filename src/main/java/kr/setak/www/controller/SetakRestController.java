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
	
	//웹, 관리자 로그인 
	@RequestMapping(value="/login", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> webLoginCtrl(@RequestParam(value="loginUserId")String loginUserId){
		System.out.println("확인 : "+loginUserId);
		Map<String, Object> resultMap = setakService.masterLoginServ(loginUserId);
		return resultMap;
	}
	
	@RequestMapping(value="/login.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> loginCtrl(@RequestParam(value="phone", defaultValue="none")String userId,
			@RequestParam(value="pw", defaultValue="none")String userHp,
			@RequestParam(value="userNo", defaultValue="0")int userNo){
		System.out.println("앱통신 테스트~!!");
		System.out.println("앱에서 넘어온 id값  확인 : "+userId);
		System.out.println("앱에서 넘어온 pw값  확인 : "+userHp);
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = setakService.readLoginServ(userId, userHp, userNo);
		
		map.put("result", resultMap.get("result"));
		if(resultMap.get("result").equals("success")){
			User user = (User) resultMap.get("user");
			map.put("loginUserNo", user.getUserNo());
			map.put("loginUserId", user.getUserId());
			map.put("loginUserHp", user.getUserHp());
			map.put("userGrade", user.getUserGrade());
			
		}
		/*map.put("result", "success");
		map.put("loginUserNo", 4);
		map.put("loginUserId", "윤재호");
		map.put("loginUserHp", "01038390401");
		map.put("userGrade", "staff");*/

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
			@RequestParam(value="applyOrderList")String orderList){
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
	
	//토큰저장하기
	@RequestMapping(value="/tokenAdd.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> tokenAddCtrl(@RequestParam(value="token")String token,
			User user,
			@RequestParam(value="type", defaultValue="login")String type){
		System.out.println("토큰  확인 : "+token);
		System.out.println("user 확인"+user);
		
		
		Map<String, Object> map = setakService.modifyTokenServ(user, token, type);
		
		return map;
	}
	
	//회원가입
	@RequestMapping(value="/join.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> joinCtrl(User user){
		System.out.println("회원가입 값  확인 : "+user);
		
		Map<String, Object> map = setakService.addUserServ(user);
		
		return map;
	}
	
	//웹, 푸쉬알림보내기 sendPush
	@RequestMapping(value="/sendPush", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> sendPushCtrl(@RequestParam(value="sendMsg")String sendMsg){
		System.out.println("푸쉬메세지 확인 : "+sendMsg);
		
		Map<String, Object> map = setakService.sendPushServ(sendMsg);
		
		return map;
	}
	
	//하나의 회원조회
	@RequestMapping(value="/getUser.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> getUserCtrl(@RequestParam(value="userNo")int userNo){
		System.out.println("userNo 확인 : "+userNo);
		Map<String, Object> map = setakService.readUserForAppServ(userNo);
		return map;
	}
	
	//회원탈퇴
	@RequestMapping(value="/userDelete.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> userDeleteCtrl(User user){
		System.out.println("user 확인 : "+user);
		Map<String, Object> map = setakService.removeUserServ(user);
		return map;
	}
	
	//세탁주문 목록 조회(완료제외)
	@RequestMapping(value="/mySetakReserve.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> mySetakReserveCtrl(User user){
		System.out.println("목록 user 확인 : "+user);
		Map<String, Object> map = setakService.readSetakListServ(user);
		return map;
	}
	
	//세탁주문 목록 조회(완료)
	@RequestMapping(value="/myCompleteReserve.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> myCompleteReserveCtrl(User user){
		System.out.println("목록 user 확인 : "+user);
		String type = "none";
		Map<String, Object> map = setakService.readCompleteListServ(user, type);
		return map;
	}
	
	//세탁주문 배정완료 이후 목록 조회
	@RequestMapping(value="/completeReserve.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> completeReserveCtrl(User user){
		System.out.println("스탭 user 확인 : "+user);
		String type="staff";
		Map<String, Object> map = setakService.readCompleteListServ(user, type);
		return map;
	}
	
	//유저정보수정
	@RequestMapping(value="/userModify.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> userModifyCtrl(User user){
		System.out.println("목록 user 확인 : "+user);
		Map<String, Object> map = setakService.modifyUserServ(user);
		return map;
	}
	
	//직원배정 대기중인 목록 조회
	@RequestMapping(value="/setakReserve.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> setakReserveCtrl(User user){
		System.out.println("스탭 확인 : "+user);
		Map<String, Object> map = setakService.readListStatOrderServ();
		return map;
	}
	
	//직원배정
	@RequestMapping(value="/setakReceive.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> setakReceiveCtrl(User user, Order order){
		System.out.println("기사배정 user 확인 : "+user);
		System.out.println("order 확인 : "+order);
		Map<String, Object> map = setakService.staffCheckServ(user, order);
		return map;
	}
	
	//직원배정
	@RequestMapping(value="/stateChange.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> stateChangeCtrl(User user, Order order){
		System.out.println("상태변경 user 확인 : "+user);
		System.out.println("상태변경order 확인 : "+order);
		Map<String, Object> map = setakService.staffCheck2Serv(user, order);
		return map;
	}
	
	//유저체크
	@RequestMapping(value="/checkUser.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> checkUserCtrl(@RequestParam(value="userNo")int userNo){
		Map<String, Object> map = setakService.userCheckServ(userNo);
		return map;
	}
	
	@RequestMapping(value="/firstTest.app", method= {RequestMethod.POST,RequestMethod.GET})
	public Map<String, Object> checkUser2Ctrl(@RequestParam(value="hi")String hi){
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(hi);
		map.put("text", hi);
		return map;
	}
}
