package com.exciting.vvue.picture.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.exciting.vvue.picture.exception.FileUploadFailException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileManageUtil {

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	/**
	 * S3로 파일 업로드
	 */
	public String uploadFile(String fileType, MultipartFile multipartFile)
		throws FileUploadFailException {
		// 업로드하는 파일 경로 만들기
		String uploadFilePath = fileType + "/" + getFolderName();
		log.debug("uploadFilePath :" + uploadFilePath);

		// 파일 이름을 가져와서 UUID로 변환
		String originalFileName = multipartFile.getOriginalFilename();
		String uploadFileName = getUuidFileName(originalFileName);

		// 파일 저장 경로
		String keyName = uploadFilePath + "/" + uploadFileName; // ex) 구분/년/월/일/파일.확장자

		try (InputStream inputStream = multipartFile.getInputStream()) {

			// S3에 파일 업로드 (AWS SDK v2 방식)
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(keyName)
				.contentType(multipartFile.getContentType())
				.build();

			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));

		} catch (IOException e) {
			log.error("File upload failed", e);
			throw new FileUploadFailException("File upload failed");
		}

		return keyName;
	}

	/**
	 * S3에 업로드된 파일 삭제
	 */
	public String deleteFile(String keyName) {
		String result = "success";
		try {
			// S3 버킷에 해당 파일이 존재하는지 확인
			s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(keyName).build());
			// 존재하면 삭제
			s3Client.deleteObject(builder -> builder.bucket(bucketName).key(keyName).build());
		} catch (NoSuchKeyException e) {
			log.debug("File not found: {}", keyName);
			result = "file not found";
		} catch (Exception e) {
			log.error("Delete File failed", e);
			result = "error";
		}

		return result;
	}

	/**
	 * UUID 파일명 반환
	 */
	public String getUuidFileName(String fileName) {
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		return UUID.randomUUID().toString() + "." + ext;
	}

	/**
	 * 년/월/일 폴더명 반환
	 */
	public String getFolderName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
		return sdf.format(new Date());
	}
}
