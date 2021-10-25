/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

public class StoppageList {

    int ExpenseId;
    String placeName;
    int amount;

    public StoppageList(){

    }
    public StoppageList(int expenseId, String expenseName, int amount) {
        ExpenseId = expenseId;
        placeName = expenseName;
        this.amount = amount;
    }


    public StoppageList(int expenseId, String expenseName) {
        ExpenseId = expenseId;
        placeName = expenseName;
    }



    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getExpenseId() {
        return ExpenseId;
    }

    public void setExpenseId(int expenseId) {
        ExpenseId = expenseId;
    }

    public String getExpenseName() {
        return placeName;
    }

    public void setExpenseName(String expenseName) {
        placeName = expenseName;
    }
}
