package com.digsol.main.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private Long id;

    @NotNull
    private String address;

    @Override
    public String toString() {
        return address;
    }
}
