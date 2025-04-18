package com.emoney.repository;

import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.entity.EmoneyDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmoneyDetailRepositoryDsl {

    List<InfoEmoneyDetailDto> findAllUsableEmoneyList(RequestEmoneyDeductDto emoneyDeductDto);
    Page<EmoneyDetail> findEmoneyDetailPaging(RequestEmoneySearchDto emoneySearchDto);
    Page<InfoEmoneyDetailDto> findEmoneyBalancePaging(RequestEmoneySearchDto emoneySearchDto);
}
