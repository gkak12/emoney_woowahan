package com.emoney.service;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySaveDto;

public interface EmoneyService {

    void saveEmoney(RequestEmoneySaveDto emoneySaveDto);
    void deductEmoney(RequestEmoneyDeductDto emoneyDeductDto);
}
