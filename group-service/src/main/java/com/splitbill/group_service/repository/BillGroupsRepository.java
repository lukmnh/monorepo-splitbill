package com.splitbill.group_service.repository;

import com.splitbill.group_service.entity.BillGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillGroupsRepository extends JpaRepository<BillGroups, Long> {
    @Query(value = """
        SELECT * FROM bill_groups b 
        WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) 
           OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%'))
        """,
            countQuery = """
        SELECT COUNT(*) FROM bill_groups b 
        WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) 
           OR LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%'))
        """,
            nativeQuery = true)
    Page<BillGroups> searchGroups(@Param("search") String search, Pageable pageable);
    Optional<BillGroups> findById(Long id);
}
