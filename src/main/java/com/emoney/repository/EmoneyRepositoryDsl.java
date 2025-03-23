package com.emoney.repository;

import com.emoney.domain.dto.request.RequestEmoneyDeductDto;
import com.emoney.domain.dto.request.RequestEmoneySearchDto;
import com.emoney.domain.entity.Emoney;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmoneyRepositoryDsl {

    Page<Emoney> findEmoneyPaging(RequestEmoneySearchDto searchDto);
    List<Emoney> findAllUsableEmoneyList(RequestEmoneyDeductDto requestEmoneyDeductDto);
}
