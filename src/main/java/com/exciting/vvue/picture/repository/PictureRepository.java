package com.exciting.vvue.picture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exciting.vvue.picture.model.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

}
