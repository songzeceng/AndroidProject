import 'package:flutter/material.dart';

void main() {
  runApp(new MyApp());
}

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "GestureDetector监听手势",
      home: new Scaffold(
        body: new Center(
          child: new GestureDetector(
            child: new FlutterLogo(
              size: 200,
            ),

            onTap: () {
              print("onTap");
            },

            onTapDown: (d) {
              print("onTapDown:" + d.toString());
            },

            onTapUp: (d) {
              print("onTapUp:" + d.toString());
            },

            onDoubleTap: () {
              print("onDoubleTap");
            },

            onLongPress: () {
              print("onLongPress");
            },
          ),
        ),
      )
    );
  }

}