package com.emoney.repository;

import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.entity.EmoneyDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmoneyDetailRepositoryDsl {

    Page<EmoneyDetail> findEmoneyDetailPaging(RequestEmoneySearchDto emoneySearchDto);
    List<InfoEmoneyDetailDto> findAllUsableEmoneyList(RequestEmoneyDeductDto emoneyDeductDto);
}
