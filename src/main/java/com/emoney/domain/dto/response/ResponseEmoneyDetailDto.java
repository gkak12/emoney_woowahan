package com.emoney.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime creationDateTime;
    private LocalDateTime expirationDateTime;
}
