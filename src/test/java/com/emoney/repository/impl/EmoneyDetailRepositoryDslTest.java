package com.emoney.repository.impl;

import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.dto.response.ResponseEmoneyDetailDto;
import com.emoney.domain.entity.Emoney;
import com.emoney.domain.entity.EmoneyDetail;
import com.emoney.domain.mapper.EmoneyDetailMapper;
import com.emoney.repository.EmoneyDetailRepository;
import com.emoney.repository.EmoneyRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmoneyDetailRepositoryDslTest {

    @Autowired
    private EmoneyRepository emoneyRepository;

    @Autowired
    private EmoneyDetailRepository emoneyDetailRepository;

    @Autowired
    private EmoneyDetailMapper emoneyDetailMapper;

    private Long userSeq = 2L;

    @BeforeEach
    void conditionalSetup(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("findAllUsableEmoneyList_사용_가능_적립금_조회()") ||
            testInfo.getDisplayName().equals("findEmoneyBalancePaging_적립금_잔액_조회_페이징_조회()")) {
            setupUsableEmoneyData();
        }
    }

    private void setupUsableEmoneyData() {
        Long expectAmount = 1500L;

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
    }

    @Test
    void findAllUsableEmoneyList_사용_가능_적립금_조회(){
        // Given
        Long expectAmount = 1500L;

        RequestEmoneyDeductDto dto = RequestEmoneyDeductDto.builder()
            .userSeq(userSeq)
            .build();

        // When
        List<InfoEmoneyDetailDto> list = emoneyDetailRepository.findAllUsableEmoneyList(dto).stream()
            .filter(item -> item.getAmount() > 0)
            .toList();

        Long totalUsableAmount = list.stream()
            .mapToLong(InfoEmoneyDetailDto::getAmount).sum();

        // Then
        assertEquals(expectAmount, totalUsableAmount);
    }

    @Test
    void findEmoneyBalancePaging_적립금_잔액_조회_페이징_조회(){
        // Given
        List<Long> expectAmonutList = List.of(0L, 1500L);
        LocalDateTime localDateTime = LocalDateTime.of(2025, 4, 10, 12, 30);

        RequestEmoneySearchDto emoneySearchDto = RequestEmoneySearchDto.builder()
            .userSeq(userSeq)
            .build();
        emoneySearchDto.setPageNumber(1);
        emoneySearchDto.setPageSize(10);

        // When
        Page<InfoEmoneyDetailDto> pageInfo = emoneyDetailRepository.findEmoneyBalancePaging(emoneySearchDto);

        List<ResponseEmoneyDetailDto> list = pageInfo.get().toList().stream()
            .map(item -> {
                ResponseEmoneyDetailDto dto = emoneyDetailMapper.toDto(item);
                boolean flag = localDateTime.compareTo(item.getExpirationDateTime()) < 0;   // true: 사용 가능, false: 사용 불가능
                dto.setExpirationFlag(flag);

                return dto;
            })
            .toList();

        List<Long> amountList = list.stream()
            .map(item -> item.getAmount())
            .toList();

        // Then
        assertIterableEquals(expectAmonutList, amountList);
    }
}
