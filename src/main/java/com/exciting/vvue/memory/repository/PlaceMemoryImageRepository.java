package com.exciting.vvue.memory.repository;

import com.exciting.vvue.memory.model.PlaceMemoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceMemoryImageRepository extends JpaRepository<PlaceMemoryImage, Long> {

}

