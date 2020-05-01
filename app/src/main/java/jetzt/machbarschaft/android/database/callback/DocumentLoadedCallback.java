package jetzt.machbarschaft.android.database.callback;

/**
 * This callback is used to pass a document that has been loaded from the database back to the
 * caller of the database query.
 *
 * @param <T> The type of document that has been requested.
 */
public interface DocumentLoadedCallback<T> {
    /**
     * Called when the requested document has been loaded from the database.
     *
     * @param document The document that has been loaded.
     */
    void onDocumentLoaded(T document);
}
