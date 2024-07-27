package com.exciting.vvue.picture.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.exciting.vvue.picture.exception.FileDeleteFailException;
import com.exciting.vvue.picture.exception.FileUploadFailException;
import com.exciting.vvue.picture.model.Picture;

public interface PictureService {
	// 단일 이미지 업로드
	String uploadSingle(MultipartFile multipartFile)
		throws FileUploadFailException;

	//여러 이미지 업로드
	List<String> uploadMulti(List<MultipartFile> multipartFiles)
		throws FileUploadFailException;

	// 여러 이미지 Picture 테이블에 저장
	List<Long> insertMulti(List<String> filePaths) throws FileUploadFailException;

	// 단일 이미지 Picture 테이블에 저장
	Long insertSingle(String filePath) throws FileUploadFailException;

	// 이미지 존재 여부 조회
	boolean existSingle(Long imageId);
	// 단일 이미지 조회
	Picture getSingle(Long imageId);
	// 단일 이미지 논리삭제
	void deleteSingle(Long imageId) throws FileDeleteFailException;

	// 여러 이미지 논리 삭제
	void deleteMulti(List<Long> imageIdList) throws FileDeleteFailException;
}
