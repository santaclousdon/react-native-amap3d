import * as React from "react";
import { StyleSheet } from "react-native";
import { MapView } from "react-native-amap3d";

const coordinates = [
  {
    latitude: 39.806901,
    longitude: 116.397972,
  },
  {
    latitude: 39.806901,
    longitude: 116.297972,
  },
  {
    latitude: 39.906901,
    longitude: 116.397972,
  },
];

export default () => (
  <MapView style={StyleSheet.absoluteFill}>
    <MapView.Polygon
      strokeWidth={5}
      strokeColor="rgba(0, 0, 255, 0.5)"
      fillColor="rgba(255, 0, 0, 0.5)"
      coordinates={coordinates}
    />
  </MapView>
);
