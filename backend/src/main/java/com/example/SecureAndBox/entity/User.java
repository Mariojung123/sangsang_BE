package com.example.SecureAndBox.entity;

import com.example.SecureAndBox.etc.Role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity //엔티티 정의
@Table(name="user_info") //사용하지 않으면 클래스 이름이 테이블 이름이 됨
@Getter //lombok getter
@Setter //lombok setter
public class User {
    @Id //기본키를 의미. 반드시 기본키를 가져야함.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, length=10) //유일하고 최대 길이가 10.
    private String user_id;

    @Column(length = 15)
    private String user_pwd;

    @Column()
    private Role role;
}
