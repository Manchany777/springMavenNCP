<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
table {
	border-collapse: collapse;
}
th, td {
	padding: 5px;
}
#uploadForm div{
	color: red;
	font-size: 8pt;
	font-weight: bold;
}
</style>
</head>
<body>
<form id="uploadForm">
	<table border="1">
		<tr>
			<th>상품명</th>
			<td>
				<input type="text" name="imageName" id="imageName" size="35">
				<div id="imageNameDiv"></div>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<textarea name="imageContent" id="imageContent" rows="10" cols="50"></textarea>
				<div id="imageContentDiv"></div>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<span id="showImgList"></span>
				
				<img id="camera" src="../image/camera.png" width="30" height="30" alt="카메라">
				<input type="file" name="img[]" id="img" multiple="multiple" style="visibility: hidden;">
			</td>
		</tr>
		
		<tr>
			<td colspan="2" align="center">
				<input type="button" value="이미지 업로드" id="uploadBtn">
				<input type="reset" value="취소">
			</td>
		</tr>
	</table>
	<br>
	
	<div id="resultDiv"></div>
</form>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.7.0.min.js"></script>
<script type="text/javascript" src="../js/upload.js"></script>
<script type="text/javascript">
$('#camera').click(function(){
	$('#img').trigger('click'); // 강제 이벤트 발생
});


// 업로드 버튼을 누르기 전에 선택한 이미지가 맞는지 확인하기 위해서 이미지를 보여준다.
// 이미지 1개만 미리보기
/*$('#img').change(function(){
	readURL(this);
}); */

// 여러 개의 이미지 미리보기
$('#img').change(function(){
	$('#showImgList').empty();
	for(i=0; i<this.files.length; i++) {
		readURL(this.files[i]);
	}
});

// 이미지 1개만 미리보기
/*
function readURL(input) {
	var reader = new FileReader();
	
	reader.onload = function(e) {
		$('#showImg').attr('src', e.target.result); // e.target - 이벤트가 발생하는 요소를 반환해준다.
		
		//show += `<img src="e.target.result" width="70" height="70" />`;
	}
	
	reader.readAsDataURL(input.files[0]);
}*/

// 여러 개의 이미지 미리보기
function readURL(file) {
	var reader = new FileReader();
	
	var show; 
	reader.onload = function(e) {  // 태그 만들어주는 역할
		var img = document.createElement('img');
		img.src = e.target.result;
		img.width = 70;
		img.height = 70;
		$('#showImgList').append(img);
	}
	
	reader.readAsDataURL(file);
}

</script>
</body>
</html>