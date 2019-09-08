import 'package:flutter/material.dart';
import 'package:sqflite/sqflite.dart';

void main() {
  runApp(new MySqfliteApp());
}

class MySqfliteApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "sqflite使用",
      home: new SqflitePage(),
    );
  }
}

class SqflitePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new SqflitePageState();
  }
}

class SqflitePageState extends State<SqflitePage> {
  Database db;
  int total = 0;
  List<Map> currentData = [];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    initDB();
  }

  void initDB() async {
    String dbPath = await getDatabasesPath();
    String path =
        dbPath.endsWith("\\") ? dbPath + "mydb.db" : dbPath + "\\mydb.db";

    db = await openDatabase(path, version: 1,
        onCreate: (Database db, int version) async {
      await db.execute(
          "create table if not exists student(id int(3), name varchar(20),"
          "constraint PK_STUDENT primary key(id))");
    });
    print("db init over..");
  }

  void getTotal() async {
    total = Sqflite.firstIntValue(
        await db.rawQuery("select count(*) from student"));
  }

  void insert() async {
    getTotal();
    await db.transaction((txn) async {
      txn.rawInsert(
          "insert into student(id, name) values(?, ?)", [total + 1, "szc"]);
      txn.rawInsert(
          "insert into student(id, name) values(?, ?)", [total + 2, "jason"]);
    });
    print("insert over..");
  }

  void update() async {
    getTotal();
    if (total > 0) {
      await db.rawUpdate(
          "update student set name = ? where id = ?", ["songzeceng", total - 1]);
      await db.rawUpdate(
          "update student set name = ? where id = ?", ["songzeceng", total - 1]);
      // 似乎更新语句要执行两遍才有一遍的效果
    }
    print("update over..");
  }

  void delete() async {
    getTotal();
    await db.rawDelete("delete from student where id = ?", [total]);
    await db.rawDelete("delete from student where id = ?", [total]);
    // 似乎删除语句要执行两遍才有一遍的效果
    print("delete over..");
  }

  void getAll() async {
    List<Map> ret = await db.rawQuery("select * from student");

    setState(() {
      currentData = ret;
    });
  }

  void release() async {
    await db.close();
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("sqflite的使用"),
          actions: <Widget>[
            new PopupMenuButton(
              itemBuilder: (BuildContext context) => <PopupMenuItem<String>>[
                new PopupMenuItem(
                  child: new Text("add"),
                  value: "add",
                ),
                new PopupMenuItem(
                  child: new Text("update"),
                  value: "update",
                ),
                new PopupMenuItem(
                  child: new Text("delete"),
                  value: "delete",
                ),
                new PopupMenuItem(
                  child: new Text("queryAll"),
                  value: "queryAll",
                ),
              ],
              onSelected: (String value) {
                switch (value) {
                  case "add":
                    insert();
                    break;
                  case "update":
                    update();
                    break;
                  case "delete":
                    delete();
                    break;
                  case "queryAll":
                    getAll();
                    break;
                }
              },
            )
          ],
        ),
        body: getBody());
  }

  getBody() {
    if (currentData.length == 0) {
      return new Text("no data by now");
    } else {
      return new ListView.builder(
        itemCount: currentData.length,
        itemBuilder: (BuildContext context, int index) {
          return new Padding(
            padding: new EdgeInsets.all(10.0),
            child: new Text(
                currentData[index]["id"].toString() + "--" + currentData[index]["name"]
            ),
          );
        },
      );
    }
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    release();
  }
}
