package com.emoney.repository.impl;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.response.ResponseEmoneyDetailDto;
import com.emoney.repository.EmoneyDetailRepositoryDsl;
import com.emoney.util.ConditionBuilderUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.emoney.domain.entity.QEmoney.emoney;
import static com.emoney.domain.entity.QEmoneyDetail.emoneyDetail;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmoneyDetailRepositoryDslImpl implements EmoneyDetailRepositoryDsl {

    private final JPAQueryFactory jpaQueryFactory;

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
}
