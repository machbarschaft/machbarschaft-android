package com.ks.einanrufhilft.Database.Callback;

import com.ks.einanrufhilft.Database.Entitie.Collection;

public interface CollectionLoadedCallback {
    void onOrderLoaded(Collection order);
}
