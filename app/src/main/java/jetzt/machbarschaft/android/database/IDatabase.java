package jetzt.machbarschaft.android.database;

import android.location.Location;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.List;

import jetzt.machbarschaft.android.database.callback.DocumentLoadedCallback;
import jetzt.machbarschaft.android.database.entitie.Account;
import jetzt.machbarschaft.android.database.entitie.Order;

/**
 * An interface to access the database.
 */
public interface IDatabase {
    /**
     * Loads a single order from the database.
     *
     * @param orderId  The id of the order to load.
     * @param callback The callback to notify once the order is successfully loaded.
     */
    default void loadOrder(String orderId, DocumentLoadedCallback<Order> callback) {
        loadOrder(orderId, callback, null);
    }

    /**
     * Loads a single order from the database.
     *
     * @param orderId          The id of the order to load.
     * @param callback         The callback to notify once the order is successfully loaded.
     * @param completeListener A listener that is notified when the database query has been
     *                         performed. It will receive whether the query was successful or not.
     */
    void loadOrder(String orderId, DocumentLoadedCallback<Order> callback,
                   @Nullable OnCompleteListener<Boolean> completeListener);

    /**
     * Loads all orders from the database that are within the given area. The area is specified by
     * a central location and a radius around that define a circle.
     * <p>
     * Note, that due to the nature of Geohashes, this query will not only load orders that are
     * strictly within the circle, but will also load some orders, that are further apart from the
     * central location. If you need the orders to be strictly within the radius, you will have to
     * filter the returned list.
     *
     * @param location The location to search nearby orders for. This is the center of the circle
     *                 within which all orders will be returned.
     * @param radius   The radius of the circle in kilometers.
     * @param callback The callback to notify once the orders are successfully loaded.
     */
    default void loadNearbyOrders(Location location, float radius,
                                  DocumentLoadedCallback<List<Order>> callback) {
        loadNearbyOrders(location, radius, callback, null);
    }

    /**
     * Loads all orders from the database that are within the given area. The area is specified by
     * a central location and a radius around that define a circle.
     * <p>
     * Note, that due to the nature of Geohashes, this query will not only load orders that are
     * strictly within the circle, but will also load some orders, that are further apart from the
     * central location. If you need the orders to be strictly within the radius, you will have to
     * filter the returned list.
     *
     * @param location         The location to search nearby orders for. This is the center of the
     *                         circle within which all orders will be returned.
     * @param radius           The radius of the circle in kilometers.
     * @param callback         The callback to notify once the orders are successfully loaded.
     * @param completeListener A listener that is notified when the database query has been
     *                         performed. It will receive whether the query was successful or not.
     */
    void loadNearbyOrders(Location location, float radius,
                          DocumentLoadedCallback<List<Order>> callback,
                          @Nullable OnCompleteListener<Boolean> completeListener);

    /**
     * Loads the account of the user that is currently signed in.
     *
     * @param callback The callback to notify once the account is successfully loaded.
     */
    default void loadMyAccount(DocumentLoadedCallback<Account> callback) {
        loadMyAccount(callback, null);
    }

    /**
     * Loads the account of the user that is currently signed in.
     *
     * @param callback         The callback to notify once the account is successfully loaded.
     * @param completeListener A listener that is notified when the database query has been
     *                         performed. It will receive whether the query was successful or not.
     */
    void loadMyAccount(DocumentLoadedCallback<Account> callback,
                       @Nullable OnCompleteListener<Boolean> completeListener);

    /**
     * Claims the order for this user to indicate, that the user is processing the order. This will
     * set the account id in the order to the id of the account that is currently logged in to claim
     * the order, update the status to {@link Order.Status#IN_PROGRESS} and add a log message for
     * these changes.
     *
     * @param orderId The id of the order to claim.
     */
    default void claimOrder(String orderId) {
        closeOrder(orderId, null);
    }

    /**
     * Claims the order for this user to indicate, that the user is processing the order. This will
     * set the account id in the order to the id of the account that is currently logged in to claim
     * the order, update the status to {@link Order.Status#IN_PROGRESS} and add a log message for
     * these changes.
     *
     * @param orderId          The id of the order to claim.
     * @param completeListener A listener that is notified when the database query has been
     *                         performed. It will receive whether the query was successful or not.
     */
    void claimOrder(String orderId, @Nullable OnCompleteListener<Boolean> completeListener);

    /**
     * Releases a previously claimed order, so that it is again open for others to claim. This will
     * remove the currently logged in account from the order, revert the status to
     * {@link Order.Status#OPEN} and add a log message for these changes.
     *
     * @param orderId The id of the order to release.
     */
    default void releaseOrder(String orderId) {
        releaseOrder(orderId, null);
    }

    /**
     * Releases a previously claimed order, so that it is again open for others to claim. This will
     * remove the currently logged in account from the order, revert the status to
     * {@link Order.Status#OPEN} and add a log message for these changes.
     *
     * @param orderId          The id of the order to release.
     * @param completeListener A listener that is notified when the database query has been
     *                         performed. It will receive whether the query was successful or not.
     */
    void releaseOrder(String orderId, @Nullable OnCompleteListener<Boolean> completeListener);

    /**
     * Closes a previously claimed order to mark it as done. This will set the status to
     * {@link Order.Status#CLOSED} and add a log message for this change.
     *
     * @param orderId The id of the order to close.
     */
    default void closeOrder(String orderId) {
        closeOrder(orderId, null);
    }

    /**
     * Closes a previously claimed order to mark it as done. This will set the status to
     * {@link Order.Status#CLOSED} and add a log message for this change.
     *
     * @param orderId          The id of the order to close.
     * @param completeListener A listener that is notified when the database query has been
     *                         performed. It will receive whether the query was successful or not.
     */
    void closeOrder(String orderId, @Nullable OnCompleteListener<Boolean> completeListener);

    /**
     * Updates the account of the user that is currently signed in.
     *
     * @param account The new account data.
     */
    default void updateMyAccount(Account account) {
        updateMyAccount(account, null);
    }

    /**
     * Updates the account of the user that is currently signed in.
     *
     * @param account          The new account data.
     * @param completeListener A listener that is notified when the database query has been
     *                         performed. It will receive whether the query was successful or not.
     */
    void updateMyAccount(Account account, @Nullable OnCompleteListener<Boolean> completeListener);
}
