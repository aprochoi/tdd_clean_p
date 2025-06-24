package com.concert.domain.service;

import com.concert.domain.model.Reservation;
import com.concert.domain.model.Seat;
import com.concert.domain.model.User;
import com.concert.domain.repository.ReservationRepository;
import com.concert.domain.repository.SeatRepository;
import com.concert.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SeatReservationService {

    private final TokenValidator tokenValidator;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 좌석을 5분간 임시 배정한다.
     * @param tokenValue 토큰 정보
     * @param userId userid
     * @param seatId 좌석 id
     * @return 예약 정보
     */
    @Transactional
    public Reservation reserveSeat(String tokenValue, Long userId, Long seatId) {
        // 1. 토큰이 유효한지 검증
        tokenValidator.validate(tokenValue);

        // 2. 사용자 정보 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. 좌석 정보 조회 (Lock은 impl에서 적용함)
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));

        // 4. 좌석 예약 처리 (도메인 객체에서 진행)
        seat.reserve();

        // 5. 예약 정보 생성
        Reservation newReservation = Reservation.builder()
                .user(user)
                .seat(seat)
                .status(Reservation.ReservationStatus.PENDING_PAYMENT)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        // 6. 변경된 좌석 상태와 예약 정보를 DB에 저장
        seatRepository.save(seat);
        return reservationRepository.save(newReservation);
    }
}
