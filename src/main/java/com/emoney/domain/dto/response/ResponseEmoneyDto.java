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
public class ResponseEmoneyDto {

    private Long emoneyDetailSeq;
    private Long userSeq;
    private Long productSeq;
    private Long typeSeq;
    private Long amount;
    private String content;
    private LocalDateTime creationDateTime;
    private LocalDateTime expirationDateTime;
}
