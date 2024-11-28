//package com.exciting.vvue.picture;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.transaction.Transactional;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.exciting.vvue.auth.AuthService;
//import com.exciting.vvue.picture.exception.FileDeleteFailException;
//import com.exciting.vvue.picture.exception.FileUploadFailException;
//import com.exciting.vvue.picture.model.dto.MetaReqDto;
//import com.exciting.vvue.picture.model.dto.PictureIdList;
//import com.exciting.vvue.picture.model.dto.PictureMultiUploadResDto;
//import com.exciting.vvue.picture.model.dto.PictureSingleUploadResDto;
//import com.exciting.vvue.picture.service.PictureService;
//import com.exciting.vvue.user.exception.UserNotFoundException;
//import com.exciting.vvue.user.UserService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RestController
//@RequestMapping("/pictures")
//@RequiredArgsConstructor
//public class PictureController {
//	private final PictureService pictureService;
//	private final AuthService authService;
//	private final UserService userService;
//	@Transactional
//	@PostMapping("/upload/multi")
//	@Operation(description ="사진 여러장 업로드", summary = "장소 추억 전용")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "업로드 성공",  content = {@Content(schema = @Schema(implementation = PictureMultiUploadResDto.class))}),
//		@ApiResponse(responseCode = "400", description = "업로드 실패"),
//		@ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
//	})
//	public ResponseEntity<?> uploadMulti(
//		@RequestHeader("Authorization")String token,
//		@RequestPart(value="meta", required = false) MetaReqDto meta,
//		@RequestPart(value="pictures", required = false) List<MultipartFile> multipartFiles){
//		//log.debug("[POST] /images/upload : images pieces " + fileTypes.size());
//		//log.debug("[POST] /images/upload : fileTypes " + imageTypes.toString());
//		if(userService.getUserById(authService.getUserIdFromToken(token)) == null )
//			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
//
//		if(multipartFiles.size() == 0)
//			throw new FileUploadFailException("업로드 할 이미지가 없어요");
//		// meta != null 유지할 정보가 있는 경우, 추억블록 id
//		if(meta != null){
//			log.debug("[POST] /pictures/upload/multi : meta " + meta.toString());
//			/**
//			 * todo
//			 * 수정 시 로직
//			 * 장소 블럭 수정시 구현해야 함
//			 */
//		}
//		// meta == null
//		// 유지할 정보가 없는 경우
//		log.debug("[POST] /pictures/upload/multi : muiltipartfiles number " + multipartFiles.size() );
//		List<String> filePaths = new ArrayList<>();
//		for(int i = 0; i < multipartFiles.size(); i++){
//			// 받은 사진 -> S3 업로드 url filePaths에 추가
//			if(multipartFiles.get(i) != null && !multipartFiles.get(i).isEmpty()){
//				filePaths.add(pictureService.uploadSingle(multipartFiles.get(i)));
//			}
//			else throw new FileUploadFailException("이미지 업로드 중 문제가 발생하였습니다.");
//		}
//
//		//이미지 메타 정보가 있으면 meta
//		List<Long> imagesIdList = pictureService.insertMulti(filePaths);
//		log.debug("[POST] /pictures/upload/single : url " + imagesIdList.toString());
//
//		PictureMultiUploadResDto pictureMultiUploadResDto = PictureMultiUploadResDto.builder()
//			.meta(meta)
//			.pictureIdList(imagesIdList).build();
//		return new ResponseEntity<>(pictureMultiUploadResDto, HttpStatus.OK);
//	}
//
//	@Transactional
//	@PostMapping("/upload/single")
//	@Operation(description ="사진 1장 업로드", summary = "프로필 변경, 부부공유 사진 변경")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "업로드 성공",  content = {@Content(schema = @Schema(implementation = PictureSingleUploadResDto.class))}),
//		@ApiResponse(responseCode = "400", description = "업로드 실패"),
//		@ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
//	})
//	public ResponseEntity<?> uploadSingle(
//		@RequestHeader("Authorization")String token,
//		@RequestPart(value="meta", required = false) MetaReqDto meta,
//		@RequestPart(value="picture", required = false) MultipartFile multipartFile){
//		if(userService.getUserById(authService.getUserIdFromToken(token)) == null )
//			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
//
//		if(meta != null){
//			log.debug("[POST] /pictures/upload/single : meta " + meta.toString());
//			/**
//			 * todo
//			 * 수정 시 로직
//			 * 장소 블럭 수정시 구현해야 함
//			 */
//		}
//		if(multipartFile == null || multipartFile.isEmpty())
//			throw new FileUploadFailException("이미지 업로드 중 문제가 발생하였습니다.");
//
//		String url = pictureService.uploadSingle(multipartFile);
//		log.debug("[POST] /pictures/upload/single : url " + url);
//		Long pictureId = pictureService.insertSingle(url);
//		log.debug("[POST] /pictures/upload/single : pictureId " + pictureId);
//
//		PictureSingleUploadResDto pictureSingleUploadResDto = PictureSingleUploadResDto.builder()
//			.meta(meta)
//			.id(pictureId)
//			.build();
//		return new ResponseEntity<>(pictureSingleUploadResDto, HttpStatus.OK);
//	}
//
//	@Transactional
//	@DeleteMapping("/single/{pictureId}")
//	@Operation(description ="사진 1장 삭제", summary = "사진 삭제")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "삭제 성공"),
//		@ApiResponse(responseCode = "400", description = "삭제 실패"),
//		@ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음"),
//	})
//	public ResponseEntity<?> delete(@RequestHeader("Authorization")String token, @PathVariable("pictureId") Long id){
//		if(userService.getUserById(authService.getUserIdFromToken(token)) == null )
//			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
//		if(pictureService.getSingle(id) == null)
//			throw new FileDeleteFailException("없거나 이미 삭제한 이미지입니다");
//		pictureService.deleteSingle(id);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//
//	@Transactional
//	@DeleteMapping("/multi")
//	@Operation(description ="사진 여러장 삭제", summary = "사진 삭제")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "삭제 성공"),
//		@ApiResponse(responseCode = "404", description = "삭제 실패"),
//		@ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
//	})
//	public ResponseEntity<?> deleteMulti(@RequestHeader("Authorization")String token, @RequestBody PictureIdList pictureIdList){
//		if(userService.getUserById(authService.getUserIdFromToken(token)) == null )
//			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
//		if(pictureIdList.getPictureIds() == null || pictureIdList.getPictureIds().size() == 0)
//			throw new FileDeleteFailException("삭제할 파일이 없어요.");
//		pictureService.deleteMulti(pictureIdList.getPictureIds());
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//}
