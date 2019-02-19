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
      home: Scaffold( // home表示主页，传入的是一个Scaffold对象
          appBar: AppBar(title: Text("宋泽嶒的flutter"),), // 顶部栏
          body: Center( // 主页内容
            child: MyButton(), // 中间放一个MyButton
          ),
      )
    );
  }
}

class MyButton extends StatefulWidget {
  // MyButton继承StatefulWidget
  // StatefulWidget可以响应动作
  @override
  State<StatefulWidget> createState() {
    return MyButtonState(); // 创建一个初始状态
  }
}

class MyButtonState extends State<MyButton> {
  // 状态类，泛型绑定为MyButton
  final random = Random();
  int number1 = -1, number2 = -1;

  void produceRandomNumbers() {
    number1 = random.nextInt(20) + 1;
    number2 = random.nextInt(20) + 1;
    showDialog(context: context,
        // 显示对话框
        builder:(_) {
          // 构造对话框，返回一个AlertDialog
          int result = getResults();
          return AlertDialog(content: Text("两个随机数：$number1和$number2,"
              "乘积是$result" ));
          // $用来引用外部变量的值
        });
  }

  int getResults() {
    return number1 * number2;
  }
  @override
  Widget build(BuildContext context) {
    // 初始化状态
    return RaisedButton(onPressed: produceRandomNumbers, // onPressed相当于onClick
      child: Text("生成两个随机数"),);
  }
}
