package com.emoney.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEmoneyDetailDto {

    private Long emoneyDetailSeq;
    private Long userSeq;
    private Long accumulationSeq;
    private Long typeSeq;
    private Long amount;
    private String creationDateTime;
    private String expirationDateTime;
    private Long emoneySeq;
}
