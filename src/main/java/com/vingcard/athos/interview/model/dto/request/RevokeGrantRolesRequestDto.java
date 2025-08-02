package com.vingcard.athos.interview.model.dto.request;

import com.vingcard.athos.interview.persistence.entity.auth.Role;

import java.util.List;

public record RevokeGrantRolesRequestDto(List<Role> roles) {
}
