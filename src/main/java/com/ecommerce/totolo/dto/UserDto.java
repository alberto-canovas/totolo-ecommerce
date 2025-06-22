package com.ecommerce.totolo.dto;

import com.ecommerce.totolo.Enum.TypeEnum;
import com.ecommerce.totolo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String address;
    private String phone_number;
    private TypeEnum type;

    // ✅ Constructor personalizado para construir el DTO desde un User
    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phone_number = user.getPhone_number();
        this.type = user.getType();
    }
}
