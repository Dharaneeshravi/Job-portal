package com.dharaneesh.job_portal_backend.dto;

public record LoginResponseDto(String message,UserDto userDto,String token) {
}
