package com.emoney.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String creationDateTime;
    private String expirationDateTime;
}
