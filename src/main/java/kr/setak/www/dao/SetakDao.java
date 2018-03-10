package kr.setak.www.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.setak.www.domain.Order;
import kr.setak.www.domain.OrderItem;
import kr.setak.www.domain.OrderedItem;
import kr.setak.www.domain.User;

@Repository
public class SetakDao {

	@Autowired
	private SqlSessionTemplate sql;
	
	//상품조회
	public List<OrderItem> selectAllItems(){
		return sql.selectList("SetakDao.selectAllItems");
	}
	
	//유저정보조회
	public User selectUserInfo(String userId){
		return sql.selectOne("SetakDao.selectUserInfo", userId);
	}
	
	//로그인
	public User selectLogin(User user){
		return sql.selectOne("SetakDao.selectLogin",user);
	}
	
	//수거신청등록
	public int insertApplyOrder(Order order){
		return sql.insert("SetakDao.insertApplyOrder", order);
	}
	
	//수거신청물품 등록
	public int insertOrderedItem(OrderedItem orderedItem){
		return sql.insert("SetakDao.insertOrderedItem", orderedItem);
	}
	
	//웹,물품등록
	public int insertSetakItem(OrderItem orderItem){
		return sql.insert("SetakDao.insertSetakItem", orderItem);
	}
	
	//웹,물품수정
	public int updateSetakItem(OrderItem orderItem){
		return sql.insert("SetakDao.updateSetakItem", orderItem);
	}
	
	//웹,물품삭제
	public int deleteSetakItem(int no){
		return sql.insert("SetakDao.deleteSetakItem", no);
	}
	
	//웹, 하나의 물품조회
	public OrderItem selectItem(int no) {
		return sql.selectOne("SetakDao.selectItem", no);
	}
	
	//웹, 스탭조회
	public List<User> selectStaffAll(){
		return sql.selectList("SetakDao.selectStaffAll");
	}
	
	//웹,물품등록
	public int insertStaff(User user){
		return sql.insert("SetakDao.insertStaff", user);
	}
	
	//웹,물품수정
	public int updateStaff(User user){
		return sql.insert("SetakDao.updateStaff", user);
	}
	
	//웹,물품삭제
	public int deleteStaff(int userNo){
		return sql.insert("SetakDao.deleteStaff", userNo);
	}
	
	//웹, 하나의스탭 조회
	public User selectStaff(int userNo){
		return sql.selectOne("SetakDao.selectStaff", userNo);
	}
	
	//토큰저장
	public int updateUserToken(User user){
		return sql.update("SetakDao.updateUserToken", user);
	}
	
	//관리자,스탭조회
	public List<User> selectStaffNAdmin(){
		return sql.selectList("SerakDao.selectStaffNAdmin");
	}
	
	//회원가입
	public int insertUser(User user){
		return sql.insert("SetakDao.insertUser", user);
	}
	
	//중복회원조회
	public List<User> selectSameUserCheck(User user){
		return sql.selectList("SetakDao.selectLogin", user);
	}
	
	//수거신청시 수거대기로 변경
	public int updateSetakState(User user){
		return sql.update("SetakDao.updateSetakState", user);
	}
	
	//모든 유저정보조회
	public List<User> selectUserAll(){
		return sql.selectList("SetakDao.selectUserAll");
	}
}

