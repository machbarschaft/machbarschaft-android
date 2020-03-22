
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MessagingService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // TODO handle message
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(LOG_TAG, "New token: " + token);

        // TODO send new token to server
    }
}
