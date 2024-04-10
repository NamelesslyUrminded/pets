package com.example.pets.repository;

import com.example.pets.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long>, PagingAndSortingRepository<Client, Long> {

    @Query("SELECT c FROM Client c JOIN FETCH c.pet JOIN FETCH c.owner o WHERE o.id = :id")
    List<Client> findByIdOwner(@Param("id") Long id);

    @Query("SELECT c FROM Client c JOIN FETCH c.pet JOIN FETCH c.owner o")
    List<Client> findAll();

    @Query("DELETE FROM Client c WHERE c.owner.id IN :idList")
    @Modifying
    void deleteByListId(@Param("idList") List<Long> idList);

    @Query("SELECT c FROM Client c JOIN FETCH c.pet p JOIN FETCH c.owner o ")
    Page<Client> findAllByFilter(Specification<Client> clientFilter, Pageable pageable);

}
