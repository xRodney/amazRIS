package name.xrodney.amazris.fragment;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kieronquinn.library.amazfitcommunication.location.ProxyLocationManager;

public class NearbyStopListFragment extends AbcStopListFragment {
    private Location location;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Acquire a reference to the system Location Manager
        ProxyLocationManager locationManager = new ProxyLocationManager(requireContext(), null);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.d(this.getClass().getSimpleName(), "Has location " + location.toString());
                locationChanged(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(this.getClass().getSimpleName(), "Status changed " + provider + ": " + status);
            }

            public void onProviderEnabled(String provider) {
                Log.d(this.getClass().getSimpleName(), "Provider enabled " + provider);
            }

            public void onProviderDisabled(String provider) {
                Log.d(this.getClass().getSimpleName(), "Provider disabled " + provider);
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    protected void load() {
        if (location != null) {
            Log.i(getClass().getSimpleName(), "Location: " + location);
            client.getStops(requireContext(), location.getLatitude(), location.getLongitude(), this);
        } else {
            Log.i(getClass().getSimpleName(), "Waiting for location...");
        }
    }

    private void locationChanged(Location location) {
        this.location = location;
        refresh();
    }
}
