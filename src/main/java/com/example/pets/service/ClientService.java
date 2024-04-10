package com.example.pets.service;

import com.example.pets.dto.filter.ClientFilter;
import com.example.pets.dto.filter.FilterArgument;
import com.example.pets.dto.request.ClientRequest;
import com.example.pets.dto.response.ClientResponse;
import com.example.pets.dto.response.OwnerResponse;
import com.example.pets.dto.response.PetResponse;
import com.example.pets.entity.Client;
import com.example.pets.entity.Owner;
import com.example.pets.entity.Pet;
import com.example.pets.repository.ClientRepository;
import com.example.pets.service.mapper.ResponseMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final OwnerService ownerService;
    private final PetService petService;
    private final ResponseMapper<Client, ClientResponse> clientResponseMapper;
    private final ResponseMapper<Owner, OwnerResponse> ownerResponseMapper;
    private final ResponseMapper<Pet, PetResponse> petResponseMapper;

    public ClientResponse create(ClientRequest clientRequest) {
        Owner owner = ownerService.update(clientRequest.getOwner());
        List<Client> afterSaveClientList = new ArrayList<>();
        clientRequest.getPets().forEach((petRequest) -> {
            Pet pet = petService.update(petRequest);
            Client client = new Client(pet, owner);
            if (Objects.isNull(petRequest.getId())) {
                client = clientRepository.save(client);
            }
            afterSaveClientList.add(client);
        });
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setPets(new ArrayList<>());
        afterSaveClientList.stream().map(clientResponseMapper::toResponse).forEach((item) -> {
            if (clientResponse.getOwner() == null)
                clientResponse.setOwner(item.getOwner());
            clientResponse.getPets().addAll(item.getPets());
        });
        return clientResponse;
    }

    public ClientResponse findById(Long id) throws EntityNotFoundException {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setPets(new ArrayList<>());
        clientRepository.findByIdOwner(id).stream().map(clientResponseMapper::toResponse).forEach((item) -> {
            if (clientResponse.getOwner() == null)
                clientResponse.setOwner(item.getOwner());
            clientResponse.getPets().addAll(item.getPets());
        });
        return clientResponse;
    }

    public List<ClientResponse> findAll() {
        Map<Owner, List<Pet>> clientData = clientRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy((t) -> t.getOwner(), Collectors.mapping(t -> t.getPet(), Collectors.toList())));
        return clientData
                .entrySet()
                .stream()
                .map(t -> {
                    OwnerResponse ownerResponse = ownerResponseMapper.toResponse(t.getKey());
                    List<PetResponse> petResponseList = t.getValue().stream().map(petResponseMapper::toResponse).collect(Collectors.toList());
                    return new ClientResponse(ownerResponse, petResponseList);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSeveral(List<Long> idList) {
        this.clientRepository.deleteByListId(idList);
    }

    public Page<ClientResponse> findAllByFilter(ClientFilter clientFilter, Pageable pageable) {
        Page<Client> pageClient = this.clientRepository.findAllByFilter(clientFilter(clientFilter), pageable);
        List<ClientResponse> clientResponseList = pageClient.getContent()
                .stream()
                .map(clientResponseMapper::toResponse)
                .collect(Collectors.toList());
        Page<ClientResponse> responsePage = new PageImpl<>(clientResponseList, PageRequest.of(pageClient.getNumber(), pageClient.getSize()), pageClient.getTotalElements());
        return responsePage;
    }

    public Specification<Client> clientFilter(ClientFilter clientFilter) {
        return (iRoot, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(clientFilter.getName())) {
                if (Objects.equals(clientFilter.getNameArg(), FilterArgument.CONTAINS)) {
                    predicates.add(cb.like(cb.lower(iRoot.get("owner").get("name")), "%" + clientFilter.getName().toLowerCase() + "%"));
                } else if (Objects.equals(clientFilter.getNameArg(), FilterArgument.EQUALS)) {
                    predicates.add(cb.equal(cb.lower(iRoot.get("owner").get("name")), clientFilter.getName().toLowerCase()));
                }
            }
            if (StringUtils.isNotBlank(clientFilter.getSurname())) {
                if (Objects.equals(clientFilter.getSurnameArg(), FilterArgument.CONTAINS)) {
                    predicates.add(cb.like(cb.lower(iRoot.get("owner").get("surname")), "%" + clientFilter.getSurname().toLowerCase() + "%"));
                } else if (Objects.equals(clientFilter.getSurnameArg(), FilterArgument.EQUALS)) {
                    predicates.add(cb.equal(cb.lower(iRoot.get("owner").get("surname")), clientFilter.getSurname().toLowerCase()));
                }
            }
            query.where(predicates.toArray(new Predicate[]{}));
            return null;
        };
    }

}
