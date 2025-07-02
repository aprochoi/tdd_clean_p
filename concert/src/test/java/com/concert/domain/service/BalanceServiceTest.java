package com.concert.domain.service;

import com.concert.domain.model.User;
import com.concert.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    @DisplayName("사용자 잔액 충전 성공")
    void chargeBalance_succeeds() {
        //given
        long userId = 1L;
        long initBalance = 10000;
        long chargeAmount = 5000;
        User user = new User(userId, "최영민", initBalance);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        //when
        User updatedUser = balanceService.chargeBalance(userId, chargeAmount);

        // then
        assertThat(updatedUser.getBalance()).isEqualTo(initBalance + chargeAmount);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("0원 이하의 금액으로 충전을 시도하면 예외가 발생")
    void chargeBalance_withInvalidAmount_shouldThrowException() {
        // given
        long userId = 1L;
        long chargeAmount = -100; // 잘못된 금액
        User user = new User(userId, "최영민", 10000);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when & then (User.charge() 내부 로직이 예외를 발생시킴)
        assertThrows(IllegalArgumentException.class, () -> {
            balanceService.chargeBalance(userId, chargeAmount);
        });
    }

    @Test
    @DisplayName("사용자의 현재 잔액 조회 성공")
    void getBalance_succeeds() {
        // given
        long userId = 1L;
        long currentBalance = 15000;
        User user = new User(userId, "최영민", currentBalance);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        User retrievedUser = balanceService.getBalance(userId);

        // then
        assertThat(retrievedUser.getId()).isEqualTo(userId);
        assertThat(retrievedUser.getBalance()).isEqualTo(currentBalance);
    }
}