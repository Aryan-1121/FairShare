package com.fairshare.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

//need to keep track of all transactions by every person(since there will be multiple expense for single expense)
    private Double transactionAmount ;


/*
   Many transaction may occur from 1 expense
   Many payers will pay for 1 expense
   there might be many payee(receivers ) for 1 expense

  this above strategy will help to keep track of each expense where what amount of transaction occurred i.e. who paid whom and how much

sum of all transaction under same expenseId must be equal to expenseAmount

 */
    @ManyToOne
    private Expense expense;


//    Many Transactions will be done by one user
    @ManyToOne
    private User user;




}
