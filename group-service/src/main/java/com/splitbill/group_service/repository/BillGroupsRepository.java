package com.splitbill.group_service.repository;

import com.splitbill.group_service.entity.BillGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillGroupsRepository extends JpaRepository<BillGroups, Long> {
    @Query("SELECT b FROM BillGroups b WHERE " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<BillGroups> searchGroups(@Param("search") String search);
    Optional<BillGroups> findById(Long id);
}
