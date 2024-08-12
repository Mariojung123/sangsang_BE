package com.example.SecureAndBox.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id //기본키를 의미. 반드시 기본키를 가져야함.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    @Column()
    private Role role;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String serialId;

    @Column(nullable = true)
    private String refreshToken;

    private String username;
    private String pw;
    private String email;



    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public enum Role {
        USER,
        ADMIN
    }
}
