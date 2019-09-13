package com.ng.oauth2.demo.security.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {

	//NOTE: Make sure the role is having a prefix of 'ROLE_' 
	//if 'Role' based access restrictions are to be used (e.g.: hasRole(), @RolesAllowed(), etc.). 
	//If 'ROLE_' is not prefixed, 'Authority' based access restrictions would be in place (e.g.: hasAuthority()) 
    public enum Role {ROLE_USER, ROLE_ADMIN, ROLE_USER_MANAGER}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    //@Email
    private String email;
    @JsonIgnore
    @ToString.Exclude
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
	
}
