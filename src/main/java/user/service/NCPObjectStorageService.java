package user.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class NCPObjectStorageService implements ObjectStorageService {
	// pom.xml에 추가한 aws드라이브에 내장된 변수
	final AmazonS3 s3;
	
	// NaverConfiguration의 설정값을 인자로 주입
	public NCPObjectStorageService(NaverConfiguration naverConfiguration) {
		s3 = AmazonS3ClientBuilder
				.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder
							.EndpointConfiguration(naverConfiguration.getEndPoint(), 
												   naverConfiguration.getRegionName())
							)
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(naverConfiguration.getAccessKey(), 
												naverConfiguration.getSecretKey())
						)
				).build();
	}
	
	@Override
	public String uploadFile(String bucketName, String directoryPath, MultipartFile img) {
		if(img.isEmpty())return null;
		
		try(InputStream fileIn = img.getInputStream()) { // 파일내용을 읽어오도록
			//String fileName = img.getOriginalFilename();	// 파일의 원래 이름으로 올리기
			String fileName = UUID.randomUUID().toString(); // UUID로 올리기
			
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(img.getContentType());
			
			PutObjectRequest objectRequest = new PutObjectRequest(bucketName, 
																	 directoryPath + fileName, 
																 	 fileIn, 
																	 objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(objectRequest);
			
			return fileName;
		} catch(Exception e) {
			throw new RuntimeException("파일 업로드 오류", e);
		}
	}

}
