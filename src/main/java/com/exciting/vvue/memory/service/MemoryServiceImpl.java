package com.exciting.vvue.memory.service;

import com.exciting.vvue.auth.exception.UserUnAuthorizedException;
import com.exciting.vvue.married.model.Married;
import com.exciting.vvue.memory.exception.MemoryNotFoundException;
import com.exciting.vvue.memory.exception.UserMemoryAlreadyExists;
import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.model.PlaceMemoryImage;
import com.exciting.vvue.memory.model.ScheduleMemory;
import com.exciting.vvue.memory.model.UserMemory;
import com.exciting.vvue.memory.model.dto.MemoryAlbumDataDto;
import com.exciting.vvue.memory.model.dto.req.MemoryAddReqDto;
import com.exciting.vvue.memory.model.dto.req.PlaceMemoryReqDto;
import com.exciting.vvue.memory.model.dto.res.MemoryAlbumResDto;
import com.exciting.vvue.memory.model.dto.res.MemoryResDto;
import com.exciting.vvue.memory.repository.PlaceMemoryImageRepository;
import com.exciting.vvue.memory.repository.PlaceMemoryRepository;
import com.exciting.vvue.memory.repository.ScheduleMemoryRepository;
import com.exciting.vvue.memory.repository.UserMemoryRepository;
import com.exciting.vvue.picture.exception.PictureNotFoundException;
import com.exciting.vvue.picture.model.Picture;
import com.exciting.vvue.picture.repository.PictureRepository;
import com.exciting.vvue.place.model.Place;
import com.exciting.vvue.place.repository.PlaceRepository;
import com.exciting.vvue.schedule.exception.ScheduleNotFoundException;
import com.exciting.vvue.schedule.model.Schedule;
import com.exciting.vvue.schedule.repository.ScheduleRepository;
import com.exciting.vvue.user.model.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemoryServiceImpl implements MemoryService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMemoryRepository scheduleMemoryRepository;
    private final UserMemoryRepository userMemoryRepository;

    private final PlaceMemoryRepository placeMemoryRepository;
    private final PlaceMemoryImageRepository placeMemoryImageRepository;

    private final PictureRepository pictureRepository;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public Long add(MemoryAddReqDto memoryAddReqDto, User user, Married userMarried) {
        Long scheduleId = memoryAddReqDto.getScheduleId();

        scheduleRepository.findByIdAndMarriedId(scheduleId, userMarried.getId())
                .orElseThrow(
                        () -> new ScheduleNotFoundException(
                                "[부부ID]에 해당하는 [스케줄ID]가 존재하지 않습니다" + userMarried.getId() + " " + scheduleId));

        // ScheduleMemory 저장
        ScheduleMemory scheduleMemory = scheduleMemoryRepository.findByScheduleIdAndMarriedId(
            scheduleId, userMarried.getId());
        if (scheduleMemory == null) { // 부부 중 아무도 작성하지 않음
            scheduleMemory = scheduleMemoryRepository.save(
                ScheduleMemory.with(memoryAddReqDto,  userMarried));
        }

        // UserMemory 저장
        UserMemory userMemory = userMemoryRepository.findByUserIdAndScheduleMemoryId(user.getId(),
            scheduleMemory.getId());
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
        for (PlaceMemoryReqDto placeMemoryReqDto : placeMemoryReqDtos) {
            // PlaceMemoryImage 저장
            Optional<Place> place = placeRepository.findById(
                Long.parseLong(placeMemoryReqDto.getPlace().getId()));
            if (place.isEmpty()) {
                place = Optional.of(placeRepository.save(Place.from(placeMemoryReqDto.getPlace())));
            }
            PlaceMemory placeMemory = placeMemoryRepository.save(
                PlaceMemory.with(placeMemoryReqDto, scheduleMemory, place.get(), user));
            for (Long pictureId : placeMemoryReqDto.getPictureIds()) {
                Picture picture = pictureRepository.findById(pictureId).get();
                placeMemoryImageRepository.save(PlaceMemoryImage.builder()
                    .picture(picture)
                    .placeMemory(placeMemory)
                    .build());
            }
        }
        return scheduleMemory.getId();
    }

    @Override
    public MemoryResDto getById(Long scheduleMemoryId, User user) throws MemoryNotFoundException {

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
            log.debug("[추억ID] 잘못됨");
            throw new MemoryNotFoundException("[추억ID]가 존재하지 않습니다" + scheduleMemoryId);
        }
        if (!scheduleMemory.get().getMarried().isMarried(user.getId())) {
            throw new UserUnAuthorizedException("[유저ID]의 권한이 없는 요청입니다");
        }
        return MemoryResDto.from(scheduleMemory.get());

    }

    @Override
    public void deleteById(Long memoryId, User user) {
        Optional<ScheduleMemory> scheduleMemory = scheduleMemoryRepository.findById(
            memoryId);
        if (scheduleMemory.isEmpty()) {
            log.debug("[추억ID] 잘못됨");
            throw new MemoryNotFoundException("[추억ID]가 존재하지 않습니다" + memoryId);
        }
        if (!scheduleMemory.get().getMarried().isMarried(user.getId())) {
            throw new UserUnAuthorizedException("[유저ID]의 권한이 없는 요청입니다");
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
        if(scheduleMemories.size()==0){
            return MemoryAlbumResDto.builder().hasNext(false).build();
        }
        Long lastCursorId = scheduleMemories.get(scheduleMemories.size()-1).getId();
        List<ScheduleMemory> nextScheduleMemories = scheduleMemoryRepository.findByMarriedIdWithCursor(
            married.getId(), lastCursorId, size);

        return MemoryAlbumResDto.builder()
            .allMemories(res)
            .lastCursorId(lastCursorId)
            .hasNext(nextScheduleMemories.size()==0?false:true)
            .build();
    }
}
