package jetzt.machbarschaft.android.database.entitie;

import androidx.annotation.NonNull;

import jetzt.machbarschaft.android.database.collections.Order;

/**
 * The status of an order defines whether it has been processed, is in process or can be
 * processed by some one.
 */
public enum Status {
    OPEN("open"),
    CONFIRMED("confirmed"),
    CLOSED("closed");

    @NonNull
    private final String name;

    Status(@NonNull String name) {
        this.name = name;
    }

    /**
     * Gets the appropriate status for the given internal name.
     *
     * @param name The internal name of the status.
     * @return The parsed status.
     */
    @NonNull
    public static Status byName(String name) {
        for (Status status : values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }
        return OPEN;
    }
}