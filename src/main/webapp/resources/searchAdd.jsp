<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>주소찾기</title>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    new daum.Postcode({
        oncomplete: function(data) {
            if(data.userSelectedType=="R"){
                // userSelectedType : 검색 결과에서 사용자가 선택한 주소의 타입
                // return type : R - roadAddress, J : jibunAddress
                // TestApp 은 안드로이드에서 등록한 이름
                window.TestApp.setAddress(data.zonecode, data.roadAddress, data.buildingName);
            }
            else{
                window.TestApp.setAddress(data.zonecode, data.jibunAddress, data.buildingName);
            }       
        }
    }).open();
</script>
</head>
<body>

</body>
</html>