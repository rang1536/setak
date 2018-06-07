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
	
	//마스터 로그인
	public List<User> selectMasterLogin(User user){
		return sql.selectList("SetakDao.selectMasterLogin", user);
	}
	
	//상품조회
	public List<OrderItem> selectAllItems(){
		return sql.selectList("SetakDao.selectAllItems");
	}
	
	//유저정보조회
	public User selectUserInfo(String userId){
		return sql.selectOne("SetakDao.selectUserInfo", userId);
	}
	
	//
	public User selectLogin2(int userNo) {
		return sql.selectOne("SetakDao.selectLogin2", userNo);
	}
	
	//핸드폰번호 조회
	public String selectHpForApp(int userNo) {
		return sql.selectOne("SetakDao.selectHpForApp", userNo);
	}
	//로그인
	public User selectLogin(User user){
		System.out.println("유저값 확인DAO : "+user);
		return sql.selectOne("SetakDao.selectLogin", user);
	}
	
	
	// 유저체크
	public int selectUserCntByCheck(int userNo) {
		return sql.selectOne("SetakDao.selectUserCntByCheck", userNo);
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
	
	//토큰저장
	public int updateUserTokenBySns(User user){
		return sql.update("SetakDao.updateUserTokenBySns", user);
	}
		
	//관리자,스탭조회
	public List<User> selectStaffNAdmin(){
		return sql.selectList("SetakDao.selectStaffNAdmin");
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
	
	//토큰으로 유저조회
	public int selectUserByToken(User user) {
		return sql.selectOne("SetakDao.selectUserByToken",user);
	}
	
	//토큰인서트
	public int insertUserToken(User user) {
		return sql.insert("SetakDao.insertUserToken", user);
	}
	
	//하나의회원조회
	public User selectUserForApp(int userNo) {
		return sql.selectOne("SetakDao.selectUserForApp",userNo);
	}
	
	//회원탈퇴
	public int deleteUser(User user) {
		return sql.delete("SetakDao.deleteUser", user);
	}
	
	//세탁주문 조회(완료제외)
	public List<Order> selectOrderList(int userNo){
		return sql.selectList("SetakDao.selectOrderList", userNo);
	}
	
	//세탁주문 조회(완료)
	public List<Order> selectCompleteList(int userNo){
		return sql.selectList("SetakDao.selectCompleteList", userNo);
	}
	
	//세탁물조회
	public List<OrderedItem> selectOrderedItemAll(int orderNo){
		return sql.selectList("SetakDao.selectOrderedItemAll", orderNo);
	}
	
	//유저정보조회(핸드폰번호 중복확인)
	public List<User> selectUserByHp(String userHp){
		return sql.selectList("SetakDao.selectUserByHp", userHp);
	}
	
	//유저정보수정
	public int updateUserInfoByApp(User user) {
		return sql.update("SetakDao.updateUserInfoByApp", user);
	}
	
	//주문번호로 주문확인
	public Order selectOrderByOrderNo(int orderNo) {
		return sql.selectOne("SetakDao.selectOrderByOrderNo", orderNo);
	}
	
	// 배송대기 주문확인
	public List<Order> selectOrderByStateStay(){
		return sql.selectList("SetakDao.selectOrderByStateStay");
	}
	
	// 배송직원 할당 후 체크
	public int updateOrderStaffCheck(Order order) {
		return sql.update("SetakDao.updateOrderStaffCheck", order);
	}
	
	// sns로 회원조회 
	public User selectUserBySnsId(User user) {
		return sql.selectOne("SetakDao.selectUserBySnsId", user);
	}
	
	// 직원> 나의 배정목록
	public List<Order> selectListByStaffNo(int userNo){
		return sql.selectList("SetakDao.selectListByStaffNo", userNo);
	}
	
	//모든세탁물 조회
	public List<Order> selectAllOrder(){
		return sql.selectList("SetakDao.selectAllOrder");
	}
}

