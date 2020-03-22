package com.ks.einanrufhilft.Database;

import com.ks.einanrufhilft.Database.Entitie.Order;

public interface OrderLoadedCallback {
    void onOrderLoaded(Order order);
}
