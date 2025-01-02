package com.exciting.vvue.notification.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.exciting.vvue.notification.model.VvueNotification;
import com.exciting.vvue.notification.repository.jpa.VvueNotificationJpaRepository;
import com.exciting.vvue.notification.service.VvueNotificationRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VvueNotificationRepositoryImpl implements VvueNotificationRepository {

	private final VvueNotificationJpaRepository vvueNotificationJpaRepository;

	@Override
	public List<VvueNotification> findByReceiverId(Long userId) {
		return vvueNotificationJpaRepository.findByReceiverId(userId);
	}

	@Override
	public int countUnReadByReceiverId(Long userId) {
		return vvueNotificationJpaRepository.countUnReadByReceiverId(userId);
	}

	@Override
	public void readAllUnReadNotify(Long userId) {
		vvueNotificationJpaRepository.readAllUnReadNotify(userId);
	}

	@Override
	public void readUnReadNotify(Long userId, Long notificationId) {
		vvueNotificationJpaRepository.readUnReadNotify(userId, notificationId);
	}

	@Override
	public void save(VvueNotification vvueNotification) {
		vvueNotificationJpaRepository.save(vvueNotification);
	}
}
