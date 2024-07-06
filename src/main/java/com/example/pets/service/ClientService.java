package com.example.pets.service;

import com.example.pets.dto.filter.ClientFilter;
import com.example.pets.dto.filter.FilterArgument;
import com.example.pets.dto.request.ClientRequest;
import com.example.pets.dto.request.PetRequest;
import com.example.pets.dto.response.*;
import com.example.pets.dto.response.clientFillerResponse.ClientFillerResponseMain;
import com.example.pets.dto.response.clientFillerResponse.ClientResultsResponse;
import com.example.pets.entity.Client;
import com.example.pets.entity.Owner;
import com.example.pets.entity.Pet;
import com.example.pets.exception.EntityModifyException;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    public Owner pendingOwner = new Owner();
    List<PetRequest> pendingPets = new ArrayList<>();


    private final ClientRepository clientRepository;
    private final OwnerService ownerService;
    private final PetService petService;
    private final ResponseMapper<Client, ClientResponse> clientResponseMapper;
    private final ResponseMapper<Owner, OwnerResponse> ownerResponseMapper;
    private final ResponseMapper<Pet, PetResponse> petResponseMapper;

    @Transactional
    public List<Client> create(ClientRequest clientRequest) throws EntityModifyException {
        if (CollectionUtils.isEmpty(clientRequest.getPets())) {
            throw new EntityModifyException("Pets must not be empty");
        }
        List<Client> afterSaveClientList = new ArrayList<>();
        Owner pendingOwner = ownerService.update(clientRequest.getOwner()); // owner edit
        clientRequest.getPets().forEach((petRequest) -> {
            afterSaveClientList.add(clientForEveryPet(petRequest, pendingOwner));
        });
        return clientRepository.saveAll(afterSaveClientList);
    }

    public Client clientForEveryPet(PetRequest petRequest, Owner owner){
        Pet pet = petService.update(petRequest);
        return new Client(pet, owner);
    }

    public List<ClientResponse> toResponseList(List<Client> clients) {
        return clients.stream()
                .collect(Collectors.groupingBy(Client::getOwner,
                        Collectors.mapping(Client::getPet, Collectors.toList())))
                .entrySet()
                .stream()
                .map(t -> {
                    OwnerResponse ownerResponse = ownerResponseMapper.toResponse(t.getKey());
                    List<PetResponse> petResponseList = t.getValue().stream().map(petResponseMapper::toResponse).collect(Collectors.toList());
                    return new ClientResponse(ownerResponse, petResponseList);
                })
                .collect(Collectors.toList());
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

    public List<Client> findAll() {
        return clientRepository.findAll();
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
        return new PageImpl<>(clientResponseList, PageRequest.of(pageClient.getNumber(), pageClient.getSize()), pageClient.getTotalElements());
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

    public void fillClients(){
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<ClientResultsResponse> response = restTemplate
        .exchange("https://randomuser.me/api/?results={results}",
        HttpMethod.GET,
        null,
        ClientResultsResponse.class,
        "100"
      );
      ClientResultsResponse clientResultsResponse = response.getBody();
      if (Objects.nonNull(clientResultsResponse)) {
        List<ClientFillerResponseMain> results = clientResultsResponse.getResults();
        results.forEach(t -> System.out.println(t.toString()));
      }
    }
}
