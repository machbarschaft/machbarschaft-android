package jetzt.machbarschaft.android.database.callback;

import java.util.List;

import jetzt.machbarschaft.android.database.entitie.Collection;

public interface CollectionsLoadedCallback {
    void onOrdersLoaded(List<Collection> order);

}

