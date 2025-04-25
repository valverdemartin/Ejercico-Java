package com.userApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhoneDTO {
    private Long number;
    private Integer citycode;
    private String contrycode;

}
