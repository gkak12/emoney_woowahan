package com.emoney.service;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;

public interface EmoneyService {

    void deductEmoney(RequestEmoneyDeductDto emoneyDeductDto);
}
