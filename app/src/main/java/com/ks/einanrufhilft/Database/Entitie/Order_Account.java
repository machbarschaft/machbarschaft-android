package com.ks.einanrufhilft.Database.Entitie;

import com.google.firebase.Timestamp;

public class Order_Account {

    private String account_id;
    private String order_id;
    private String status;
    private Timestamp created_timestamp;

    public Order_Account() {}

    public Order_Account(String account_id, String order_id, String status, Timestamp created_timestamp) {
    this.account_id = account_id;
    this.order_id = order_id;
    this.status = status;
    this.created_timestamp = created_timestamp;
    }


    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(Timestamp created_timestamp) {
        this.created_timestamp = created_timestamp;
    }
}
