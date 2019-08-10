import 'package:flutter/material.dart';

void main() => runApp(HomePage());

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      home: new PageNavigator(),
      routes: <String, WidgetBuilder> {
        "A": (BuildContext context) => new PageA(),
        "B": (BuildContext context) => new PageB(),
        "C": (BuildContext context) => new PageC()
      },
    );
  }
}

class PageA extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: Text("A页面"),
      ),
      body: new Text("红军利物浦"),
    );
  }
}

class PageB extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: Text("B页面"),
      ),
      body: new Text("蓝月曼城"),
    );
  }
}

class PageC extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: Text("C页面"),
      ),
      body: new Text("南部之星拜仁"),
    );
  }
}

class PageNavigator extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new PageNavigatorState();
  }
}

class PageNavigatorState extends State<PageNavigator> {
  List<String> mItems = ["A", "B", "C"];
  getView() {
    return ListView.builder(
        padding: const EdgeInsets.all(5.0),
        itemBuilder: (context, i) {
          if (i.isOdd) {
            return new Divider();
          }

          int index = i ~/ 2;
          if (index >= mItems.length) {
            return null;
          }
          String currentItem = mItems[index];
          return new ListTile(
            title: Text(currentItem),
            onTap: () {
              Navigator.of(context).pushNamed(currentItem);
            },
          );
          
        });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: Text("路由跳转"),
      ),
      body: getView(),
    );
  }
}
