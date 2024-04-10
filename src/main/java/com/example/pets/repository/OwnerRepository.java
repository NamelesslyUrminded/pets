package com.example.pets.repository;

import com.example.pets.entity.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long>, PagingAndSortingRepository<Owner, Long>, JpaSpecificationExecutor {
    @Query("DELETE FROM Owner o WHERE o.id IN :idList")
    @Modifying
    void deleteByListId(@Param("idList") List<Long> idList);
}
