package jetzt.machbarschaft.android.database.test;

import android.util.Log;

import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.entitie.Account;
import jetzt.machbarschaft.android.database.entitie.Order;

public class DataAccessTest {
    private static DataAccessTest test;

    private DataAccessTest() {
    }

    ;

    public static DataAccessTest getInstance() {
        if (test == null) {
            test = new DataAccessTest();
        }
        return test;
    }

    public void runTests() {
        Log.i("DataAccessTest", "Tests start:");

        //createAccount(); //passed
        login(); // passed
        // getOrders(); // passed
        // setOrderStatus(); // passed
        // getOrderById(); // passed
        // getMyOrder(); // passed
        getMyOrders();
    }

    private void createAccount() {
        Account a = new Account();
        a.setFirst_name("TestFirstName");
        a.setLast_name("TestLastName");
        a.setRadius(10f);
        a.setPhone_number("0157 12345678");
        a.setCredits(10);
        DataAccess.getInstance().createAccount(a, success -> {
            if (success == true) {
                Log.i("DataAccessTest", "createAccount: Success");
                return;
            } else {
                Log.i("DataAccessTest", "createAccount: Failure");
            }
        });
    }

    private void login() {
        DataAccess.getInstance().login("0157 12345678", collection -> {
            if (collection != null) {
                Log.i("DataAccessTest", "login: Success");
                Log.i("DataAccessTest", "login: " + collection);
            } else {
                Log.i("DataAccessTest", "login: Failure");
            }
        });
    }

    private void getOrderById() {
        DataAccess.getInstance().getOrderById("3Bh8isxyUHQ1Gny3G0Nn", order -> {
            if (order != null) {
                Log.i("DataAccessTest", "getOrderById: Success");
                Log.i("DataAccessTest", order.toString());

            } else {
                Log.i("DataAccessTest", "getOrderById: Failure");
            }
        });
    }

    private void getMyOrder() {
        Log.i("DataAccessTest", "getMyOrder: started");

        DataAccess.getInstance().getMyOrder("0157 12345678", order -> {
            if (order != null) {
                Log.i("DataAccessTest", "getMyOrder: Success");
                Log.i("DataAccessTest", order.toString());

            } else {
                Log.i("DataAccessTest", "getMyOrder: Failure");
            }
        });
    }

    private void getMyOrders() {
        Log.i("DataAccessTest", "getMyOrders: started");

        DataAccess.getInstance().getMyOrders("+4915233145276", orderList -> {
            if (orderList != null) {
                Log.i("DataAccessTest", "getMyOrders: Success");
                Log.i("DataAccessTest", orderList.toString());
            } else {
                Log.i("DataAccessTest", "getMyOrders: Failure");
            }
        });
    }

    private void getOrders() {
        DataAccess.getInstance().getOrders(successful -> {
            if (successful) {
                Log.i("DataAccessTest", "getOrders: Success");
            } else {
                Log.i("DataAccessTest", "getOrders: Failure");
            }
        });
    }

    private void setOrderStatus() {
        // Confirmed
        DataAccess.getInstance().setOrderStatus("3Bh8isxyUHQ1Gny3G0Nn", Order.Status.CONFIRMED, successful -> {
            if (successful) {
                Log.i("DataAccessTest", "setOrderStatus: CONFIRMED:Success");
            } else {
                Log.i("DataAccessTest", "setOrderStatus: CONFIRMED:Failure");
            }
        });

        // Closed
        DataAccess.getInstance().setOrderStatus("3Bh8isxyUHQ1Gny3G0Nn", Order.Status.CLOSED, successful -> {
            if (successful) {
                Log.i("DataAccessTest", "setOrderStatus: CLOSED:Success");
                return;
            } else {
                Log.i("DataAccessTest", "setOrderStatus: CLOSED:Failure");
            }
        });
    }

}
