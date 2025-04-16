package com.emoney.repository;

import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;

import java.util.List;

public interface EmoneyDetailRepositoryDsl {

    List<InfoEmoneyDetailDto> findAllUsableEmoneyList(RequestEmoneyDeductDto requestEmoneyDeductDto);
}
