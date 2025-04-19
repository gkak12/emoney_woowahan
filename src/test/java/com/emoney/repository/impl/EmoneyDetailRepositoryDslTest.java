package com.emoney.repository.impl;

import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.entity.Emoney;
import com.emoney.domain.entity.EmoneyDetail;
import com.emoney.repository.EmoneyDetailRepository;
import com.emoney.repository.EmoneyRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmoneyDetailRepositoryDslTest {

    @Autowired
    private EmoneyRepository emoneyRepository;

    @Autowired
    private EmoneyDetailRepository emoneyDetailRepository;

    @Test
    void findAllUsableEmoneyList_사용_가능_적립금_조회(){
        // Given
        Long expectAmount = 1500L;
        
        Long userSeq = 2L;
        LocalDateTime creationDateTime = LocalDateTime.of(2025, 4, 10, 12, 30);
        LocalDateTime expirationDateTime = LocalDateTime.of(2025, 5, 10, 12, 30);

        Emoney emoney1 = Emoney.builder()
            .userSeq(userSeq)
            .productSeq(1L)
            .typeSeq(1L)
            .amount(1000L)
            .creationDateTime(creationDateTime)
            .expirationDateTime(expirationDateTime)
            .build();

        emoneyRepository.save(emoney1);

        EmoneyDetail emoneyDetail1 = EmoneyDetail.builder()
            .accumulationSeq(1L)
            .userSeq(userSeq)
            .typeSeq(1L)
            .amount(1000L)
            .creationDateTime(creationDateTime)
            .expirationDateTime(expirationDateTime)
            .emoneySeq(1L)
            .emoney(emoney1)
            .build();

        emoneyDetailRepository.save(emoneyDetail1);

        Emoney emoney2 = Emoney.builder()
            .userSeq(userSeq)
            .productSeq(2L)
            .typeSeq(1L)
            .amount(2000L)
            .creationDateTime(creationDateTime)
            .expirationDateTime(expirationDateTime)
            .build();

        emoneyRepository.save(emoney2);

        EmoneyDetail emoneyDetail2 = EmoneyDetail.builder()
            .accumulationSeq(2L)
            .userSeq(userSeq)
            .typeSeq(1L)
            .amount(2000L)
            .creationDateTime(creationDateTime)
            .expirationDateTime(expirationDateTime)
            .emoneySeq(2L)
            .emoney(emoney2)
            .build();

        emoneyDetailRepository.save(emoneyDetail2);

        Emoney emoney3 = Emoney.builder()
            .userSeq(userSeq)
            .productSeq(2L)
            .typeSeq(2L)
            .amount(-1500L)
            .creationDateTime(creationDateTime)
            .expirationDateTime(expirationDateTime)
            .build();

        emoneyRepository.save(emoney3);

        EmoneyDetail emoneyDetail3 = EmoneyDetail.builder()
            .accumulationSeq(1L)
            .userSeq(userSeq)
            .typeSeq(2L)
            .amount(-1000L)
            .creationDateTime(creationDateTime.plusDays(1))
            .expirationDateTime(expirationDateTime)
            .emoneySeq(3L)
            .emoney(emoney3)
            .build();

        emoneyDetailRepository.save(emoneyDetail3);

        EmoneyDetail emoneyDetail4 = EmoneyDetail.builder()
            .accumulationSeq(2L)
            .userSeq(userSeq)
            .typeSeq(2L)
            .amount(-500L)
            .creationDateTime(creationDateTime.plusDays(1))
            .expirationDateTime(expirationDateTime)
            .emoneySeq(3L)
            .emoney(emoney3)
            .build();

        emoneyDetailRepository.save(emoneyDetail4);

        // When
        RequestEmoneyDeductDto dto = RequestEmoneyDeductDto.builder()
                .userSeq(userSeq)
                .build();

        List<InfoEmoneyDetailDto> list = emoneyDetailRepository.findAllUsableEmoneyList(dto).stream()
            .filter(item -> item.getAmount() > 0)
            .toList();

        Long totalUsableAmount = list.stream()
                .map(InfoEmoneyDetailDto::getAmount)
                .reduce(0L, Long::sum);

        // Then
        assertEquals(expectAmount, totalUsableAmount);
    }
}
