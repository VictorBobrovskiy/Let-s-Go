package com.digsol.main.location;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddressMapper {

    public static AddressDto toAddressDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getAddress()
        );
    }

    public static Address toAddress(AddressDto address) {
        return new Address(
                address.getId(),
                address.getAddress()
        );
    }
}
