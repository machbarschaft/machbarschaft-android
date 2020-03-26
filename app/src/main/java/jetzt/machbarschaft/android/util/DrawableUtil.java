package jetzt.machbarschaft.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public final class DrawableUtil {
    private DrawableUtil() {
        // No instances
    }

    /**
     * Creates a {@link BitmapDescriptor} from the given drawable resource.
     *
     * @param context The context to create the descriptor in.
     * @param id      The id of the resource to create the descriptor from.
     * @return The bitmap descriptor for the given resource.
     */
    @Nullable
    public static BitmapDescriptor getBitmapDescriptor(Context context, @DrawableRes int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, id);
        if (vectorDrawable == null) {
            return null;
        }

        int height = vectorDrawable.getIntrinsicHeight();
        int width = vectorDrawable.getIntrinsicWidth();
        vectorDrawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
