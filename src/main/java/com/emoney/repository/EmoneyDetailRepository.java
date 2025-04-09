package com.emoney.repository;

import com.emoney.domain.entity.EmoneyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmoneyDetailRepository extends JpaRepository<EmoneyDetail, Long>, EmoneyDetailRepositoryDsl {
}
