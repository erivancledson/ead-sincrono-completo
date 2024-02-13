package com.ead.course.dtos;

import com.ead.course.enums.USerStatus;
import com.ead.course.enums.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private USerStatus userStatus;
    private UserType userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;


}
