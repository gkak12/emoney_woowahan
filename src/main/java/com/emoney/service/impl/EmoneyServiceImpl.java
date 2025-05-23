package com.emoney.service.impl;

import com.emoney.comm.enums.EmoneyEnums;
import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySaveDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.dto.response.*;
import com.emoney.domain.entity.Emoney;
import com.emoney.domain.entity.EmoneyDetail;
import com.emoney.domain.mapper.EmoneyDetailMapper;
import com.emoney.domain.mapper.EmoneyMapper;
import com.emoney.repository.EmoneyDetailRepository;
import com.emoney.repository.EmoneyRepository;
import com.emoney.service.EmoneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmoneyServiceImpl implements EmoneyService {

    private final EmoneyMapper emoneyMapper;
    private final EmoneyDetailMapper emoneyDetailMapper;

    private final EmoneyRepository emoneyRepository;
    private final EmoneyDetailRepository emoneyDetailRepository;

    @Override
    public void saveEmoney(RequestEmoneySaveDto emoneySaveDto) {
        // 1. 적립금 적립시간/만료시간 설정
        LocalDateTime craationDateTime = LocalDateTime.now();
        LocalDateTime expirationDateTime = craationDateTime.plusDays(30);

        // 2. RequestEmoneySaveDto -> Emoney Entity로 변환 및 저장
        Emoney emoney = emoneyMapper.toEntity(emoneySaveDto);
        emoney.setTypeSeq(EmoneyEnums.SAVE.getVal());
        emoney.setCreationDateTime(craationDateTime);
        emoney.setExpirationDateTime(expirationDateTime);

        emoneyRepository.save(emoney);

        // 3. EmoneyDetail Entity 생성 및 저장
        EmoneyDetail emoneyDetail = EmoneyDetail.builder()
            .userSeq(emoneySaveDto.getUserSeq())
            .accumulationSeq(emoney.getEmoneySeq())
            .typeSeq(EmoneyEnums.SAVE.getVal())
            .amount(emoneySaveDto.getAmount())
            .creationDateTime(craationDateTime)
            .expirationDateTime(expirationDateTime)
            .emoney(emoney)
            .build();

        emoneyDetailRepository.save(emoneyDetail);
    }

    @Override
    public void deductEmoney(RequestEmoneyDeductDto emoneyDeductDto) {
        // 1. 사용 가능 적립금 조회
        List<InfoEmoneyDetailDto> list = emoneyDetailRepository.findAllUsableEmoneyList(emoneyDeductDto).stream()
            .filter(item -> item.getAmount() > 0)
            .toList();

        Long totalUsableAmount = list.stream()
            .mapToLong(InfoEmoneyDetailDto::getAmount).sum();

        Long requestAmount = emoneyDeductDto.getAmount();

        // 2. 사용 가능 적립금이 사용 요청한 적립금 비교해서 작으면 예외 발생
        if(totalUsableAmount < requestAmount) {
            throw new RuntimeException("현재 사용 가능한 적립금이 사용 요청한 적립금 보다 적습니다.");
        }

        // 3. 적립금 사용 내역 추가
        LocalDateTime localDateTime = LocalDateTime.now();

        Emoney emoney = Emoney.builder()
                .userSeq(emoneyDeductDto.getUserSeq())
                .productSeq(emoneyDeductDto.getProductSeq())
                .typeSeq(emoneyDeductDto.getTypeSeq())
                .amount(-requestAmount)
                .content(emoneyDeductDto.getContent())
                .creationDateTime(localDateTime)
                .build();

        emoneyRepository.save(emoney);

        // 4. 각 적립금 사용 및 차감 추가
        for(InfoEmoneyDetailDto emoneyDetailDto : list) {
            /**
             * 사용 요청한 적립금이 현재 적립금 보다 크면, 현재 적립금 0원 처리 및 사용 요청한 적립금 차감 처리
             * 반대로 작거나 작으면, 현재 적립금에서 사용 요청한 적립금 차감 및 사용 요청한 적립금 0원 처리
             */
            Long amount = emoneyDetailDto.getAmount();

            if(requestAmount > amount){
                requestAmount = requestAmount - amount;
            } else {
                amount = requestAmount;
                requestAmount = 0L;
            }

            emoneyDetailRepository.save(
                EmoneyDetail.builder()
                    .userSeq(emoneyDeductDto.getUserSeq())
                    .accumulationSeq(emoneyDetailDto.getAccumulationSeq())
                    .typeSeq(emoneyDeductDto.getTypeSeq())
                    .amount(-amount)
                    .creationDateTime(localDateTime)
                    .expirationDateTime(emoneyDetailDto.getExpirationDateTime())
                    .emoney(emoney)
                    .build()
            );
        }
    }

    @Override
    public ResponseEmoneyListDto findEmoneyPaging(RequestEmoneySearchDto emoneySearchDto) {
        Page<Emoney> pageInfo = emoneyRepository.findEmoneyPaging(emoneySearchDto);

        List<ResponseEmoneyDto> list = pageInfo.get().toList().stream()
            .map(emoneyMapper::toDto)
            .toList();

        ResponsePageDto page = ResponsePageDto.builder()
            .totalPages(pageInfo.getTotalPages())
            .totalItems(pageInfo.getTotalElements())
            .build();

        return ResponseEmoneyListDto.builder()
                .list(list)
                .page(page)
                .build();
    }

    @Override
    public ResponseEmoneyDetailListDto findEmoneyDetailPaging(RequestEmoneySearchDto emoneySearchDto) {
        Page<EmoneyDetail> pageInfo = emoneyDetailRepository.findEmoneyDetailPaging(emoneySearchDto);

        List<ResponseEmoneyDetailDto> list = pageInfo.get().toList().stream()
            .map(emoneyDetailMapper::toDto)
            .toList();

        ResponsePageDto page = ResponsePageDto.builder()
            .totalPages(pageInfo.getTotalPages())
            .totalItems(pageInfo.getTotalElements())
            .build();

        return ResponseEmoneyDetailListDto.builder()
            .list(list)
            .page(page)
            .build();
    }

    @Override
    public ResponseEmoneyDetailListDto findEmoneyDetailBalancePaging(RequestEmoneySearchDto emoneySearchDto) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        Page<InfoEmoneyDetailDto> pageInfo = emoneyDetailRepository.findEmoneyBalancePaging(emoneySearchDto);

        List<ResponseEmoneyDetailDto> list = pageInfo.get().toList().stream()
            .map(item -> {
                ResponseEmoneyDetailDto dto = emoneyDetailMapper.toDto(item);
                boolean flag = localDateTime.compareTo(item.getExpirationDateTime()) < 0;   // true: 사용 가능, false: 사용 불가능
                dto.setExpirationFlag(flag);

                return dto;
            })
            .toList();

        ResponsePageDto page = ResponsePageDto.builder()
            .totalPages(pageInfo.getTotalPages())
            .totalItems(pageInfo.getTotalElements())
            .build();

        return ResponseEmoneyDetailListDto.builder()
                .list(list)
                .page(page)
                .build();
    }
}
