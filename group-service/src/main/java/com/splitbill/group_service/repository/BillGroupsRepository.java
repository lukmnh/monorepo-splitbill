package com.splitbill.group_service.repository;

import com.splitbill.group_service.entity.BillGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillGroupsRepository extends JpaRepository<BillGroups, Long> {
}
