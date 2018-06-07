package kr.setak.www.domain;

public class User {
	private int userNo;
	private String userId;
	private String gender;
	private String userEmail;
	private String userHp;
	private String userAdd;
	private String userBirth;
	private int orderCount;
	private String regDate;
	private String userGrade;
	private String token;
	private String sangseAdd;
	private long kakaoId;
	private long facebookId;
	
	
	public long getKakaoId() {
		return kakaoId;
	}
	public void setKakaoId(long kakaoId) {
		this.kakaoId = kakaoId;
	}
	public long getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}
	public String getSangseAdd() {
		return sangseAdd;
	}
	public void setSangseAdd(String sangseAdd) {
		this.sangseAdd = sangseAdd;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserHp() {
		return userHp;
	}
	public void setUserHp(String userHp) {
		this.userHp = userHp;
	}
	public String getUserAdd() {
		return userAdd;
	}
	public void setUserAdd(String userAdd) {
		this.userAdd = userAdd;
	}
	public String getUserBirth() {
		return userBirth;
	}
	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getUserGrade() {
		return userGrade;
	}
	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}
	@Override
	public String toString() {
		return "User [userNo=" + userNo + ", userId=" + userId + ", gender=" + gender + ", userEmail=" + userEmail
				+ ", userHp=" + userHp + ", userAdd=" + userAdd + ", userBirth=" + userBirth + ", orderCount="
				+ orderCount + ", regDate=" + regDate + ", userGrade=" + userGrade + ", token=" + token + ", sangseAdd="
				+ sangseAdd + ", kakaoId=" + kakaoId + ", facebookId=" + facebookId + "]";
	}
	
	
}
