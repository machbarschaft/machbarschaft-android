package jetzt.machbarschaft.android.database.entitie;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import jetzt.machbarschaft.android.R;

/**
 * The urgency defines how fast the order should be processed.
 */
public enum Urgency {
    URGENT("asap", R.drawable.ic_order_urgent, R.color.urgency_urgent, R.string.order_urgency_urgent),
    TODAY("today", R.drawable.ic_order_today, R.color.urgency_urgent, R.string.order_urgency_today),
    TOMORROW("tomorrow", R.drawable.ic_order_tomorrow, R.color.urgency_normal, R.string.order_urgency_tomorrow),
    UNDEFINED("undefined", R.drawable.ic_order_undefined, R.color.urgency_normal, R.string.order_urgency_undefined);

    @NonNull
    private final String name;
    @DrawableRes
    private final int icon;
    @StringRes
    private final int title;
    @ColorRes
    private final int color;

    Urgency(@NonNull String name, @DrawableRes int icon, @ColorRes int color, @StringRes int title) {
        this.name = name;
        this.icon = icon;
        this.title = title;
        this.color = color;
    }

    /**
     * Gets the appropriate urgency for the given internal name.
     *
     * @param name The internal name of the urgency.
     * @return The parsed urgency.
     */
    @NonNull
    public static Urgency byName(String name) {
        for (Urgency urgency : values()) {
            if (urgency.name.equals(name)) {
                return urgency;
            }
        }
        return UNDEFINED;
    }

    /**
     * Gets the internal name of the urgency. This is the name used in firebase.
     *
     * @return The internal name of the urgency.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Gets the icon for a marker of this urgency.
     *
     * @return The resource id of the icon to use as a marker.
     */
    @DrawableRes
    public int getIconRes() {
        return icon;
    }

    /**
     * Gets the display title of the urgency.
     *
     * @return The resource id of the title.
     */
    @StringRes
    public int getTitle() {
        return title;
    }

    /**
     * Gets the resource id of the color to use with this urgency.
     *
     * @return The resource id of the color.
     */
    @ColorRes
    public int getColor() {
        return color;
    }

    /**
     * Gets the color value to use with this urgency.
     *
     * @param context The context to get the color value in.
     * @return The color value.
     */
    @ColorInt
    public int getColor(Context context) {
        return ContextCompat.getColor(context, color);
    }
}