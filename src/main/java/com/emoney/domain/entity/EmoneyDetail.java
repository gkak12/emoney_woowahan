package com.emoney.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "EMONEY_DETAIL", schema = "emoney_woowahan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmoneyDetail {

    @Id
    @Column(name = "EMONEY_DETAIL_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emoneyDetailSeq;

    @Column(name = "USER_SEQ")
    private Long userSeq;

    @Column(name = "TYPE_SEQ")
    private Long typeSeq;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "CREATION_DATE_TIME")
    private LocalDateTime creationDateTime;
}
