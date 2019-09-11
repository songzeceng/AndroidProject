import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:io';
import 'package:path_provider/path_provider.dart';

void main() {
  runApp(new FileIOApp());
}

class FileIOApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "文件读写",
      home: new FileIOPage(),
    );
  }
}

class FileIOPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new FileIOPageState();
  }
}

class FileIOPageState extends State<FileIOPage> {
  int initCount = 0;
  String errorInfo = "";

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    readCounter().then((int value) {
      setState(() {
        initCount = value;
        errorInfo = "";
      });
    }).catchError((Object error) {
      errorInfo = error.toString();
      print("on error...");
      return null;
    });
  }

  Future<File> getLocalFile() async {
    String dir = (await getApplicationDocumentsDirectory()).path;
    return new File(dir + "/counter.txt");
  }

  Future<int> readCounter() async {
    try {
      File file = await getLocalFile();
      return int.parse((await file.readAsString()));
    } catch(e) {
      print("=======");
      print(e.toString());
      print("=======");
      return 0;
    }
  }

  void incrementCount() async {
    setState(() {
      initCount++;
    });

    (await getLocalFile()).writeAsString(initCount.toString());
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("文件io"),
      ),
      body: new Center(
        child: new Text(errorInfo == "" ? "$initCount" : errorInfo),
      ),
      floatingActionButton: new FloatingActionButton(
          onPressed: incrementCount,
          child: new Icon(Icons.add),
          tooltip: "点击+1",
      ),
    );
  }
}