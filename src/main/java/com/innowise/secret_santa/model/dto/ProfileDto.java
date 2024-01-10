package com.innowise.secret_santa.model.dto;

import com.innowise.secret_santa.constants_message.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {

    @NotBlank(message = Constants.NOT_NULL_FIELD)
    private String name;

    private AddressDto address;
}
