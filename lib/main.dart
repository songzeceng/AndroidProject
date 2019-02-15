import 'dart:math';

import 'package:flutter/material.dart';

void main() => runApp(MyApp()); // main()函数，入口
// 执行runApp()方法启动应用，先构造一个MyApp对象

class MyApp extends StatelessWidget { // 无状态控件，只负责显示信息，不负责响应动作
  @override
  Widget build(BuildContext context) { // 控件的主方法时build()方法，负责显示东西
    return MaterialApp( // 返回一个MaterialApp
      title: '宋泽嶒的flutter应用', // 传参以键值对的形式传入
      theme: ThemeData(
        // theme是主题外观
        primarySwatch: Colors.lightBlue,
      ),
      home: MyHomePage( // home表示主页，传入的是一个MyHomePage对象
          title: '主页',
      )
    );
  }
}

class MyHomePage extends StatefulWidget { // 有状态的控件，可以响应动作
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
  // 初始状态是构造一个_MyHomePageState对象，也就是一个状态对象
}

class _MyHomePageState extends State<MyHomePage> { // 传入泛型是和此状态绑定的控件类
  final random = Random();
  int number1 = -1, number2 = -1;

  void produceRandomNumbers() {
    number1 = random.nextInt(20) + 1;
    number2 = random.nextInt(20) + 1;
  }

  int getResults() {
    return number1 + number2;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              '两个随机数:',
            ),
            Text(
              '$number1' + '和 ' + '$number2',
              style: Theme.of(context).textTheme.display1,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: produceRandomNumbers,
        tooltip: '产生两个随机数',
        child: Icon(Icons.center_focus_strong),
      ),
    );
  }
}
