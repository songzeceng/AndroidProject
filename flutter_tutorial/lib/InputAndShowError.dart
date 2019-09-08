import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  runApp(new MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "表单输入和验证对错",
      home: new InputPage(),
    );
  }
}

class InputPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new InputPageState();
  }
}

class InputPageState extends State<InputPage> {
  String errorText;
  SharedPreferences sp;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    initSp();
  }

  void initSp() async {
    sp = await SharedPreferences.getInstance();
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("表单输入和验证对错"),
      ),
      body: new Center(
        child: new TextField(
          onSubmitted: (String text) {
            // 输入回车键提交，调用此方法
            setState(() {
              String defaultName = sp.get("defaultName");

              if (defaultName == null) {
                print("no sp data");
                defaultName = "szc";
              }

              print("defaultName:" + defaultName);

              if (text == "szc" || text == "songzeceng") {
                // dart中判断字符串内容相等，直接用==
                sp.setString("defaultName", text);
                errorText = null;
              } else {
                errorText = "name error";
              }
            });
          },

          decoration: new InputDecoration(
            hintText: "Input name",
            errorText: errorText
          ),
        ),
      ),
    );
  }
}