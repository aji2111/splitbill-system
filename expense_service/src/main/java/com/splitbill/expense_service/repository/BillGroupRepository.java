package com.splitbill.expense_service.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.splitbill.expense_service.entity.BillGroup;



@Repository
public interface BillGroupRepository extends JpaRepository<BillGroup, Long> {

    boolean existsByPublicId(UUID publicId);
    
    Optional<BillGroup> findByPublicId(UUID publicId);
}
