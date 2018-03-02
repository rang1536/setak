package kr.setak.www.domain;

public class OrderedItem {
	private int orderedNo;
	private int no; //상품번호
	private int orderCount; //갯수
	private int orderNo; //주문번호
	private String name;
	private String price;
	
	
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getOrderedNo() {
		return orderedNo;
	}
	public void setOrderedNo(int orderedNo) {
		this.orderedNo = orderedNo;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	@Override
	public String toString() {
		return "OrderedItem [orderedNo=" + orderedNo + ", no=" + no + ", orderCount=" + orderCount + ", orderNo="
				+ orderNo + ", name=" + name + ", price=" + price + "]";
	}
	
}
