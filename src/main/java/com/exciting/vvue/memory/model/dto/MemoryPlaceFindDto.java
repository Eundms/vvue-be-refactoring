package com.exciting.vvue.memory.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class MemoryPlaceFindDto {
    private BigDecimal x;
    private BigDecimal y;
}
