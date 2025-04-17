package com.emoney.repository.impl;

import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.entity.EmoneyDetail;
import com.emoney.enums.EmoneySearchEnums;
import com.emoney.repository.EmoneyDetailRepositoryDsl;
import com.emoney.util.ConditionBuilderUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.emoney.domain.entity.QEmoneyDetail.emoneyDetail;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmoneyDetailRepositoryDslImpl implements EmoneyDetailRepositoryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<EmoneyDetail> findEmoneyDetailPaging(RequestEmoneySearchDto emoneySearchDto) {
        Pageable pageable = PageRequest.of(
            emoneySearchDto.getPageNumber(),
            emoneySearchDto.getPageSize()
        );

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(ConditionBuilderUtil.buildEquals(emoneyDetail.emoneyDetailSeq, emoneySearchDto.getEmoneyDetailSeq()))
            .and(ConditionBuilderUtil.buildEquals(emoneyDetail.userSeq, emoneySearchDto.getUserSeq()))
            .and(ConditionBuilderUtil.buildEquals(emoneyDetail.typeSeq, emoneySearchDto.getTypeSeq()));

        String amountType = emoneySearchDto.getSearchAmountType();
        Long startAmount = emoneySearchDto.getSearchStartAmountVal();
        Long endAmount = emoneySearchDto.getSearchEndAmountVal();

        if(EmoneySearchEnums.AMOUNT.getVal().equals(amountType)){
            builder.and(ConditionBuilderUtil.buildAmountBetween(emoneyDetail.amount, startAmount, endAmount));
        }

        String dateType = emoneySearchDto.getSearchDateType();
        LocalDate startDate = emoneySearchDto.getSearchStartDate();
        LocalDate endDate = emoneySearchDto.getSearchEndDate();

        if(EmoneySearchEnums.EXPIRATION_DATE.getVal().equals(dateType)){
            builder.and(ConditionBuilderUtil.buildDateBetween(emoneyDetail.expirationDateTime, startDate, endDate));
        } else if(EmoneySearchEnums.CREATION_DATE.getVal().equals(dateType)){
            builder.and(ConditionBuilderUtil.buildDateBetween(emoneyDetail.creationDateTime, startDate, endDate));
        }

        List<EmoneyDetail> list = jpaQueryFactory
            .select(emoneyDetail)
            .from(emoneyDetail)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = Optional
            .ofNullable(
                jpaQueryFactory
                    .select(emoneyDetail.count())
                    .from(emoneyDetail)
                    .where(builder)
                    .fetchOne())
            .orElse(0L);

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<InfoEmoneyDetailDto> findAllUsableEmoneyList(RequestEmoneyDeductDto requestEmoneyDeductDto) {
        Long userSeq = Objects.requireNonNull(requestEmoneyDeductDto.getUserSeq(), "userSeq is null.");
        LocalDateTime now = LocalDateTime.now();

        BooleanBuilder builder = new BooleanBuilder();

        return jpaQueryFactory
            .select(Projections.fields(
                InfoEmoneyDetailDto.class,
                emoneyDetail.accumulationSeq,
                emoneyDetail.amount.sum().as("amount"),
                emoneyDetail.expirationDateTime.max().as("expirationDateTime")
            ))
            .from(emoneyDetail)
            .where(
                builder
                    .and(ConditionBuilderUtil.buildEquals(emoneyDetail.userSeq, userSeq))
                    .and(ConditionBuilderUtil.buildDateTimeBetween(emoneyDetail.expirationDateTime, now, null))
            )
            .groupBy(emoneyDetail.accumulationSeq)
            .orderBy(emoneyDetail.accumulationSeq.asc())
            .fetch();
    }
}
