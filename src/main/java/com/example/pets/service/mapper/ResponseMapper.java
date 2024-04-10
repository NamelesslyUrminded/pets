package com.example.pets.service.mapper;

import com.example.pets.dto.response.Response;

@FunctionalInterface
public interface ResponseMapper<T, R extends Response> {
   R toResponse(T t);
}
