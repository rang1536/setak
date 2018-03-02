package kr.setak.www.domain;

public class TokenTb {
	private int no;
	private String token;
	private int userGrade;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUserGrade() {
		return userGrade;
	}
	public void setUserGrade(int userGrade) {
		this.userGrade = userGrade;
	}
	@Override
	public String toString() {
		return "TokenTb [no=" + no + ", token=" + token + ", userGrade=" + userGrade + "]";
	}
	
	
}
