package jetzt.machbarschaft.android.database.entitie;

import com.google.firebase.Timestamp;

/**
 * Give information about which account accepted which order and at which timestamp.
 * The timestamp will help to determine, if a person, doesn't complete a accepted order,
 * so we can give it to another helper.
 */
public class OrderAccount extends Collection {
    private String accountId;
    private String orderId;
    private String status;
    private Timestamp createdTimestamp;

    public OrderAccount() {
    }

    public OrderAccount(String accountId, String orderId, String status, Timestamp createdTimestamp) {
        this.accountId = accountId;
        this.orderId = orderId;
        this.status = status;
        this.createdTimestamp = createdTimestamp;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String account_id) {
        this.accountId = account_id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String order_id) {
        this.orderId = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp created) {
        this.createdTimestamp = created;
    }
}
