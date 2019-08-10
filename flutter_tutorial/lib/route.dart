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
      floatingActionButton: new FloatingActionButton(onPressed: () {
        Navigator.of(context).pop({
          "主教练": "克洛普",
          "队长": "乔丹·亨德森"
        });
      }, child: Icon(Icons.arrow_back),),
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
      floatingActionButton: new FloatingActionButton(onPressed: () {
        Navigator.of(context).pop({
          "主教练": "佩普·瓜迪奥拉",
          "队长": "孔帕尼"
        });
      }, child: Icon(Icons.arrow_back),),
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
      floatingActionButton: new FloatingActionButton(onPressed: () {
        Navigator.of(context).pop({
          "主教练": "尼科·科瓦奇",
          "队长": "诺伊尔"
        });
      }, child: Icon(Icons.arrow_back),),
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

  void updateList(int index, String newValue) {
    setState(() {
      mItems[index] = newValue;
    });
  }

  String map2Str(Map map) {
    StringBuffer buffer = new StringBuffer();
    List<String> keys = map.keys.toList();
    List<String> values = map.values.toList();
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];
      String value = values[i];
      buffer.write(key + ":" + value + "\n");
    }

    String retStr = buffer.toString();
    return retStr.substring(0, retStr.lastIndexOf("\n"));
  }

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
            onTap: () async {
              Map result = await Navigator.of(context).pushNamed(currentItem) as Map;
              if (result != null) {
                updateList(index, map2Str(result));
              }
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
