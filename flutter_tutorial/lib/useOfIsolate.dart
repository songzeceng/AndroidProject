import 'dart:convert';
import 'package:flutter/material.dart';
import 'dart:isolate';
import 'dart:async';
import 'package:http/http.dart' as http;

void main() {
  runApp(new SampleApp());
}

class SampleApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "Isolate异步加载",
      home: new SampleAppPage(),
    );
  }
}

class SampleAppPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new SampleAppPageState();
  }
}

class SampleAppPageState extends State<SampleAppPage>
    with WidgetsBindingObserver {
  List widgets = [];
  AppLifecycleState _lastState;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    WidgetsBinding.instance.addObserver(this);
    loadData();
  }

  void loadData() async {
    ReceivePort outReceivePort = new ReceivePort();
    await Isolate.spawn(dataLoader, outReceivePort.sendPort);
    // Isolate异步加载数据，入参：加载的入口方法和初始化消息对象(通常是一个sendPort)

    SendPort sendPort = await outReceivePort.first;
    // 等待接收消息，由于outReceivePort在dataLoader里已经给dataLoader.receivePort发送了消息
    // 所以这儿直接返回了dataLoader.receivePort

    List msg = await sendReceive(
        sendPort, "https://jsonplaceholder.typicode.com/posts");
    // 监听sendReceive.receivePort，因为此方法返回的是sendReceive.receivePort.first(这是一个Future对象)

    setState(() {
      widgets = msg;
    });
  }

  static dataLoader(SendPort sendPort) async {
    ReceivePort receivePort = new ReceivePort();

    sendPort.send(receivePort.sendPort);

    await for (var msg in receivePort) {
      // 等待dataLoader.receivePort发送消息
      // 在sendReceive方法中，dataLoader.receivePort给sendReceive.receivePort发送消息，这里结束等待

      String data = msg[0];
      SendPort replyTo = msg[1];

      String dataUrl = data;

      http.Response response = await http.get(dataUrl);
      replyTo.send(json.decode(response.body)); // 给sendReceive.receivePort发消息
    }
  }

  Future sendReceive(SendPort sendPort, msg) {
    ReceivePort receivePort = new ReceivePort();
    sendPort.send([msg, receivePort.sendPort]);

    return receivePort.first;
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Isolate异步加载"),
      ),
      body: getBody(),
    );
  }

  getBody() {
    if (widgets.length == 0) {
      return new Center(
        child: new CircularProgressIndicator(),
      );
    } else {
      return new ListView.builder(
          itemCount: widgets.length + 1,
          itemBuilder: (BuildContext context, int index) {
            if (index == 0) {
              return new Text(_lastState == null
                  ? "state not changed"
                  : "sate is " + _lastState.toString());
            } else {
              return new Padding(
                padding: new EdgeInsets.all(10.0),
                child: new Text("Row ${widgets[index - 1]["title"]}"),
              );
            }
          });
    }
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    // 生命周期回调监听
    // Android：resumed和paused
    // IOS：resumed、inactive(非活动，没接收用户输入)、paused(不可见)和suspending(暂时中止)
    super.didChangeAppLifecycleState(state);
    setState(() {
      _lastState = state;
    });
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    WidgetsBinding.instance.removeObserver(this);
  }
}
