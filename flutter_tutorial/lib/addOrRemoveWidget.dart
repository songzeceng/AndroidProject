import 'package:flutter/material.dart';

void main() => runApp(AddOrRemoveApp());

class AddOrRemoveApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "动态添加或删除组件",
      home: new AddOrRemovePage(),
    );
  }
}

class AddOrRemovePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new AddOrRemovePageState();
  }

}

class AddOrRemovePageState extends State<AddOrRemovePage> {
  bool flag = true;

  onClick() {
    setState(() {
      flag = !flag;
    });
  }

  getView() {
    if (flag) {
      return new Text("我的世界太过安静，安静的可以听见自己的心跳");
    } else {
      return new MaterialButton(
        onPressed: () {},
        child: Text("你以为我刀枪不入，我以为你百毒不侵"),
      );
    }
  }
  
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      body: new Center(
        child: getView(),),
      floatingActionButton: new FloatingActionButton(
          onPressed: onClick,
          tooltip: "点击改变视图",
          child: new Icon(Icons.update),
      ),

    );
  }

}