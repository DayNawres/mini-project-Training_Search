package com.wedevzone.digiparc.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmplacementMapperTest {

    private EmplacementMapper emplacementMapper;

    @BeforeEach
    public void setUp() {
        emplacementMapper = new EmplacementMapperImpl();
    }
}
