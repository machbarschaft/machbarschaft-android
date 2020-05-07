package jetzt.machbarschaft.android.database.entitie;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import jetzt.machbarschaft.android.R;

public enum Type {
    GROCERIES("groceries", R.drawable.ic_type_groceries, R.string.order_title_groceries),
    MEDICINE("medicine", R.drawable.ic_type_medicine, R.string.order_title_medicine),
    OTHER("other", R.drawable.ic_type_other, R.string.order_title_other);

    @NonNull
    private final String name;
    @DrawableRes
    private final int icon;
    @StringRes
    private final int title;

    Type(@NonNull String name, @DrawableRes int icon, @StringRes int title) {
        this.name = name;
        this.icon = icon;
        this.title = title;
    }

    /**
     * Gets the appropriate type for the given internal name.
     *
     * @param name The internal name of the type.
     * @return The parsed type.
     */
    @NonNull
    public static Type byName(String name) {
        for (Type type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return OTHER;
    }

    /**
     * Gets the internal name of the type. This is the name used in firebase.
     *
     * @return The internal name of the type.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Gets the icon that describes the order type.
     *
     * @return The resource id of the icon.
     */
    @DrawableRes
    public int getIcon() {
        return icon;
    }

    /**
     * Gets the string resource of the tile of the type.
     *
     * @return The resource id of the type title.
     */
    @StringRes
    public int getTitle() {
        return title;
    }
}
