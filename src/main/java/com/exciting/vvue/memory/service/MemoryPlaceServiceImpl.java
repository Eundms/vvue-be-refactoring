package com.exciting.vvue.memory.service;

import com.exciting.vvue.memory.MemoryPlaceService;
import com.exciting.vvue.memory.dto.MemoryPlaceFindDto;
import com.exciting.vvue.memory.dto.res.MemoryAtPlaceDto;
import com.exciting.vvue.memory.dto.res.PlaceMemoryToMapDto;
import com.exciting.vvue.memory.model.PlaceMemory;
import com.exciting.vvue.memory.repository.jpa.PlaceMemoryJpaRepository;
import com.exciting.vvue.picture.dto.PictureDto;
import com.exciting.vvue.place.dto.res.PlaceResDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MemoryPlaceServiceImpl implements MemoryPlaceService {

  private final PlaceMemoryJpaRepository placeMemoryJpaRepository;

  @Override
  public List<PlaceMemoryToMapDto> getMemoriablePlaceByMarriedId(Long marriedId,
      MemoryPlaceFindDto findCondition) {
    List<PlaceMemory> placeMemories = placeMemoryJpaRepository.findAllByMarriedId(marriedId);

    Map<Long, List<PlaceMemory>> groupedByPlaceId = placeMemories.stream()
        .collect(Collectors.groupingBy(pm -> pm.getPlace().getId()));
    List<PlaceMemoryToMapDto> placeMemoryToMapDtos = groupedByPlaceId.entrySet().stream()
        .map(entry -> {
          Long placeId = entry.getKey();
          List<PlaceMemory> placeMemoryList = entry.getValue();
          // 3. 각 장소에 대한 PlaceResDto 생성 (모든 PlaceMemory의 장소 정보를 바탕으로 하나의 DTO 생성)
          PlaceResDto placeResDto = PlaceResDto.from(placeMemoryList.get(0).getPlace());
          Map<Long, List<PlaceMemory>> groupedByScheduleId = placeMemoryList.stream()
              .collect(Collectors.groupingBy(pm -> pm.getScheduleMemory().getId()));
          List<MemoryAtPlaceDto> memoryAtPlaceDtos = groupedByScheduleId.entrySet().stream()
              .map(scheduleEntry -> {
                Long scheduleId = scheduleEntry.getKey();
                List<PlaceMemory> scheduleMemoryList = scheduleEntry.getValue();
                // 모든 scheduleMemory에 대해 정보를 추출
                String scheduleName = scheduleMemoryList.stream()
                    .findFirst() // 첫 번째 값을 가져오는 대신 stream을 이용해 적절한 값을 찾음
                    .map(pm -> pm.getScheduleMemory().getScheduleName())
                    .orElse("Unknown Schedule");

                LocalDate scheduleDate = scheduleMemoryList.stream()
                    .findFirst() // 첫 번째 값을 가져오는 대신 stream을 이용해 적절한 값을 찾음
                    .map(pm -> pm.getScheduleMemory().getScheduleDate())
                    .orElse(LocalDate.now()); // 기본값을 설정할 수 있음

                // 장소 사진 정보
                List<PictureDto> placePictures = scheduleMemoryList.stream()
                    .flatMap(pm -> pm.getPlaceMemoryImageList().stream())
                    .map(image -> new PictureDto(image.getPicture().getId(),
                        image.getPicture().getUrl()))
                    .collect(Collectors.toList());

                // MemoryAtPlaceDto 생성
                return new MemoryAtPlaceDto(scheduleId, scheduleName, scheduleDate, placePictures);
              })
              .toList();
          return new PlaceMemoryToMapDto(placeResDto, memoryAtPlaceDtos);
        }).toList();

    if (findCondition != null && findCondition.getX() != null && findCondition.getY() != null) {
      BigDecimal x = findCondition.getX();
      BigDecimal y = findCondition.getY();
      placeMemoryToMapDtos.sort(Comparator.comparing(dto
          -> calculateHaversineDistance(x, y, new BigDecimal(dto.getPlace().getX()),
          new BigDecimal(dto.getPlace().getY()))
      ));
    }
    return placeMemoryToMapDtos;
  }

  public double calculateHaversineDistance(BigDecimal x, BigDecimal y, BigDecimal placeLat,
      BigDecimal placeLon) {
    final double EARTH_RADIUS = 6371.0; // 지구 반지름 (단위: km)

    // 위도, 경도를 라디안으로 변환
    double lat1 = x.doubleValue();  // 주어진 x (위도)
    double lon1 = y.doubleValue();  // 주어진 y (경도)
    double lat2 = placeLat.doubleValue();  // 장소의 위도
    double lon2 = placeLon.doubleValue();  // 장소의 경도

    // 라디안으로 변환
    lat1 = Math.toRadians(lat1);
    lon1 = Math.toRadians(lon1);
    lat2 = Math.toRadians(lat2);
    lon2 = Math.toRadians(lon2);

    // 위도와 경도의 차이
    double dLat = lat2 - lat1;
    double dLon = lon2 - lon1;

    // Haversine 공식 계산
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1) * Math.cos(lat2) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    // 거리 계산 (km 단위)
    return EARTH_RADIUS * c;
  }

}
