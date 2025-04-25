package com.userApi.mapper;

import com.userApi.dto.PhoneDTO;
import com.userApi.entity.Phone;
import com.userApi.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class PhoneMapper {

    public static Phone toEntity(PhoneDTO phoneDTO, User user) {
        Phone phone = new Phone();
        phone.setNumber(phoneDTO.getNumber());
        phone.setCitycode(phoneDTO.getCitycode());
        phone.setContrycode(phoneDTO.getContrycode());
        phone.setUser(user);
        return phone;
    }

    public static PhoneDTO toDTO(Phone phone) {
        PhoneDTO dto = new PhoneDTO();
        dto.setNumber(phone.getNumber());
        dto.setCitycode(phone.getCitycode());
        dto.setContrycode(phone.getContrycode());
        return dto;
    }

    public static PhoneDTO toPhoneDTO(Phone phone) {
        if (phone == null) {
            return null;
        }
        return new PhoneDTO(phone.getNumber(), phone.getCitycode(), phone.getContrycode());
    }

    public static List<PhoneDTO> toPhoneDTOList(List<Phone> phones) {
        if (phones == null) {
            return null;
        }
        return phones.stream().map(PhoneMapper::toPhoneDTO).collect(Collectors.toList());
    }
}