import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(new HomePage());

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      home: new AppPage(),
    );
  }
}

class AppPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new AppPageState();
  }
}

class AppPageState extends State<AppPage> {
  static const sPlatform = MethodChannel("app.channel.shared.data");
  String sharedData = "";

  getSharedText() async {
    var sharedText = await sPlatform.invokeMethod("getSharedText");
    if (sharedText != null) {
      setState(() {
        sharedData = sharedText as String;
      });
    }
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    getSharedText();
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: Text("处理外部Intent"),
      ),
      body: new Center(
        child: Text(sharedData),
      ),
    );
  }

}