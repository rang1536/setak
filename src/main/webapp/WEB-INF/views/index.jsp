<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="format-detection" content="telephone=no">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta property="og:title" content="세탁풍경 관리자 ">
	<meta property="og:description" content="세탁풍경 관리자 모바일 웹페이지">
	<meta property="og:image" content="http://sh86.kr/m/resources/img/jbimage.jpg">
	<title>세탁풍경 관리자</title>
	
	<link rel="stylesheet" href="resources/js/jquery.mobile-1.4.5.css">
	<script src="resources/js/jquery.js"></script>
	<script src="resources/js/jquery.mobile-1.4.5.js"></script>
	
	<link href="resources/js/jquery.modal.css" type="text/css" rel="stylesheet" />
	<script src="resources/js/jquery.modal.min.js"></script>
	
	<!-- 우편번호(다음) -->
	<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
	<link href="resources/img/jbShotCut.PNG" rel="shortcut icon" />
	<link href="resources/img/jbShotCut.PNG" rel="apple-touch-icon"></link>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
	
	<style>
		.btn-label {position: relative;left: -12px;display: inline-block;padding: 6px 12px;background: rgba(0,0,0,0.15);border-radius: 3px 0 0 3px;}
		.btn-labeled {padding-top: 0;padding-bottom: 0;}
		.btn { margin-bottom:10px; }
		.subDiv{width:100%;border:1px solid #ddd;background-color:#EAEAEA;color:#00000;}
		.contents{width:100%;padding-top:5px;font-weight:bold;border:2px solid #0054FF;"}
		.subTH{text-align:center;}
		.loader{width: 100%;
			height: 100%;
			top: 0;
			left: 0;
			position: fixed;
			display: block;
			opacity: 0.8;
			background: white;
			z-index: 99;
			text-align: center;
		}
		.loader img{
			position: absolute;
			top: 50%;
			left: 50%;
			z-index: 100;
		}
	</style>
	
	<script>
	
	var localUserId = '';
    if(localStorage.getItem("userId") == null){
    	/* makeCookie(localUserId); */
		window.location.href = "#page5";
	}else{
		localUserId = localStorage.getItem("userId");
		/* makeCookie(localUserId); */
	}
    
    function login(){
    	var loginUserId = $('#loginUserId').val();
    	/* console.log(loginUserId); */
    	if(loginUserId == null || loginUserId ==''){
    		alert('관리자 휴대폰 번호를 입력하세요');
    	}else{
    		$.ajax({
    			url : 'login',
    			data : {'loginUserId':loginUserId},
    			type : 'post',
    			dataType : 'json',
    			success : function(data){
    				/* console.log(data); */
    				if(data.result == 'login'){
    					localStorage.setItem('userId',data.result);
    					window.location.href = "#page1";
    				}else{
    					alert('관리자 정보를 다시 확인해주세요');
    				}
    			}
    		})
    	} 	 	
    }
    
	//세탁물입력폼 열기
	function addSetakItemForm(){
		$('#listSetakItemDiv').css('display','none');
		$('#addSetakItemDiv').slideDown();
	}
	
	//세탁물입력폼 닫기
	function closeThis(){
		$('#addSetakItemDiv').css('display','none');
		$('#listSetakItemDiv').slideDown();
	}
	
	//세탁물추가
	function addSetakItem(){
		var name = $('#addSetakItemDiv').find('#name').val();
		var price = $('#addSetakItemDiv').find('#price').val();
		
		if(name == null || name == ''){
			alert('물품명을 입력하세요!');
			return;
		}else if(price == null || price == ''){
			alert('가격을 입력하세요!');
			return;
		}
		
		$.ajax({
			url : 'addSetakItem',
			type : 'post',
			dataType : 'json',
			data : {'name':name, 'price':price},
			success: function(data){
				if(data.result == 'success'){
					alert('등록되었습니다~!!');
					window.location.reload(true);
				}else{
					alert('등록실패~!! 010-9747-0740(코리아웹센터)로 전화주세요');
					window.location.reload(true);
				}
			}
		})
	}
	
	//세탁물수정
	function modifyItem(no){
		console.log(no);
		$.ajax({
			url : 'readItemInfo',
			data : {'no':no},
			dataType : 'json',
			type : 'post',
			success : function(data){
				var html = '';
				html += '<table id="modifyItemTb" style="width:90%;font-size:17px;">';
				html += '<tr>';
				html += '<th style="width:35%;">물품명</th>';
				html += '<td><input type="hidden" name="no" id="no" value="'+data.no+'"/><input type="text" name="name" id="name" value="'+data.name+'"/></td>';
				html += '</tr><tr>';
				html += '<th><br/>가격</th>';
				html += '<td><br/><input type="number" name="price" id="price" value="'+data.price+'" placeholder="숫자만입력"/></td>';
				html += '</tr><tr>';
				/* html += '<td colspan="2">';
				html += '<button type="button" class="btn btn-labeled btn-success" style="font-weight:bold;" onclick="addSetakItem()">물품등록</button>';
				html += '</td>'; */
				html += '</tr></table>';
				
				modifyItemPopUp(html);
			}
		})
	}
	
	function modifyItemPopUp(txt){
	    modal({
	        type: 'info',
	        title: '물품수정',
	        text: txt,
	        buttons: [{
	    		text: '수정', //Button Text
	    		val: 'comment', //Button Value
	    		eKey: true, //Enter Keypress
	    		addClass: 'btn-light-green', //Button Classes (btn-large | btn-small | btn-green | btn-light-green | btn-purple | btn-orange | btn-pink | btn-turquoise | btn-blue | btn-light-blue | btn-light-red | btn-red | btn-yellow | btn-white | btn-black | btn-rounded | btn-circle | btn-square | btn-disabled)
	    		onClick: function(dialog) {
	    			var no = $('#modifyItemTb').find('#no').val();
	    			var name = $('#modifyItemTb').find('#name').val();
	    			var price = $('#modifyItemTb').find('#price').val();
	    			
	    			if(confirm('정말 수정하시겠습니까?')){
	    				$.ajax({
		    				url : 'modifySetakItem',
		    				data : {'no':no, 'name':name, 'price':price},
		    				dataType : 'json',
		    				type : 'post',
		    				success:function(data){
		    					if(data.result == 'success'){
		    						alert('수정되었습니다~!!');
		    						window.location.reload(true);
		    					}else{
		    						alert('수정실패~!! 010-9747-0740(코리아웹센터)로 전화주세요');
		    						window.location.reload(true);
		    					}
		    				}
		    			})
	    			}	    			
	    			return true;
	    		}
	    	},
	    	{
	    		text: '닫기', //Button Text
	    		val: 'close', //Button Value
	    		eKey: true, //Enter Keypress
	    		addClass: 'btn-light-blue', //Button Classes (btn-large | btn-small | btn-green | btn-light-green | btn-purple | btn-orange | btn-pink | btn-turquoise | btn-blue | btn-light-blue | btn-light-red | btn-red | btn-yellow | btn-white | btn-black | btn-rounded | btn-circle | btn-square | btn-disabled)
	    		onClick: function(dialog) {
	    			return true;
	    		}
	    	}]
	    });
	}
	
	//물품삭제
	function removeItem(no){
		if(confirm('정말로 삭제하시겠습니까?')){
			$.ajax({
				url : 'removeSetakItem',
				data : {'no':no},
				dataType : 'json',
				type : 'post',
				success : function(data){
					if(data.result == 'success'){
						alert('삭제되었습니다~!!');
						window.location.reload(true);
					}else{
						alert('삭제실패~!! 010-9747-0740(코리아웹센터)로 전화주세요');
						window.location.reload(true);
					}
				}
			})
		}
	}
	
	//스탭 입력폼 열기
	function addStaffForm(){
		$('#listStaffDiv').css('display','none');
		$('#addStaffDiv').slideDown();
	}
	
	//스탭입력폼 닫기
	function closeThisForm(){
		$('#addStaffDiv').css('display','none');
		$('#listStaffDiv').slideDown();
	}
	
	//스탭등록
	function addStaff(){
		var userId = $(addStaffDiv).find('#userId').val();
		var userHp = $(addStaffDiv).find('#userHp').val();
		
		if(userId == null || userId == ''){
			alert('체인점 이름을 입력하세요!');
			return;
		}else if(userHp == null || userHp == ''){
			alert('체인점 휴대폰번호를 입력하세요!');
			return;
		}
		
		$.ajax({
			url : 'addStaff',
			type : 'post',
			dataType : 'json',
			data : {'userId':userId, 'userHp':userHp},
			success: function(data){
				if(data.result == 'success'){
					alert('등록되었습니다~!!');
					window.location.reload(true);
				}else{
					alert('등록실패~!! 010-9747-0740(코리아웹센터)로 전화주세요');
					window.location.reload(true);
				}
			}
		})
	}
	
	//스탭 정보수정
	function modifyStaff(userNo){
		$.ajax({
			url : 'readStaffInfo',
			data : {'userNo':userNo},
			dataType : 'json',
			type : 'post',
			success : function(data){
				var html = '';
				html += '<table id="modifyStaffTb" style="width:90%;font-size:17px;">';
				html += '<tr>';
				html += '<th style="width:35%;">체인점 이름</th>';
				html += '<td><input type="text" name="userId" id="userId" value="'+data.userId+'"/>';
				html += '<input type="hidden" name="userNo" id="userNo" value="'+data.userNo+'"/></td>';
				html += '</tr><tr>';
				html += '<th><br/>휴대폰</th>';
				html += '<td><br/><input type="number" name="userHp" id="userHp" value="'+data.userHp+'"/></td>';
				html += '</tr><tr>';
				/* html += '<td colspan="2">';
				html += '<button type="button" class="btn btn-labeled btn-success" style="font-weight:bold;" onclick="modifyStaff()">수정</button>';
				html += '</td>'; */
				html += '</tr></table>';
				
				modifyStaffPopUp(html);
			}
		})
	}
	
	function modifyStaffPopUp(txt){
	    modal({
	        type: 'info',
	        title: '체인점 정보수정',
	        text: txt,
	        buttons: [{
	    		text: '수정', //Button Text
	    		val: 'modify', //Button Value
	    		eKey: true, //Enter Keypress
	    		addClass: 'btn-light-green', //Button Classes (btn-large | btn-small | btn-green | btn-light-green | btn-purple | btn-orange | btn-pink | btn-turquoise | btn-blue | btn-light-blue | btn-light-red | btn-red | btn-yellow | btn-white | btn-black | btn-rounded | btn-circle | btn-square | btn-disabled)
	    		onClick: function(dialog) {
	    			var userId = $('#modifyStaffTb').find('#userId').val();
	    			var userHp = $('#modifyStaffTb').find('#userHp').val();
	    			var userNo = $('#modifyStaffTb').find('#userNo').val();
	    			console.log("체크 : "+userId+" "+userHp+" "+userNo);
	    			$.ajax({
	    				url : 'modifyStaff',
	    				data : {'userId':userId, 'userHp':userHp, 'userNo': userNo},
	    				dataType : 'json',
	    				type : 'post',
	    				success:function(data){
	    					if(data.result == 'success'){
	    						alert('수정되었습니다~!!');
	    						window.location.reload(true);
	    					}else{
	    						alert('수정실패~!! 010-9747-0740(코리아웹센터)로 전화주세요');
	    						window.location.reload(true);
	    					}
	    				}
	    			})
	    			return true;
	    		}
	    	},
	    	{
	    		text: '닫기', //Button Text
	    		val: 'close', //Button Value
	    		eKey: true, //Enter Keypress
	    		addClass: 'btn-light-blue', //Button Classes (btn-large | btn-small | btn-green | btn-light-green | btn-purple | btn-orange | btn-pink | btn-turquoise | btn-blue | btn-light-blue | btn-light-red | btn-red | btn-yellow | btn-white | btn-black | btn-rounded | btn-circle | btn-square | btn-disabled)
	    		onClick: function(dialog) {
	    			return true;
	    		}
	    	}]
	    });
	}
	
	//스탭 삭제
	function removeStaff(userNo){
		if(confirm('정말로 삭제하시겠습니까?')){
			$.ajax({
				url : 'removeStaff',
				data : {'userNo':userNo},
				dataType : 'json',
				type : 'post',
				success : function(data){
					if(data.result == 'success'){
						alert('삭제되었습니다~!!');
						window.location.reload(true);
					}else{
						alert('삭제실패~!! 010-9747-0740(코리아웹센터)로 전화주세요');
						window.location.reload(true);
					}
				}
			})
		}
	}
	
	//숫자컴마찍기
 	function numberChange(){
		$('.numberInput').html(function(){
			var x = $(this).html();
		    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
		});
	}
	
	$(document).ready(function(){
		numberChange();
	})
	
	//푸쉬보내기
	function sendPush(){
		var msg = $('#sendMsg').val();
		
		if(confirm('푸쉬알림을 보내시겠습니까?')){
			$('.loader').css('display','');
			$.ajax({
				url : 'sendPush',
				data : {'sendMsg':msg},
				dataType : 'json',
				type : 'post',
				success : function(data){
					$('.loader').css('display','none');
					if(data.result = 'success'){
						alert('알림을 발송하였습니다.');
						$('#sendMsg').val('');
					}
				}
			})
		}
	}
	
	//주문현황보기
	function orderListShow(){
		$('#orderCompleteDiv').css('display','none');
		$('#listOrderDiv').slideDown();
	}
	
	function orderCompleteShow(){
		$('#listOrderDiv').css('display','none');
		$('#orderCompleteDiv').slideDown();
	}
	
	//상세내용 열고닫기
	function openNClose(index){
		var tag = 'tr'+index;
		var val = $('#'+tag).find('#openChk').val();
		if(val == '1'){
			$('#'+tag).slideDown();
			$('#'+tag).find('#openChk').val('2');
		}else{
			$('#'+tag).slideUp();
			$('#'+tag).find('#openChk').val('1');
		}
	}
	
	function openNClose2(index){
		var tag = '2tr'+index;
		var val = $('#'+tag).find('#openChk').val();
		if(val == '1'){
			$('#'+tag).slideDown();
			$('#'+tag).find('#openChk').val('2');
		}else{
			$('#'+tag).slideUp();
			$('#'+tag).find('#openChk').val('1');
		}
	}
	</script>
</head>
<body>
<div class="loader" style="display:none;">
	<img src="resources/img/2.gif" alt="loading">
</div>
<section id="page1" data-role="page">
    <header data-role="header" data-tap-toggle="false" data-position="fixed" style="background-color:#F6F6F6;">
    	<img src="resources/img/ac_logo.png" />
    	<!-- <a class="ui-btn-right" href="#page9" data-icon="gear" style="background-color:rgba(255,255,255,0.5);margin-top:15px;"><font style="font-weight:bold;color:red;">MY</font></a> -->
    	<div data-role="navbar">
            <ul>
                <li><a href="#" class="ui-btn-active ui-state-persist"><font style="font-size:16px;">상품</font></a></li>
                <li><a href="#page2" data-transition="none"><font style="font-size:16px;">체인점</font></a></li>
                <li><a href="#page3" data-transition="none"><font style="font-size:16px;">세탁물</font></a></li>
                <li><a href="#page4" data-transition="none"><font style="font-size:16px;">프로모션</font></a></li>
                
            </ul>
        </div><!-- /navbar -->
    </header>
    
    <div class="content" data-role="content">
    	
    	<table style="width:100%;">
    		<tr>
    			<td style="width:50%;">
	    			<a class="btn btn-info btn-labeled" href="#" role="button" style="width:99%;" onclick="addSetakItemForm();">
		                <span class="btn-label">
		                <i class="glyphicon glyphicon-info-sign"></i></span>세탁상품 추가
		            </a>
	            </td>
    			<td style="width:50%;">
    				<a class="btn btn-warning btn-labeled" href="#" role="button" style="width:99%;" onclick="closeThis();">
                		<span class="btn-label">
                		<i class="glyphicon glyphicon-bookmark"></i></span>세탁상품 목록
            		</a>
            	</td>
    		</tr>
    	</table>
    	<div style="width:100%;display:none;font-size:17px;" id="addSetakItemDiv">
    		<table style="width:100%;">
    			<tr>
    				<td colspan="2" onclick="closeThis()" style="text-align:right;width:100%;">
    					<button type="button" class="btn btn-block btn-default" style="font-weight:bold;" onclick="closeThis()">창닫기</button>
    				</td>
    			</tr>
    			<tr>
    				<td><input type="text" name="name" id="name" placeholder="물품명"/></td>
    			</tr>
    			<tr>
    				<td><input type="number" name="price" id="price" placeholder="가격"/></td>
    			</tr>
    			<tr>
    				<td>
    					<button type="button" class="btn btn-block btn-default" style="font-weight:bold;" onclick="addSetakItem()">물품등록</button>
            		</td>
    			</tr>
    		</table>
    	</div>
    	<div style="width:100%;font-size:17px;" id="listSetakItemDiv">
    		<p style="width:100%;background-color:#CC3D3D;color:white;text-align:center;border-radius:4px;">세탁물품목록</p>
    		<table style="width:100%;">
    			<tr>
    				<th style="border-bottom:2px solid #ddd;">물품명</th>
    				<th style="border-bottom:2px solid #ddd;">가격</th>
    				<th style="border-bottom:2px solid #ddd;"></th>
    				<th style="border-bottom:2px solid #ddd;"></th>
    			</tr>
    			<c:forEach var="list" items="${itemList }">
    				<tr>
    					<td style="border-bottom:1px dotted #ddd;margin:2px;">${list.name }</td>
    					<td style="border-bottom:1px dotted #ddd;margin:2px;"><span class="numberInput">${list.price }</span></td>
    					<td style="width:45px;border-bottom:1px dotted #ddd;margin:2px;" onclick="modifyItem('${list.no}');"><img src="resources/img/edit.jpg" style="width:40px;height:40px;"/></td>
    					<td style="width:45px;text-align:center;border-bottom:1px dotted #ddd;margin:2px;" onclick="removeItem('${list.no}');"><img src="resources/img/cencel.jpg" style="width:40px;height:40px;"/></td>
    				</tr>
    			</c:forEach>
    		</table>
    	</div>
    	
   		<br/>
   		
    </div>	
    
    <!-- 푸터  -->
    <div style="overflow: hidden;" data-role="footer" data-tap-toggle="false" data-position="fixed" onclick="footerBanner();">
	   <img src="resources/img/foot.png" style="width:100%;height:65px;margin-left:0px;"/>
	</div>
</section> 


<section id="page2" data-role="page">
    <header data-role="header" data-tap-toggle="false" data-position="fixed" style="background-color:#F6F6F6;">
    	<img src="resources/img/ac_logo.png"/>
    	<!-- <a class="ui-btn-right" href="#page9" data-icon="gear" style="background-color:rgba(255,255,255,0.5);margin-top:15px;"><font style="font-weight:bold;color:red;">MY</font></a> -->
    	<div data-role="navbar">
            <ul>
                <li><a href="#page1" data-transition="none"><font style="font-size:16px;">상품</font></a></li>
                <li><a href="#" class="ui-btn-active ui-state-persist"><font style="font-size:16px;">체인점</font></a></li>
                <li><a href="#page3" data-transition="none"><font style="font-size:16px;">세탁물</font></a></li>
                <li><a href="#page4" data-transition="none"><font style="font-size:16px;">프로모션</font></a></li>
                
            </ul>
        </div><!-- /navbar -->
    </header>
    
    <div class="content" data-role="content">
    	<table style="width:100%;">
    		<tr>
    			<td style="width:50%;">
	    			<a class="btn btn-info btn-labeled" href="#" role="button" style="width:99%;" onclick="addStaffForm();">
		                <span class="btn-label">
		                <i class="glyphicon glyphicon-info-sign"></i></span>체인점등록
		            </a>
	            </td>
    			<td style="width:50%;">
    				<a class="btn btn-warning btn-labeled" href="#" role="button" style="width:99%;" onclick="closeThisForm();">
                		<span class="btn-label">
                		<i class="glyphicon glyphicon-bookmark"></i></span>체인점목록
            		</a>
            	</td>
    		</tr>
    	</table>
    	<div style="width:100%;display:none;font-size:17px;" id="addStaffDiv">
    		<table style="width:100%;">
    			<tr>
    				<td colspan="2" onclick="closeThisForm()" style="text-align:right;width:100%;">
    					<button type="button" class="btn btn-block btn-default" style="font-weight:bold;" onclick="closeThisForm()">창닫기</button>
    				</td>
    			</tr>
    			<tr>
    				<th>이름</th>
    				<td><input type="text" name="userId" id="userId"/></td>
    			</tr>
    			<tr>
    				<th>휴대폰</th>
    				<td><input type="number" name="userHp" id="userHp"/></td>
    			</tr>
    			<tr>
    				<td colspan="2">
    					<button type="button" class="btn btn-block btn-default" style="font-weight:bold;" onclick="addStaff()">체인점등록</button>
            		</td>
    			</tr>
    		</table>
    	</div>
    	<div style="width:100%;font-size:17px;" id="listStaffDiv">
    		<p style="width:100%;background-color:#CC3D3D;color:white;text-align:center;border-radius:5px;">체인점목록</p>
    		<table style="width:100%;">
    			<tr>
    				<th>이름</th>
    				<th>휴대폰</th>
    				<th>등급</th>
    				<th></th>
    				<th></th>
    			</tr>
    			<c:forEach var="list" items="${staffList }">
    				<tr>
    					<td>${list.userId }</td>
    					<td>${list.userHp }</td>
    					<td>${list.userGrade }</td>
    					<td style="width:50px;" onclick="modifyStaff('${list.userNo}');"><img src="resources/img/edit.jpg" style="width:40px;"/></td>
    					<td style="width:50px;" onclick="removeStaff('${list.userNo}');"><img src="resources/img/cencel.jpg" style="width:40px;"/></td>
    				</tr>
    			</c:forEach>
    		</table>
    	</div>
    </div>
    
    <!-- 푸터  -->
    <div style="overflow: hidden;" data-role="footer" data-tap-toggle="false" data-position="fixed" onclick="footerBanner();">
	   <img src="resources/img/foot.png" style="width:100%;height:65px;margin-left:0px;"/>
	</div>
</section> 

<section id="page3" data-role="page">
    <header data-role="header" data-tap-toggle="false" data-position="fixed" style="background-color:#F6F6F6;">
    	<img src="resources/img/ac_logo.png"/>
    	<!-- <a class="ui-btn-right" href="#page9" data-icon="gear" style="background-color:rgba(255,255,255,0.5);margin-top:15px;"><font style="font-weight:bold;color:red;">MY</font></a> -->
    	<div data-role="navbar">
            <ul>
                <li><a href="#page1" data-transition="none"><font style="font-size:16px;">상품</font></a></li>
                <li><a href="#page2" data-transition="none"><font style="font-size:16px;">체인점</font></a></li>
                <li><a href="#" class="ui-btn-active ui-state-persist"><font style="font-size:16px;">세탁물</font></a></li>
                <li><a href="#page4" data-transition="none"><font style="font-size:16px;">프로모션</font></a></li>
                
            </ul>
        </div><!-- /navbar -->
    </header>

	<div class="content" data-role="content">
		<table style="width:100%;">
    		<tr>
    			<td style="width:50%;">
	    			<a class="btn btn-info btn-labeled" href="#" role="button" style="width:99%;" onclick="orderListShow();">
		                <span class="btn-label">
		                <i class="glyphicon glyphicon-info-sign"></i></span>세탁진행
		            </a>
	            </td>
    			<td style="width:50%;">
    				<a class="btn btn-warning btn-labeled" href="#" role="button" style="width:99%;" onclick="orderCompleteShow();">
                		<span class="btn-label">
                		<i class="glyphicon glyphicon-bookmark"></i></span>세탁완료 
            		</a>
            	</td>
    		</tr>
    	</table>
    	
    	<div style="width:100%;font-size:17px;" id="listOrderDiv">
    		<p style="width:100%;background-color:#CC3D3D;color:white;text-align:center;border-radius:5px;">진행중목록</p>
    		<table style="width:100%;">
    			<tr>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;">주문일</th>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;">담당</th>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;">상태</th>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;"></th>
    			</tr>
    			<c:forEach var="list" items="${orderList }" varStatus="i">
    				<c:if test="${list.state ne '4' }">
	    				<tr>
	    					<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;">[${list.inDate.substring(5,16) }]</td>
	    					<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;">${list.staffName }</td>
	    					<c:choose>
		    					<c:when test="${list.state == '1'}">
		    						<td onclick="" style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;color:#050099;font-weight:bold;">수거대기</td>
		    					</c:when>
		    					<c:when test="${list.state == '2'}">
		    						<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;color:#22741C;font-weight:bold;">수거완료</td>
		    					</c:when>
		    					<c:when test="${list.state == '3'}">
		    						<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;color:#660033;font-weight:bold;">세탁중</td>
		    					</c:when>
		    					<c:otherwise>
		    						<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;color:#660033;font-weight:bold;"></td>
		    					</c:otherwise>
	    					</c:choose>
	    					<td onclick="openNClose(${i.index})" style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;">▽</td>
	    				</tr>
	    				<tr id="tr${i.index }" style="background-color:#ddd;font-size:15px;display:none;">
	    					<input type="hidden" id="openChk" value="1"/>
	    					<td style="text-align:center;">
	    						${list.userId }
	    						<br/>(<a href="tel:${list.userHp }">${list.userHp}</a>)
	    					</td>
	    					<td colspan="3">
	    						<c:forEach var="itemList" items="${list.orderList }" varStatus="j">
	    							<c:if test="${!j.last }">
	    								${itemList.name}(${itemList.orderCount}벌),
	    							</c:if>
	    							<c:if test="${j.last }">
	    								${itemList.name}(${itemList.orderCount}벌)
	    							</c:if>
	    						</c:forEach>
	    					</td>
	    				</tr>
    				</c:if>
    			</c:forEach>
    		</table>
    	</div>
    	
    	<div style="width:100%;font-size:17px;display:none;" id="orderCompleteDiv">
    		<p style="width:100%;background-color:#CC3D3D;color:white;text-align:center;border-radius:5px;">완료목록</p>
    		<table style="width:100%;">
    			<tr>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;">주문일</th>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;">담당</th>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;">상태</th>
    				<th style="font-weight:bold;text-align:center;border-bottom:2px solid #ddd;"></th>
    			</tr>
    			<c:forEach var="list" items="${orderList }" varStatus="i">
    				<c:if test="${list.state eq '4' }">
	    				<tr>
	    					<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;">[${list.inDate.substring(5,16) }]</td>
	    					<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;">${list.staffName }</td>
	    					<td style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;color:#22741C;font-weight:bold;">세탁완료</td>
	    					<td onclick="openNClose2(${i.index})" style="font-size:15px;text-align:center;padding-top:7px;padding-bottom:7px;">▽</td>
	    				</tr>
	    				<tr id="2tr${i.index }" style="background-color:#ddd;font-size:15px;display:none;">
	    					<input type="hidden" id="openChk" value="1"/>
	    					<td style="text-align:center;">
	    						${list.userId }
	    						<br/>(<a href="tel:${list.userHp }">${list.userHp}</a>)
	    					</td>
	    					<td colspan="3">
	    						<c:forEach var="itemList" items="${list.orderList }" varStatus="j">
	    							<c:if test="${!j.last }">
	    								${itemList.name}(${itemList.orderCount}벌),
	    							</c:if>
	    							<c:if test="${j.last }">
	    								${itemList.name}(${itemList.orderCount}벌)
	    							</c:if>
	    						</c:forEach>
	    					</td>
	    				</tr>
    				</c:if>
    			</c:forEach>
    		</table>
    	</div>
	</div>
	
	<!-- 푸터  -->
    <div style="overflow: hidden;" data-role="footer" data-tap-toggle="false" data-position="fixed" onclick="footerBanner();">
	   <img src="resources/img/foot.png" style="width:100%;height:65px;margin-left:0px;"/>
	</div>
</section>

<section id="page4" data-role="page">
    <header data-role="header" data-tap-toggle="false" data-position="fixed" style="background-color:#F6F6F6;">
    	<img src="resources/img/ac_logo.png"/>
    	<!-- <a class="ui-btn-right" href="#page9" data-icon="gear" style="background-color:rgba(255,255,255,0.5);margin-top:15px;"><font style="font-weight:bold;color:red;">MY</font></a> -->
    	<div data-role="navbar">
            <ul>
                <li><a href="#page1" data-transition="none"><font style="font-size:16px;">상품</font></a></li>
                <li><a href="#page2" data-transition="none"><font style="font-size:16px;">체인점</font></a></li>
                <li><a href="#page3" data-transition="none"><font style="font-size:16px;">세탁물</font></a></li>
                <li><a href="#" class="ui-btn-active ui-state-persist"><font style="font-size:16px;">프로모션</font></a></li>
                
            </ul>
        </div><!-- /navbar -->
    </header>
    
    <div class="content" data-role="content">
    	<div style="width:100%;border:1px solid #ddd;background-color:#670000;color:#FFFFFF;padding-top:2px;">
    		<p style="width:100%;text-align:center;font-size:15px;">회원 푸쉬알림 발송</p>
    	</div>
    	<div style="width:100%;border:1px solid #ddd;background-color:#FAECC5;padding-top:5px;">
   			<!-- <p>&nbsp;◇ 한달 2만원으로 누리는 업체홍보!!</p> -->
   			<p style="padding-top:3px;">&nbsp;◇ 보내실 내용을 입력하시면 세탁풍경 앱 회원분들께 푸쉬알림이 발송됩니다</p>
   			
   		</div>
   		<br/>
   		
   		<div>
   			<textarea style="width:100%;" placeholder="메세지 입력" name="sendMsg" id="sendMsg"></textarea>
   		</div>
   		<div>
    		<table style="width:100%;">
	    		<tr>
	    			<td style="width:100%;">
		    			<button type="button" class="btn btn-block btn-default" onclick="sendPush();">알림 보내기</button>
		            </td>
	    			
	    		</tr>
    		</table>
    	</div>
    </div>
    
    <!-- 푸터  -->
    <div style="overflow: hidden;" data-role="footer" data-tap-toggle="false" data-position="fixed" onclick="footerBanner();">
	   <img src="resources/img/foot.png" style="width:100%;height:65px;margin-left:0px;"/>
	</div>
</section> 

<section id="page5" data-role="page">
	<header data-role="header" data-tap-toggle="false" data-position="fixed" style="background-color:#F6F6F6;">
    	<img src="resources/img/ac_logo.png"/>
    	<!-- <a class="ui-btn-right" href="#page9" data-icon="gear" style="background-color:rgba(255,255,255,0.5);margin-top:15px;"><font style="font-weight:bold;color:red;">MY</font></a> -->
    	<!-- <div data-role="navbar">
            <ul>
                <li><a href="#page1" data-transition="none"><font style="font-size:16px;">상품관리</font></a></li>
                <li><a href="#page2" data-transition="none"><font style="font-size:16px;">체인점관리</font></a></li>
                <li><a href="#" class="ui-btn-active ui-state-persist"><font style="font-size:16px;">프로모션</font></a></li>
                
            </ul>
        </div> --><!-- /navbar -->
    </header>
    
    <div class="content" data-role="content">
    	<div style="margin-top:50px;text-align:center;">
    		<p style="color:#030066;font-weight:bold;">관리자 전용페이지 입니다</p>
    		<input type="password" class="form-control" id="loginUserId" placeholder="관리자 휴대폰번호 입력"/>
    	</div>
    	<button type="button" class="btn btn-labeled btn-default" style="font-weight:bold;" onclick="login();">시작하기</button>
    </div>
</section>
</body>
</html>