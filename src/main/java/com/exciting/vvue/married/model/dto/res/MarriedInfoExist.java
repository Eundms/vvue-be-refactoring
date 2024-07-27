package com.exciting.vvue.married.model.dto.res;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MarriedInfoExist {
    private boolean marriedInfoExists;

    public MarriedInfoExist(boolean marriedInfoExists) {
        this.marriedInfoExists = marriedInfoExists;
    }
}
