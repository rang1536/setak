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
}
