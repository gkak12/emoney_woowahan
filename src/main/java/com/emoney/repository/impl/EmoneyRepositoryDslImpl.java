package com.emoney.repository.impl;

import com.emoney.domain.dto.request.RequestEmoneyCancelDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.dto.response.ResponseEmoneyDetailDto;
import com.emoney.domain.entity.Emoney;
import com.emoney.domain.entity.EmoneyDetail;
import com.emoney.enums.EmoneySearchEnums;
import com.emoney.repository.EmoneyRepositoryDsl;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.emoney.domain.entity.QEmoney.emoney;
import static com.emoney.domain.entity.QEmoneyDetail.emoneyDetail;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmoneyRepositoryDslImpl implements EmoneyRepositoryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Emoney> findEmoneyPaging(RequestEmoneySearchDto searchDto) {
        Pageable pageable = PageRequest.of(
                searchDto.getPageNumber(),
                searchDto.getPageSize()
        );

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(ConditionBuilderUtil.buildEquals(emoney.emoneySeq, searchDto.getEmoneySeq()))
            .and(ConditionBuilderUtil.buildEquals(emoney.userSeq, searchDto.getUserSeq()))
            .and(ConditionBuilderUtil.buildEquals(emoney.productSeq, searchDto.getProductSeq()))
            .and(ConditionBuilderUtil.buildEquals(emoney.typeSeq, searchDto.getTypeSeq()))
            .and(ConditionBuilderUtil.buildStringLike(emoney.content, searchDto.getContent()));

        String amountType = searchDto.getSearchAmountType();
        Long startAmount = searchDto.getSearchStartAmountVal();
        Long endAmount = searchDto.getSearchEndAmountVal();

        if(EmoneySearchEnums.AMOUNT.getVal().equals(amountType)){
            builder.and(ConditionBuilderUtil.buildAmountBetween(emoney.amount, startAmount, endAmount));
        }

        String dateType = searchDto.getSearchDateType();
        LocalDate startDate = searchDto.getSearchStartDate();
        LocalDate endDate = searchDto.getSearchEndDate();

        if(EmoneySearchEnums.EXPIRATION_DATE.getVal().equals(dateType)){
            builder.and(ConditionBuilderUtil.buildDateBetween(emoney.expirationDateTime, startDate, endDate));
        } else if(EmoneySearchEnums.CREATION_DATE.getVal().equals(dateType)){
            builder.and(ConditionBuilderUtil.buildDateBetween(emoney.creationDateTime, startDate, endDate));
        }

        List<Emoney> list = jpaQueryFactory
            .select(emoney)
            .from(emoney)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = Optional.ofNullable(
            jpaQueryFactory
                .select(emoney.count().as("count"))
                .from(emoney)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<ResponseEmoneyDetailDto> findAllUsableEmoneyList(RequestEmoneyDeductDto requestEmoneyDeductDto) {
        Long userSeq = Objects.requireNonNull(requestEmoneyDeductDto.getUserSeq(), "userSeq is null.");
        LocalDateTime now = LocalDateTime.now();

        BooleanBuilder builder = new BooleanBuilder();

        return jpaQueryFactory
            .select(Projections.fields(
                ResponseEmoneyDetailDto.class,
                emoneyDetail.accumulationSeq,
                emoneyDetail.amount.sum()
            ))
            .from(emoneyDetail)
            .where(
                builder
                    .and(ConditionBuilderUtil.buildEquals(emoney.userSeq, userSeq))
                    .and(ConditionBuilderUtil.buildDateTimeBetween(emoney.expirationDateTime, null, now))
                    .and(emoneyDetail.amount.sum().gt(0l))
            )
            .groupBy(emoneyDetail.accumulationSeq)
            .orderBy(emoneyDetail.creationDateTime.asc())
            .fetch();
    }

    @Override
    public Map<String, Object> findCancellationEmoney(RequestEmoneyCancelDto requestEmoneyCancelDto) {
        return Map.of();
    }
}
