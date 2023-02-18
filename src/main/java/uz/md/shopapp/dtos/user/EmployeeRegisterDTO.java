package uz.md.shopapp.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.RegEx;
import javax.annotation.meta.When;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeRegisterDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Phone Number cannot be blank")
    @RegEx(when = When.ALWAYS)
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Role cannot be null")
    private Integer roleId;

}
