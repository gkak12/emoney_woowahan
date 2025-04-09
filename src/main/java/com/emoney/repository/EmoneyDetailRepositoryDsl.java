package com.emoney.repository;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.response.ResponseEmoneyDetailDto;

import java.util.List;

public interface EmoneyDetailRepositoryDsl {

    List<ResponseEmoneyDetailDto> findAllUsableEmoneyList(RequestEmoneyDeductDto requestEmoneyDeductDto);
}
