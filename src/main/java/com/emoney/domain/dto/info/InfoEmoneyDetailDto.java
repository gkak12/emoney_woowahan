package com.emoney.domain.dto.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoEmoneyDetailDto {

    private Long accumulationSeq;
    private Long amount;
    private LocalDateTime expirationDateTime;
}
