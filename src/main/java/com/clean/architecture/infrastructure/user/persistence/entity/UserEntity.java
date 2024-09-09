package com.clean.architecture.infrastructure.user.persistence.entity;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
@Entity
@Table(
    name = "tb_user",
    indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true)
    }
)
@Comment("사용자 테이블")
public class UserEntity extends BaseEntity {

    @Comment("사용자 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("사용자 이름")
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    @Comment("사용자 패스워드(암호화)")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Comment("사용자 이메일")
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Comment("사용자 휴대전화번호")
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Comment("사용자 상태")
    @Column(name = "status", nullable = false, length = 20)
    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Comment("사용자 상태 변경일시")
    @Column(name = "status_changed_at", nullable = false)
    private LocalDateTime statusChangedAt;

    @Builder
    private UserEntity(Long id, String username, String password, String email, String phoneNumber, UserStatus status, LocalDateTime statusChangedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.statusChangedAt = statusChangedAt;
    }
}
