package com.example.SecureAndBox.login.application;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

	private final int MAX_ATTEMPTS = 5; // 최대 시도 횟수
	private final long LOCK_TIME_DURATION = 15 * 60 * 1000; // 잠금 시간 15분 (밀리초 단위)

	private Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
	private Map<String, Long> lockTimeCache = new ConcurrentHashMap<>();

	public void incrementAttempts(String key) {
		int attempts = attemptsCache.getOrDefault(key, 0);
		attempts++;
		attemptsCache.put(key, attempts);
		System.out.println("Current attempts for " + key + ": " + attempts);
		if (attempts >= MAX_ATTEMPTS) {
			lockTimeCache.put(key, System.currentTimeMillis() + LOCK_TIME_DURATION);
		}
	}

	public void clearAttempts(String key) {
		attemptsCache.remove(key);
		lockTimeCache.remove(key);
	}

	public int getAttempts(String key) {
		if (isLocked(key)) {
			return MAX_ATTEMPTS;
		}
		return attemptsCache.getOrDefault(key, 0);
	}

	public boolean isLocked(String key) {
		if (lockTimeCache.containsKey(key)) {
			long lockEndTime = lockTimeCache.get(key);
			if (System.currentTimeMillis() > lockEndTime) {
				lockTimeCache.remove(key);
				return false;
			}
			return true;
		}
		return false;
	}
}
