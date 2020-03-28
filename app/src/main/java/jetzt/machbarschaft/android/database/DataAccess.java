package jetzt.machbarschaft.android.database;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jetzt.machbarschaft.android.database.callback.CollectionLoadedCallback;
import jetzt.machbarschaft.android.database.callback.CollectionsLoadedCallback;
import jetzt.machbarschaft.android.database.callback.WasSuccessfullCallback;
import jetzt.machbarschaft.android.database.entitie.Account;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.database.entitie.OrderAccount;

/**
 * Handles the access to with the database.
 */
public class DataAccess extends Database {
    private static DataAccess dataAccess;

    private DataAccess() {
    }

    public static DataAccess getInstance() {
        if (dataAccess == null) {
            dataAccess = new DataAccess();
        }
        return dataAccess;
    }

    public void createAccount(Account account) {
        createAccount(account, null);
    }

    public void createAccount(Account account, WasSuccessfullCallback callback) {
        super.addDocument(CollectionName.Account, account, callback);
    }

    public void login(String phone_number, CollectionLoadedCallback callback) {

        super.getOneDocumentByCondition(CollectionName.Account, new AbstractMap.SimpleEntry<>("phone_number", phone_number),
                document -> {
                    Storage.getInstance().setUserID(document.getId());
                    if (callback != null) {
                        Account account = new Account(document);
                        callback.onOrderLoaded(account);
                    }
                });
    }

    @Deprecated
    public void getMyOrder(String phone_number) {
        LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
        conditions.put("phone_number", phone_number);
        conditions.put("status", "confirmed");
        super.getOneDocumentByTwoConditions(CollectionName.Order_Account, conditions,
                document -> super.getDocumentById(CollectionName.Order, document.getId(),
                        document2 -> Storage.getInstance().setCurrentOrder(new Order(document2))));
    }
    @Deprecated
    public void getMyOrder(String phone_number, CollectionLoadedCallback callback) {


        super.getOneDocumentByCondition(CollectionName.Account, new AbstractMap.SimpleEntry<>("phone_number", phone_number),
                document -> {
                    Account account = new Account(document);
                    LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
                    conditions.put("account_id", account.getId());
                    conditions.put("status", "Confirmed");
                    super.getOneDocumentByTwoConditions(CollectionName.Order_Account, conditions,
                            document2 -> {
                                if (document2.get("order_id") != null) {
                                    this.getOrderById((String) document2.get("order_id"), callback);
                                } else {
                                    Log.i("DataAccess", "getMyOrder() : this account has no order confirmed");
                                }
                            });
                });
    }

    public void getMyOrders(String phone_number, CollectionsLoadedCallback callback) {

        super.getOneDocumentByCondition(CollectionName.Account, new AbstractMap.SimpleEntry<>("phone_number", phone_number),
                document -> {
                    Account account = new Account(document);
                    LinkedHashMap<String, Object> conditions = new LinkedHashMap<>();
                    conditions.put("account_id", (Object) account.getId());
                    conditions.put("status", "confirmed");
                    super.getDocumentsByTwoConditions(CollectionName.Order_Account, conditions,
                            documentList -> {
                                ArrayList<Collection> orders = new ArrayList<>();
                                for(DocumentSnapshot d: documentList) {
                                    if(d.get("order_id") != null) {
                                        orders.add(new Order(d));
                                    }
                                }
                                if(!orders.isEmpty()) {
                                    callback.onOrdersLoaded(orders);
                                } else {
                                    callback.onOrdersLoaded(null);

                                }
                            });
                });
    }

    public void getOrderById(String orderId, CollectionLoadedCallback callback) {

        super.getDocumentById(CollectionName.Order, orderId, document -> {
            if (document != null) {
                Order order = new Order(document);
                callback.onOrderLoaded(order);
            } else {
                Log.w("DataAccess", "getOrderById() : document was not found");
                callback.onOrderLoaded(null);
            }
        });
    }

    public void getOrders() {
        getOrders(null);
    }

    public void getOrders(WasSuccessfullCallback callback) {
        super.getCollection(CollectionName.Order, documents -> {
            OrderHandler.getInstance().addCollection(documents);
            //TODO:
            OrderHandler.getInstance().setUserPosition(OrderHandler.Type.Besteller, 50.555809, 9.680845);
            if (callback != null) {
                if (documents != null) {
                    callback.wasSuccessful(true);
                } else {
                    callback.wasSuccessful(false);
                }
            }
        });
    }

    public void setOrderStatus(String orderId, Order.Status status) {
        if (status == Order.Status.CONFIRMED) {
            // update Status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()));

            //  Adds Entry in Account order
            OrderAccount orderAccount = new OrderAccount();
            orderAccount.setStatus(status.getName());
            orderAccount.setAccountId(Storage.getInstance().getUserID());
            orderAccount.setOrderId(orderId);
            super.addDocument(CollectionName.Order_Account, orderAccount);
        } else if (status == Order.Status.CLOSED) {
            // update status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()));

            // update Status in Order_Account
            super.updateDocument(CollectionName.Order_Account, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()));
        }
    }

    public void setOrderStatus(String orderId, Order.Status status, WasSuccessfullCallback callback) {
        if (status == Order.Status.CONFIRMED) {
            // update Status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()), successful -> {
                if (successful) {
                    //  Adds Entry in Account order
                    OrderAccount orderAccount = new OrderAccount();
                    orderAccount.setStatus(status.getName());
                    orderAccount.setAccountId(Storage.getInstance().getUserID());
                    orderAccount.setOrderId(orderId);
                    super.addDocument(CollectionName.Order_Account, orderAccount, innerSuccessful -> {
                        if (innerSuccessful) {
                            callback.wasSuccessful(true);
                        } else {
                            callback.wasSuccessful(false);
                        }
                    });
                } else {
                    callback.wasSuccessful(false);
                }
            });
        } else if (status == Order.Status.CLOSED) {
            // update status in Collection Order
            super.updateDocument(CollectionName.Order, orderId, new AbstractMap.SimpleEntry<>("status", status.getName()), successful -> {
                if (successful) {

                    super.getOneDocumentByCondition(CollectionName.Order_Account, new AbstractMap.SimpleEntry<>("order_id", orderId),
                            document -> {
                                document.getId();
                                // update Status in Order_Account
                                super.updateDocument(CollectionName.Order_Account, document.getId(), new AbstractMap.SimpleEntry<>("status", status.getName()), innerSuccessful -> {
                                    if (innerSuccessful) {
                                        callback.wasSuccessful(true);
                                    } else {
                                        callback.wasSuccessful(false);
                                    }
                                });
                            });
                } else {
                    callback.wasSuccessful(false);
                }
            });


        }
    }

}
