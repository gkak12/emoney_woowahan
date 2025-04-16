package com.emoney.service;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySaveDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.dto.response.ResponseEmoneyListDto;

public interface EmoneyService {

    void saveEmoney(RequestEmoneySaveDto emoneySaveDto);
    void deductEmoney(RequestEmoneyDeductDto emoneyDeductDto);
    ResponseEmoneyListDto findEmoneyPaging(RequestEmoneySearchDto emoneySearchDto);
}
