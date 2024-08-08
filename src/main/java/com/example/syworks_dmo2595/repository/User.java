package com.example.syworks_dmo2595.repository;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

}
