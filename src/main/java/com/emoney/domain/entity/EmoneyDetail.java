package com.emoney.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "ACCUMULATION_SEQ")
    private Long accumulationSeq;

    @Column(name = "TYPE_SEQ")
    private Long typeSeq;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "CREATION_DATE_TIME")
    private LocalDateTime creationDateTime;

    @Column(name = "EXPIRATION_DATE_TIME")
    private LocalDateTime expirationDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMONEY_SEQ")
    @JsonBackReference
    private Emoney emoney;
}
