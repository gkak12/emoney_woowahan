package com.emoney.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "EMONEY", schema = "emoney_woowahan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emoney {

    @Id
    @Column(name = "EMONEY_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emoneySeq;

    @Column(name = "USER_SEQ")
    private Long userSeq;

    @Column(name = "PRODUCT_SEQ")
    private Long productSeq;

    @Column(name = "TYPE_SEQ")
    private Long typeSeq;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATION_DATE_TIME")
    private LocalDateTime creationDateTime;

    @Column(name = "EXPIRATION_DATE_TIME")
    private LocalDateTime expirationDateTime;

    @OneToMany(mappedBy = "emoney", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EmoneyDetail> emoneyDetails;
}
