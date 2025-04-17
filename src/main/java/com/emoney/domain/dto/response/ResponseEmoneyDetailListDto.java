package com.emoney.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEmoneyDetailListDto {

    private List<ResponseEmoneyDetailDto> list;
    private ResponsePageDto page;
}
