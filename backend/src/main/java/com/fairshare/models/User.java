package com.fairshare.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;


//    one user can have many expenses
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Expense> expenses;
}
