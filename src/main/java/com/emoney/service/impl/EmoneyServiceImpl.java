package com.emoney.service.impl;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.response.ResponseEmoneyDetailDto;
import com.emoney.repository.EmoneyDetailRepository;
import com.emoney.repository.EmoneyRepository;
import com.emoney.service.EmoneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmoneyServiceImpl implements EmoneyService {

    private final EmoneyRepository emoneyRepository;
    private final EmoneyDetailRepository emoneyDetailRepository;

    @Override
    public void deductEmoney(RequestEmoneyDeductDto emoneyDeductDto) {
        // 1. 사용 가능 적립금 조회
        List<ResponseEmoneyDetailDto> list = emoneyDetailRepository.findAllUsableEmoneyList(emoneyDeductDto);
        Long requestAmount = emoneyDeductDto.getAmount();
        Long totalUsableAmount = list.stream().map(ResponseEmoneyDetailDto::getAmount).reduce(0L, Long::sum);

        // 2. 사용 가능 적립금이 사용 요청한 적립금 비교해서 작으면 예외 발생
        if(totalUsableAmount < requestAmount) {
            throw new RuntimeException("현재 사용 가능한 적립금이 사용 요청한 적립금 보다 적습니다.");
        }

        // 3. 각 적립금 사용 및 차감
        for(ResponseEmoneyDetailDto emoneyDetailDto : list) {
            /**
             * 사용 요청한 적립금이 현재 적립금 보다 크면, 현재 적립금 0원 처리 및 사용 요청한 적립금 차감 처리
             * 반대로 작거나 작으면, 현재 적립금에서 사용 요청한 적립금 차감 및 사용 요청한 적립금 0원 처리
             */
            Long amount = emoneyDetailDto.getAmount();

            if(requestAmount > amount) {
                amount *= -1;
                requestAmount -= amount;
            } else {
                amount -= requestAmount;
                requestAmount = 0l;
            }

            emoneyDetailRepository.save(null);
        }

        emoneyRepository.save(null);
    }
}
