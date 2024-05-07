package com.clothz.aistyling.domain.user;

import com.clothz.aistyling.domain.BaseEntity;
import com.clothz.aistyling.domain.user.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "member")
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, name = "email")
    private String email;

    @Column(length = 15, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(name = "user_images")
    private String userImages;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    public String saveImages(final String images) {
        this.userImages = images;
        return this.userImages;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateNickname(String nickname){
        this.nickname=nickname;
    }

    @Builder
    public User(final String email, final String nickname, final String password, final String userImages, final UserRole userRole) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.userImages = userImages;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
