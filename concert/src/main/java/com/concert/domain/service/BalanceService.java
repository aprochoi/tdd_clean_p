package com.concert.domain.service;

import com.concert.domain.model.User;
import com.concert.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final UserRepository userRepository;

    /**
     * 유저 잔액 충전
     * @param userId 유저 ID
     * @param amount 충전할 금액
     * @return 충전 후 잔액 정보
     */
    @Transactional
    public User chargeBalance(long userId, long amount) {
        // 1.유저 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2.잔액 충전 (도메인 객체에서 진행)
        user.charge(amount);

        // 3.변경된 유저 정보 저장 후 반환
        return  userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getBalance(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void useBalance(long userId, long amount) {
        // 1.유저 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 잔액 사용 (도메인 객체에서 진행)
        user.use(amount);

        userRepository.save(user);
    }
}
