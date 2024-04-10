package com.example.pets.dto.filter;

public enum FilterArgument {

    EQUALS(1),
    CONTAINS(2);

    private final Integer argNum;

    FilterArgument(Integer argNum) {
        this.argNum = argNum;
    }

}
