package com.example.itshop.entities;

import com.example.itshop.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column
    private String name;

    @Fetch(FetchMode.JOIN)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserRecovery userRecovery;

    @Fetch(FetchMode.JOIN)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserVerification userVerification;

    @OneToMany(mappedBy = "user")
    private Set<Cart> carts = new LinkedHashSet<>();
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avatar")
    @Fetch(FetchMode.JOIN)
    private File avatar;
	
	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private Set<UserVoucher> userVouchers = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Order> orders = new LinkedHashSet<>();
	
	public User(Long id) {
        this.id = id;
    }
}
