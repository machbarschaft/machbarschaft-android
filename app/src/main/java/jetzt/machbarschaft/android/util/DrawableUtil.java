package jetzt.machbarschaft.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import jetzt.machbarschaft.android.database.collections.Order;

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
    public static BitmapDescriptor getBitmapDescriptor(Context context, @DrawableRes int id, Order order) {
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


        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((42));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        Log.wtf("GETLIST", String.valueOf(order.getListId()));
        paint.getTextBounds(String.valueOf(order.getListId()), 0, String.valueOf(order.getListId()).length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(String.valueOf(order.getListId()), x, y, paint);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
