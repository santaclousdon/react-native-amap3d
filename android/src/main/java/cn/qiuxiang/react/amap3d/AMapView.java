package cn.qiuxiang.react.amap3d;

import android.annotation.SuppressLint;
import android.location.Location;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class AMapView extends MapView {
    public final AMap map;
    public final UiSettings ui;
    private final RCTEventEmitter eventEmitter;
    private Map<String, AMapMarker> markers = new HashMap<>();

    public AMapView(final ThemedReactContext context) {
        super(context);
        super.onCreate(null);
        map = this.getMap();
        ui = map.getUiSettings();
        eventEmitter = context.getJSModule(RCTEventEmitter.class);

        // 设置默认的定位模式
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        map.setMyLocationStyle(locationStyle);

        map.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                sendEvent("onMapLoaded", Arguments.createMap());
            }
        });

        map.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                WritableMap event = Arguments.createMap();
                event.putDouble("latitude", latLng.latitude);
                event.putDouble("longitude", latLng.longitude);
                sendEvent("onMapClick", event);
            }
        });

        map.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                WritableMap event = Arguments.createMap();
                event.putDouble("latitude", latLng.latitude);
                event.putDouble("longitude", latLng.longitude);
                sendEvent("onMapLongClick", event);
            }
        });

        map.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                WritableMap event = Arguments.createMap();
                event.putDouble("latitude", location.getLatitude());
                event.putDouble("longitude", location.getLongitude());
                event.putDouble("accuracy", location.getAccuracy());
                sendEvent("onLocationChange", event);
            }
        });

        map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markers.get(marker.getId()).sendEvent("onMarkerClick", Arguments.createMap());
                return false;
            }
        });
    }

    public void addMarker(AMapMarker marker) {
        marker.addToMap(map);
        markers.put(marker.getMarkerId(), marker);
    }

    public void sendEvent(String name, WritableMap data) {
        eventEmitter.receiveEvent(getId(), name, data);
    }
}
