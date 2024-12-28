package com.exciting.vvue.picture.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exciting.vvue.picture.exception.FileDeleteFailException;
import com.exciting.vvue.picture.exception.FileUploadFailException;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.picture.util.FileManageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PictureServiceImpl implements PictureService {
	private final PictureRepository pictureRepository;
	private final FileManageUtil fileManageUtil;

	@Value("${cloud.aws.s3.prefix}")
	private String s3Prifix;

	@Override
	public String uploadSingle(MultipartFile multipartFile) throws FileUploadFailException {
		return s3Prifix + fileManageUtil.uploadFile(multipartFile);
	}

	@Override
	@Deprecated
	public List<String> uploadMulti(List<MultipartFile> multipartFiles) throws
		FileUploadFailException {
		List<String> multiImagesUrls = new ArrayList<>();
		for(int i = 0 ; i < multipartFiles.size(); i++){
			if(multipartFiles.get(i) != null && !multipartFiles.get(i).isEmpty()) {
				multiImagesUrls.add(fileManageUtil.uploadFile(multipartFiles.get(i)));
			}
		}

		return multiImagesUrls;
	}

	@Override
	public List<Long> insertMulti(List<String> filePaths)
		throws FileUploadFailException{
		List<Long> imagesIdList = new ArrayList<>();

		for(int i = 0; i < filePaths.size(); i++){
			Picture newImage = Picture.builder()
				.url(filePaths.get(i))
				.isDeleted(false)
				.build();
			Long id = pictureRepository.save(newImage).getId();
			imagesIdList.add(id);
		}

		return imagesIdList;
	}

	@Override
	public Long insertSingle(String filePath) throws FileUploadFailException {
		Picture newImage = Picture.builder()
			.url(filePath)
			.isDeleted(false)
			.build();
		Long id = pictureRepository.save(newImage).getId();
		return id;
	}

	@Override
	public boolean existSingle(Long imageId) {
		return pictureRepository.existsById(imageId);
	}

	@Override
	public Picture getSingle(Long imageId) {
		return pictureRepository.findById(imageId).get();
	}

	@Override
	public void deleteSingle(Long pictureId) throws FileDeleteFailException{
		Picture deletedPicture = pictureRepository.findById(pictureId).get();
		pictureRepository.delete(deletedPicture);
	}

	@Override
	public void deleteMulti(List<Long> imageIdList) throws FileDeleteFailException {
		for(Long imageId : imageIdList){
			this.deleteSingle(imageId);
		}
	}
}
