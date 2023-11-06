package com.main.mainserver.model.user;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",updatable = false)
    private Long id;
    private String email;
    private String password;
    private String username;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

}
