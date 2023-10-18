$(function(){
	$('#uploadBtn').click(function(){
		$('#imageNameDiv').empty();
		$('#imageContentDiv').empty();
		
		if($('#imageName').val() == '') {
			$('#imageNameDiv').text('상품명 입력');
			$('#imageName').focus();
		} else if($('#imageContent').val() == ''){
			$('#imageContentDiv').text('상품 내용 입력');
			$('#imageContent').focus();
		} else {
			var formData = new FormData($('#uploadForm')[0]); // 현재 Form안에 있는 모든 데이터(객체값)을 읽어오기 - serialize 못씀
		
			$.ajax({
				type: 'post',
				enctype: 'multipart/form-data',
				processData : false, // false로 해야 변수=값&변수=값 이렇게 문자열로 전송이 안 됨
				contentType : false,  // 이렇게 해야 multipart로 전송이 가능 
				url: '/springMavenNCP/user/upload',
				data: formData, // FormData가 가져온 모든 정보를 formdata가 가져옴 (파일업로드는 serialize 사용 불가)
				dataType: 'text',
				success: function(data){
					alert(data); // 이 이미지가 실제로 태그 안으로 들어가야 함
					//$('#resultDiv').html(data);
					location.href='/springMavenNCP/user/uploadList';
				},
				error: function(e){
					console.log(e);
				}
			}); // ajax
		}
 	

	});
});

/*
processData
 - 기본값은 true
 - 기본적으로 Query String으로 변환해서 보내진다('변수=값&변수=값')
 - 파일 전송시에는 반드시 false로 해야 한다.(formData를 문자열로 변환하지 않는다)
 
contentType
  - 기본적으로 "application/x-www-form-urlencoded; charset=UTF-8"
  - 파일 전송시에는 'multipart/form-data'로 전송이 될 수 있도록 false로 설정한다
 */