package mad9132.planets;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mad9132.planets.model.PlanetPOJO;
import mad9132.planets.services.MyService;
import mad9132.planets.utils.NetworkHelper;

/**
 * Download images from a web server.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @author David Gasner (original)
 *
 * Reference: Chapter 4. Working with Binary Resources
 *            "Android App Development: RESTful Web Services" with David Gassner
 */
public class MainActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Map<Integer, Bitmap>> {

    private static final Boolean IS_LOCALHOST;
    private static final String JSON_URL;

    private Map<Integer, Bitmap> mBitmaps;
    private PlanetAdapter        mPlanetAdapter;
    private List<PlanetPOJO>     mPlanetList;
    private boolean              networkOk;
    private RecyclerView         mRecyclerView;

    static {
        IS_LOCALHOST = false;
        JSON_URL = IS_LOCALHOST ? "http://10.0.2.2:3000/planets" : "https://planets.mybluemix.net/planets";
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(MyService.MY_SERVICE_PAYLOAD)) {
                PlanetPOJO[] planetsArray = (PlanetPOJO[]) intent
                        .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);

                mPlanetList = Arrays.asList(planetsArray);

                getLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();

            } else if (intent.hasExtra(MyService.MY_SERVICE_EXCEPTION)) {
                String message = intent.getStringExtra(MyService.MY_SERVICE_EXCEPTION);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvPlanets);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(this);
        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    private void displayPlanets() {
        if (mPlanetList != null) {
            mPlanetAdapter = new PlanetAdapter(this, mPlanetList, mBitmaps);
            mRecyclerView.setAdapter(mPlanetAdapter);
        }
    }

    @Override
    public Loader<Map<Integer, Bitmap>> onCreateLoader(int i, Bundle bundle) {
        return new ImageDownloader(this, mPlanetList);
    }

    @Override
    public void onLoadFinished(Loader<Map<Integer, Bitmap>> loader, Map<Integer, Bitmap> integerBitmapMap) {
        mBitmaps = integerBitmapMap;
        displayPlanets();
    }

    @Override
    public void onLoaderReset(Loader<Map<Integer, Bitmap>> loader) {
        // NO-OP
    }

    private static class ImageDownloader
            extends AsyncTaskLoader<Map<Integer, Bitmap>> {

        private static final String PHOTOS_BASE_URL = JSON_URL + "/";
        private static List<PlanetPOJO> mPlanetList;

        public ImageDownloader(Context context, List<PlanetPOJO> planetList) {
            super(context);
            mPlanetList = planetList;
        }

        @Override
        public Map<Integer, Bitmap> loadInBackground() {
            //download image files here
            Map<Integer, Bitmap> map = new HashMap<>();
            for (PlanetPOJO aPlanet: mPlanetList) {
                String imageUrl = PHOTOS_BASE_URL + aPlanet.getPlanetId() + "/image";
                InputStream in = null;

                try {
                    in = (InputStream) new URL(imageUrl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    map.put(aPlanet.getPlanetId(), bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return map;
        }   // end method loadInBackground
    }   // end class ImageDownloader
}   // end class MainActivity
