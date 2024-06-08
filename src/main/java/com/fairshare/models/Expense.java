package com.fairshare.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Expense {


    private Long expenseId;
    private String expenseDescription;
    private Double expenseAmount;

//    one Expense can have multiple transactions
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;



//    Multiple expense can have 1 user
    @ManyToOne
    private User user;












}
