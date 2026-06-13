package com.server.app.entities;

import com.server.app.entities.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false,length = 10)
    private String currency;

    @Column(nullable = false)
    private Double baseBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}