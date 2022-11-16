package com.example.itshop.entities;

import com.example.itshop.enums.VoucherConstraintUserType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "voucher_constraint_user")
@Getter
@Setter
public class VoucherConstraintUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "voucher_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Voucher voucher;
    
    @Column(name = "constraint_type")
    @Enumerated(EnumType.STRING)
    private VoucherConstraintUserType constraintType;
    
}