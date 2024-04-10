package com.example.pets.service;

import com.example.pets.dto.filter.FilterArgument;
import com.example.pets.dto.filter.OwnerFilter;
import com.example.pets.dto.request.OwnerRequest;
import com.example.pets.dto.response.OwnerResponse;
import com.example.pets.entity.Owner;
import com.example.pets.repository.OwnerRepository;
import com.example.pets.service.mapper.RequestMapper;
import com.example.pets.service.mapper.ResponseMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final RequestMapper<Owner, OwnerRequest> ownerRequestMapper;
    private final ResponseMapper<Owner, OwnerResponse> ownerResponseMapper;

    public Owner update(OwnerRequest ownerRequest) {
        Objects.requireNonNull(ownerRequest, "Owner request is null");
        Owner entity = Objects.isNull(ownerRequest.getId()) ? new Owner() : findById(ownerRequest.getId());
        entity = ownerRequestMapper.toEntity(entity, ownerRequest);
        return ownerRepository.save(entity);
    }

    public boolean delete(Long id) {
        Objects.requireNonNull(id, "Owner id is null");
        this.ownerRepository.deleteById(id);
        return true;
    }

    public Owner findById(Long id) {
        Objects.requireNonNull(id, "Owner id is null");
        return this.ownerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Owner by id %s not found", id)));
    }

    public List<Owner> findAll() {
        return this.ownerRepository.findAll();
    }

    public Page<OwnerResponse> findWithFilter(OwnerFilter ownerFilter, Pageable pageable){
        Specification<Owner> ownerSpecification =
                Specification.where(nameFilter(ownerFilter))
                        .and(surnameFilter(ownerFilter));
        Page<Owner> ownerPage = this.ownerRepository.findAll(ownerSpecification, pageable);
        List<OwnerResponse> ownerResponseList = ownerPage.getContent()
                .stream()
                .map(ownerResponseMapper::toResponse)
                .collect(Collectors.toList());
        Page<OwnerResponse> responsePage = new PageImpl<>(ownerResponseList,
                PageRequest.of(ownerPage.getNumber(), ownerPage.getSize()), ownerPage.getTotalElements());
        return responsePage;
    }

    public Specification<Owner> nameFilter(OwnerFilter ownerFilter){
        return (iRoot, query, cb) -> {
            if (StringUtils.isNotBlank(ownerFilter.getName())) {
                if (Objects.equals(ownerFilter.getNameArg(), FilterArgument.CONTAINS)) {
                    return cb.like(cb.lower(iRoot.get("name")), "%" + ownerFilter.getName().toLowerCase() + "%");
                } else if (Objects.equals(ownerFilter.getNameArg(), FilterArgument.EQUALS)) {
                    return cb.equal(cb.lower(iRoot.get("name")), ownerFilter.getName().toLowerCase());
                }
            }
            return null;
        };
    }

    public Specification<Owner> surnameFilter(OwnerFilter ownerFilter){
        return (iRoot, query, cb) -> {
            if (StringUtils.isNotBlank(ownerFilter.getSurname())) {
                if (Objects.equals(ownerFilter.getSurnameArg(), FilterArgument.CONTAINS)) {
                    return cb.like(cb.lower(iRoot.get("secondName")), "%" + ownerFilter.getSurname().toLowerCase() + "%");
                } else if (Objects.equals(ownerFilter.getSurnameArg(), FilterArgument.EQUALS)) {
                    return cb.equal(cb.lower(iRoot.get("secondName")), ownerFilter.getSurname().toLowerCase());
                }
            }
            return null;
        };
    }

    @Transactional
    public void deleteSeveral(List<Long> idList) {
        this.ownerRepository.deleteAllById(idList);
    }
}
