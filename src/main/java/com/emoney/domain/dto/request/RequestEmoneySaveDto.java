package com.emoney.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmoneySaveDto {

    @NotNull
    private Long userSeq;

    @NotNull
    private Long productSeq;

    @NotNull
    private Long amount;

    private String content;
}
