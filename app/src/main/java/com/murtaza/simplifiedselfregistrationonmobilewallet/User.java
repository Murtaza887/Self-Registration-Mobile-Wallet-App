package com.murtaza.simplifiedselfregistrationonmobilewallet;

public class User {
    private String number, amount;

    public User() {}

    public User(String number, String amount) {
        this.number = number;
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
