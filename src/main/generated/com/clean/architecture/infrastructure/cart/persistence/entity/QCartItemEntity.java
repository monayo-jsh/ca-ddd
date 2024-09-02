package com.clean.architecture.infrastructure.cart.persistence.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartItemEntity is a Querydsl query type for CartItemEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartItemEntity extends EntityPathBase<CartItemEntity> {

    private static final long serialVersionUID = -1901411290L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartItemEntity cartItemEntity = new QCartItemEntity("cartItemEntity");

    public final QCartEntity cart;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QCartItemEntity(String variable) {
        this(CartItemEntity.class, forVariable(variable), INITS);
    }

    public QCartItemEntity(Path<? extends CartItemEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartItemEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartItemEntity(PathMetadata metadata, PathInits inits) {
        this(CartItemEntity.class, metadata, inits);
    }

    public QCartItemEntity(Class<? extends CartItemEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCartEntity(forProperty("cart"), inits.get("cart")) : null;
    }

}

