package com.exciting.vvue.memory.service;

import com.exciting.vvue.common.exception.ErrorCode;
import com.exciting.vvue.common.exception.VvueException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.memory.MemoryService;
import com.exciting.vvue.memory.dto.MemoryAlbumDataDto;
import com.exciting.vvue.memory.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.memory.dto.res.MemoryAlbumResDto;
import com.exciting.vvue.memory.dto.res.MemoryResDto;
import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.model.PlaceMemoryImage;
import com.exciting.vvue.memory.model.ScheduleMemory;
import com.exciting.vvue.memory.model.UserMemory;
import com.exciting.vvue.picture.exception.PictureNotFoundException;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.place.repository.PlaceRepositoryImpl;
import com.exciting.vvue.schedule.repository.ScheduleRepositoryImpl;
import com.exciting.vvue.user.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemoryServiceImpl implements MemoryService {

  private final ScheduleRepositoryImpl scheduleRepository;

  private final ScheduleMemoryRepository scheduleMemoryRepository;
  private final UserMemoryRepository userMemoryRepository;

  private final PlaceMemoryRepository placeMemoryRepository;
  private final PlaceMemoryImageRepository placeMemoryImageRepository;

  private final PictureRepository pictureRepository;
  private final PlaceRepositoryImpl placeRepository;

  @Override
  @Transactional
  public Long add(MemoryAddReqDto memoryAddReqDto, User user, Married userMarried) {
    Long scheduleId = memoryAddReqDto.getScheduleId();

    scheduleRepository.findByIdAndMarriedId(scheduleId, userMarried.getId())
        .orElseThrow(
            () -> new ScheduleNotFoundException(
                "[부부ID]에 해당하는 [스케줄ID]가 존재하지 않습니다" + userMarried.getId() + " " + scheduleId));

    // ScheduleMemory 저장
    LocalDate day = LocalDate.parse(memoryAddReqDto.getScheduleDate(),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    ScheduleMemory scheduleMemory = scheduleMemoryRepository.findByScheduleIdAndMarriedIdAndDate(
        scheduleId, userMarried.getId(), day);
    if (scheduleMemory == null) { // 부부 중 아무도 작성하지 않음
      scheduleMemory = scheduleMemoryRepository.save(
          ScheduleMemory.with(memoryAddReqDto, userMarried));
    }

    // UserMemory 저장
    UserMemory userMemory = userMemoryRepository.findByUserIdAndScheduleMemoryId(user.getId(),
        scheduleMemory.getId(), scheduleMemory.getScheduleDate());
    if (userMemory != null) {
      throw new UserMemoryAlreadyExists(
          "[유저ID]가 작성한 [스케줄ID]에 대한 [추억ID]가 이미 존재합니다" + user.getId() + " " + scheduleId + " "
              + userMemory.getId());
    }
    String comment = memoryAddReqDto.getComment();
    Picture userMemoryPicture = pictureRepository.findById(memoryAddReqDto.getPictureId())
        .orElseThrow(
            () -> new PictureNotFoundException(
                "[사진ID]는 존재하지 않습니다 " + memoryAddReqDto.getPictureId()));

    userMemoryRepository.save(
        UserMemory.with(comment, userMemoryPicture, scheduleMemory, user));

    // PlaceMemory 저장
    List<PlaceMemoryReqDto> placeMemoryReqDtos = memoryAddReqDto.getPlaceMemories();
    // 미리 Place 및 Picture 조회
    Set<Long> placeIds = placeMemoryReqDtos.stream()
        .map(req -> Long.parseLong(req.getPlace().getId()))
        .collect(Collectors.toSet());
    Set<Long> pictureIds = placeMemoryReqDtos.stream()
        .flatMap(req -> req.getPictureIds().stream())
        .collect(Collectors.toSet());

    Map<Long, Place> placeMap = placeRepository.findAllById(new ArrayList<>(placeIds)).stream()
        .collect(Collectors.toMap(Place::getId, Function.identity()));
    Map<Long, Picture> pictureMap = pictureRepository.findAllById(pictureIds).stream()
        .collect(Collectors.toMap(Picture::getId, Function.identity()));

    // PlaceMemory 및 PlaceMemoryImage 저장
    List<PlaceMemory> placeMemories = new ArrayList<>();
    List<PlaceMemoryImage> placeMemoryImages = new ArrayList<>();

    for (PlaceMemoryReqDto placeMemoryReqDto : placeMemoryReqDtos) {
      Long placeId = Long.parseLong(placeMemoryReqDto.getPlace().getId());
      Place place = placeMap.getOrDefault(placeId,
          placeRepository.save(Place.from(placeMemoryReqDto.getPlace())));

      PlaceMemory placeMemory = PlaceMemory.with(placeMemoryReqDto, scheduleMemory, place, user);
      placeMemories.add(placeMemory);

      for (Long pictureId : placeMemoryReqDto.getPictureIds()) {
        Picture picture = pictureMap.get(pictureId);
        if (picture != null) {
          placeMemoryImages.add(PlaceMemoryImage.builder()
              .picture(picture)
              .placeMemory(placeMemory)
              .build());
        }
      }
    }

    // Batch Save
    placeMemoryRepository.saveAll(placeMemories);
    placeMemoryImageRepository.saveAll(placeMemoryImages);

    return scheduleMemory.getId();
  }

  @Override
  public MemoryResDto getById(Long scheduleMemoryId, Long userId) throws MemoryNotFoundException {

    /*
     * {id:, scheduleId:, scheduleName:, scheduleDate:,
     *   userMemories:[
     *       { id: , user:{id:, nickname:, email:, profile:{id:, url:} } , picture: {id:, url:} , comment: },
     *       { id: , user:{id:, nickname:, email:, profile:{id:, url:} } , picture: {id:, url:} , comment: }
     *   ],
     *   placeMemories:[
     *       { placeId : 1,
     *          //TODO: place명 추가
     *         comments : [
     *              {
     *                  id : 1,
     *                  rating : 1.5,
     *                  comment : "",
     *                  userDto : {
     *                      id : 1,
     *                      nickname:,
     *                      picture: { id: , url: }
     *                  }
     *              }
     *          ]
     *       }
     *   ]
     * }
     * */
    // {id:, scheduleId:, scheduleName:, scheduleDate:,
    Optional<ScheduleMemory> scheduleMemory = scheduleMemoryRepository.findById(
        scheduleMemoryId);
    if (scheduleMemory.isEmpty()) {
      throw new VvueException(ErrorCode.MEMORY_NOT_FOUND);
    }
    if (!scheduleMemory.get().getMarried().isMarried(userId)) {
      throw new VvueException(ErrorCode.FORBIDDEN_ACCESS);
    }
    List<UserMemory> userMemories = userMemoryRepository.findByScheduleMemory_Id(scheduleMemoryId);
    List<PlaceMemory> placeMemories = placeMemoryRepository.findByScheduleMemory_Id(
        scheduleMemoryId);
    return MemoryResDto.from(scheduleMemory.get(), userMemories, placeMemories);

  }

  @Override
  public void deleteById(Long memoryId, Long userId) {
    Optional<ScheduleMemory> scheduleMemory = scheduleMemoryRepository.findById(
        memoryId);
    if (scheduleMemory.isEmpty()) {
      throw new VvueException(ErrorCode.MEMORY_NOT_FOUND);
    }
    if (!scheduleMemory.get().getMarried().isMarried(userId)) {
      throw new VvueException(ErrorCode.FORBIDDEN_ACCESS);
    }
    scheduleMemoryRepository.deleteById(memoryId);
  }

  @Override
  public MemoryAlbumResDto getAllThumbnail(Married married, Long nextCursor,
      int size) {
    List<ScheduleMemory> scheduleMemories = scheduleMemoryRepository.findByMarriedIdWithCursor(
        married.getId(), nextCursor, size);

    List<MemoryAlbumDataDto> res = scheduleMemories.stream()
        .map(x -> {
          String imgUrl = "";
          for (UserMemory userMemory : x.getUserMemories()) {
            imgUrl = userMemory.getPicture().getUrl();
          }
          return new MemoryAlbumDataDto(x.getId(), imgUrl);
        })
        .toList();

    if (res.isEmpty()) {
      return MemoryAlbumResDto.builder().hasNext(false).build();
    }

    Long lastCursorId = scheduleMemories.get(scheduleMemories.size() - 1).getId();

    boolean hasNext =
        scheduleMemoryRepository.countByMarriedIdAndIdGreaterThan(married.getId(), lastCursorId)
            > 0;

    return MemoryAlbumResDto.builder()
        .allMemories(res)
        .lastCursorId(lastCursorId)
        .hasNext(hasNext)
        .build();
  }
}
