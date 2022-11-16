package com.example.itshop.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
@NoArgsConstructor
public class ProductVariant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Long stock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductAttribute> productAttributes = new LinkedHashSet<>();
    
    
    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariantImage> productVariantImages = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "productVariant")
    private Set<Cart> carts = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "productVariant")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();
    
    public ProductVariant(Long id) {
         this.id = id;
    }
}