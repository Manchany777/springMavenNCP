package user.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import user.bean.UserImageDTO;
import user.service.ObjectStorageService;
import user.service.UserService;

@Controller
@RequestMapping(value="user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectStorageService objectStorageService;
	
	private String bucketName = "bitcamp-edu-bucket-112";  // 내가 발급받은 고유한 버킷 이름
	
	@GetMapping(value="/uploadForm")
	public String uploadForm() {
		return "/user/uploadForm";
	}
	
	// MappingJackson2HttpMessageConverter 가 jackson 라이브러리를 이용해
    // 자바 객체를 JSON 문자열로 변환하여 클라이언트로 보낸다.
    // 이 컨버터를 사용하면 굳이 UTF-8 변환을 설정할 필요가 없다.
    // 즉 produces = "application/json;charset=UTF-8" 를 설정하지 않아도 된다.
	//@PostMapping(value="/upload")  // => produces = "application/json;charset=UTF-8" 없이 JSON문자열 한글처리하기 실패...
	@PostMapping(value="/upload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String upload(@ModelAttribute UserImageDTO userImageDTO, 
						 @RequestParam("img[]") List<MultipartFile> list, 
						 HttpSession session) {
						// HttpSession을 사용 - 가상 폴더에 안올리고 실제 폴더에 올림
		// 실제 폴더
		String filepath = session.getServletContext().getRealPath("/WEB-INF/storage");
		System.out.println("실제폴더 = " + filepath);
		
		File file;
		String originalFileName;
		//String result = ""; // 상품등록 완료 확인
		String fileName;
		
		
		// 파일명만 모아서 DB로 보내기
		List<UserImageDTO> userImageList = new ArrayList<UserImageDTO>();
		
		for(MultipartFile img : list) { // @RequestParam("img[]") List<MultipartFile> list의 list	
			originalFileName = img.getOriginalFilename();
			System.out.println(originalFileName);
			
			fileName = objectStorageService.uploadFile(bucketName, "storage/", img); 
			//  objectStorageService의 uploadFile에서 bucketName의 storage/에 있는 img를 가져와서 fileName에 담는다
			
			file = new File(filepath, originalFileName);
			
			try {
				img.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/*
			result += "<span><img src='/springMavenNCP/storage/" 
					+ originalFileName 
					+ "' width='200' height='200' /></span>";*/
			
			UserImageDTO dto = new UserImageDTO();
			dto.setImageName(userImageDTO.getImageName()); // 상품명
			dto.setImageContent(userImageDTO.getImageContent()); // 상품내용
			//dto.setImageFileName("");		 // UUID - Object에 올라갈 수 있도록 처리애햐함(클라우드에서 이름을 받아올 거라 여기선 공백으로 둔다.)
			//dto.setImageFileName("noname");  // 현재는 "noname"이 뜨게 바꿈
			dto.setImageFileName(fileName);  // NCloud에 UUID로 fileName 삽입
			dto.setImageOriginalName(originalFileName);
			
			userImageList.add(dto); // dto값을 list에 보관
		}//for
		
		// DB
		userService.upload(userImageList);
		
		return "이미지 등록 완료";
	}
	
	@GetMapping("/uploadList")
	public String uploadList() {
		// 동적처리 - 지금은 DB로 가서 데이터를 가져오지 않는다.
		return "/user/uploadList";
	}
	
	@PostMapping("/getUploadList")
	@ResponseBody
	public List<UserImageDTO> getUploadList() {
		return userService.getUploadList();
	}
}
