package kr.setak.www.domain;

public class Order {
	private int orderNo; //pk
	private String inDate; //수거일 
	private String finishDate; //완료일
	private String state; //진행상태
	private String totalPrice; //총가격
	private String userHp; //고객 핸드폰번호
	private String orderAdd; //주문지
	private String deliveryAdd; //배송지
	private String hopeCollectionDate; //원하는 수거일
	private String hopeDeliveryDate; //원하는 배달일
	private String sameAddCheck;
	private String sangseAdd;
	private String userId;
	private int userNo;
	private String comment;
	
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSangseAdd() {
		return sangseAdd;
	}
	public void setSangseAdd(String sangseAdd) {
		this.sangseAdd = sangseAdd;
	}
	public String getSameAddCheck() {
		return sameAddCheck;
	}
	public void setSameAddCheck(String sameAddCheck) {
		this.sameAddCheck = sameAddCheck;
	}
	public String getHopeCollectionDate() {
		return hopeCollectionDate;
	}
	public void setHopeCollectionDate(String hopeCollectionDate) {
		this.hopeCollectionDate = hopeCollectionDate;
	}
	public String getHopeDeliveryDate() {
		return hopeDeliveryDate;
	}
	public void setHopeDeliveryDate(String hopeDeliveryDate) {
		this.hopeDeliveryDate = hopeDeliveryDate;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDeliveryAdd() {
		return deliveryAdd;
	}
	public void setDeliveryAdd(String deliveryAdd) {
		this.deliveryAdd = deliveryAdd;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getUserHp() {
		return userHp;
	}
	public void setUserHp(String userHp) {
		this.userHp = userHp;
	}
	public String getOrderAdd() {
		return orderAdd;
	}
	public void setOrderAdd(String orderAdd) {
		this.orderAdd = orderAdd;
	}
	@Override
	public String toString() {
		return "Order [orderNo=" + orderNo + ", inDate=" + inDate + ", finishDate=" + finishDate + ", state=" + state
				+ ", totalPrice=" + totalPrice + ", userHp=" + userHp + ", orderAdd=" + orderAdd + ", deliveryAdd="
				+ deliveryAdd + ", hopeCollectionDate=" + hopeCollectionDate + ", hopeDeliveryDate=" + hopeDeliveryDate
				+ ", sameAddCheck=" + sameAddCheck + ", sangseAdd=" + sangseAdd + ", userId=" + userId + ", userNo="
				+ userNo + ", comment=" + comment + "]";
	}
	
}
