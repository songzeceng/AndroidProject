import 'package:flutter/material.dart';
import 'package:location/location.dart';

// 手机不支持google play，凉凉
void main() {
  runApp(new MyLocationApp());
}

class MyLocationApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "gps使用",
      home: new GPSPage(),
    );
  }
}

class GPSPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new GPSPageState();
  }
}

class GPSPageState extends State<GPSPage> {
  Location location = new Location();
  LocationData currentLocation;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    location.onLocationChanged().listen((LocationData newLocationData) {
      setState(() {
        print("on location changed");
        currentLocation = newLocationData;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("GPS的使用"),
      ),
      body: new Center(
        child: new Text(currentLocation == null
            ? "尚未获取到位置"
            : "经度：" +
                currentLocation.latitude.toString() +
                ",纬度：" +
                currentLocation.longitude.toString()),
      ),
    );
  }
}
