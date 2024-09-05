package com.clean.architecture.infrastructure.product.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.clean.architecture.infrastructure.common.persistence.entity.BaseEntity;
import com.clean.architecture.infrastructure.common.persistence.entity.CommonStatus;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(
    name = "tb_category",
    indexes = {
        @Index(name = "idx_category_name", columnList = "name", unique = true)
    }
)
@Comment("카테고리 테이블")
public class CategoryEntity extends BaseEntity {

    @Comment("카테고리 고유키")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("카테고리 이름")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Comment("상위 카테고리 참조(자기참조)")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_category_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CategoryEntity parentCategory;

    @Comment("카테고리 상태")
    @Column(name = "status", nullable = false, length = 20)
    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private CommonStatus status;

}
