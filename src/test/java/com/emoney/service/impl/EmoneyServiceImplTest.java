package com.emoney.service.impl;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.response.ResponseEmoneyDetailDto;
import com.emoney.domain.entity.EmoneyDetail;
import com.emoney.repository.EmoneyDetailRepository;
import com.emoney.repository.EmoneyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmoneyServiceImplTest {

    @Mock
    private EmoneyRepository emoneyRepository;

    @Mock
    private EmoneyDetailRepository emoneyDetailRepository;

    @Test
    void deductEmoney_사용_가능_적립금_조회_테스트(){
        // Given
        Long expectationAmount = 1500L;
        int expectationSize = 1;

        ResponseEmoneyDetailDto emoneyDetail1 = ResponseEmoneyDetailDto.builder()
            .accumulationSeq(1L)
            .amount(0L)
            .expirationDateTime(LocalDateTime.of(2025, 5, 12, 10, 30))
            .build();

        ResponseEmoneyDetailDto emoneyDetail2 = ResponseEmoneyDetailDto.builder()
            .accumulationSeq(2L)
            .amount(1500L)
            .expirationDateTime(LocalDateTime.of(2025, 5, 14, 10, 30))
            .build();

        RequestEmoneyDeductDto emoneyDeductDto = RequestEmoneyDeductDto.builder()
            .amount(expectationAmount)
            .build();

        List<ResponseEmoneyDetailDto> emoneyList = List.of(emoneyDetail1, emoneyDetail2);

        when(emoneyDetailRepository.findAllUsableEmoneyList(emoneyDeductDto)).thenReturn(emoneyList);

        // When
        List<ResponseEmoneyDetailDto> list = emoneyDetailRepository.findAllUsableEmoneyList(emoneyDeductDto).stream()
            .filter(item -> item.getAmount() > 0)
            .toList();

        Long totalUsableAmount = list.stream()
            .map(ResponseEmoneyDetailDto::getAmount)
            .reduce(0L, Long::sum);

        // Then
        assertEquals(expectationAmount, totalUsableAmount);
        assertEquals(expectationSize, list.size());
    }

    @Test
    void deductEmoney_적립금_차감_테스트(){
        // Given
        Long expectationAmount = 500L;
        Long requestAmount = 2500L;

        ResponseEmoneyDetailDto emoneyDetail1 = ResponseEmoneyDetailDto.builder()
                .accumulationSeq(1L)
                .amount(1000L)
                .expirationDateTime(LocalDateTime.of(2025, 5, 12, 10, 30))
                .build();

        ResponseEmoneyDetailDto emoneyDetail2 = ResponseEmoneyDetailDto.builder()
                .accumulationSeq(2L)
                .amount(2000L)
                .expirationDateTime(LocalDateTime.of(2025, 5, 14, 10, 30))
                .build();

        RequestEmoneyDeductDto emoneyDeductDto = RequestEmoneyDeductDto.builder()
                .amount(expectationAmount)
                .build();

        List<ResponseEmoneyDetailDto> emoneyList = List.of(emoneyDetail1, emoneyDetail2);
        List<ResponseEmoneyDetailDto> dataList = new ArrayList<>(List.of(emoneyDetail1, emoneyDetail2));

        when(emoneyDetailRepository.findAllUsableEmoneyList(emoneyDeductDto)).thenReturn(emoneyList);

        // When
        List<ResponseEmoneyDetailDto> list = emoneyDetailRepository.findAllUsableEmoneyList(emoneyDeductDto);
        for(ResponseEmoneyDetailDto emoneyDetailDto : list){
            Long amount = emoneyDetailDto.getAmount();

            if(requestAmount > amount){
                requestAmount = requestAmount - amount;
            } else {
                amount = requestAmount;
                requestAmount = 0L;
            }

            dataList.add(
                ResponseEmoneyDetailDto.builder()
                    .userSeq(emoneyDeductDto.getUserSeq())
                    .accumulationSeq(emoneyDetailDto.getAccumulationSeq())
                    .typeSeq(emoneyDeductDto.getTypeSeq())
                    .amount(-amount)
                    .expirationDateTime(emoneyDetailDto.getExpirationDateTime())
                    .build()
            );
        }

        // Then
        Long actualAmount = dataList.stream().map(ResponseEmoneyDetailDto::getAmount).reduce(0L, Long::sum);
        assertEquals(expectationAmount, actualAmount);
    }
}
