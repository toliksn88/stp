package com.innowise.secret_santa.model.dto;

import com.innowise.secret_santa.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
