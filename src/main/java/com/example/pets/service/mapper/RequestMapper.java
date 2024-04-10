package com.example.pets.service.mapper;

import com.example.pets.dto.request.Request;

public interface RequestMapper<T, R extends Request> {
    T toEntity(T t, R r);
}
