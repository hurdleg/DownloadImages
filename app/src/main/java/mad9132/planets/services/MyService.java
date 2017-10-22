package mad9132.planets.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import mad9132.planets.model.PlanetPOJO;
import mad9132.planets.utils.HttpHelper;

/**
 * Class MyService.
 *
 * Fetch the data at URI.
 * Return an array of PlanetPOJO[] as a broadcast message.
 *
 * @author David Gasner
 */
public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_EXCEPTION = "myServiceException";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        Gson gson = new Gson();
        PlanetPOJO[] planetsArray = gson.fromJson(response, PlanetPOJO[].class);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, planetsArray);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }
}
