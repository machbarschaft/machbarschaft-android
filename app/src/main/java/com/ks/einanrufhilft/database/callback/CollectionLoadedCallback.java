package com.ks.einanrufhilft.database.callback;

import com.ks.einanrufhilft.database.entitie.Collection;

public interface CollectionLoadedCallback {
    void onOrderLoaded(Collection order);
}
