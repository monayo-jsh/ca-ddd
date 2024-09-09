package com.clean.architecture.infrastructure.common.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter @NoArgsConstructor
@Embeddable
public class AddressEntity {

    @Comment("우편번호")
    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipCode;

    @Comment("도로명 주소")
    @Column(name = "road_address", nullable = false, length = 500)
    private String roadAddress;

    @Comment("지번 주소 (선택적)")
    @Column(name = "jibeon_address", nullable = true, length = 500)
    private String jibeonAddress;

    @Comment("상세 주소")
    @Column(name = "detail_address", nullable = false, length = 500)
    private String detailAddress;

    @Comment("추가 설명 (선택적)")
    @Column(name = "extra_address", nullable = true, length = 500)
    private String extraAddress;

    @Builder
    private AddressEntity(String zipCode, String roadAddress, String jibeonAddress, String detailAddress, String extraAddress) {
        this.zipCode = zipCode;
        this.roadAddress = roadAddress;
        this.jibeonAddress = jibeonAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }

}
