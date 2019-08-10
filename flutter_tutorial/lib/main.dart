import 'package:english_words/english_words.dart';
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key}) : super(key: key);

  @override
  createState() => MyListViewState();
}

class MyListViewState extends State<MyHomePage> {
  final suggestions = <WordPair>[];
  final myFavorites = new Set<WordPair>();
  final biggerFont = const TextStyle(fontSize: 18.0);

  Widget getView() {
    // 构造ListView
    return new ListView.builder(
        padding: const EdgeInsets.all(8.0),
        itemBuilder: (context, i) {
          if (i.isOdd) { // 如果是奇数，构造分割线
            return new Divider();
          }

          if (i >= suggestions.length) {
            // 如果超出范围，则再生成10项
            suggestions.addAll(generateWordPairs().take(10));
          }

          return fillView(suggestions[i]);
        }
    );
  }

  Widget fillView(WordPair wordPair) {
    final isFavorite = myFavorites.contains(wordPair);

    return new ListTile(
      // 生成一行内容
      title: new Text(
        wordPair.asPascalCase,
        style: biggerFont,
      ),
      trailing: new Icon(
        // 设置右侧图标
        isFavorite ? Icons.favorite : Icons.favorite_border,
        color: isFavorite ? Colors.red : null,
      ),
      onTap: () {
        // 点击事件，此处可以设置状态
        setState(() {
          isFavorite ? myFavorites.remove(wordPair) : myFavorites.add(wordPair);
        });
      },
    );
  }

  _onPressed() {
    Navigator.of(context).push(
      // 页面跳转，新页面入栈
      new MaterialPageRoute(builder: (context) {
        final tiles = myFavorites.map((pair) {
          return new ListTile(
            title: new Text(
              pair.asPascalCase,
              style: biggerFont,
            ),
          );
        },);
        final dividers = ListTile.divideTiles(
            context: context,
            tiles: tiles).toList();

        return Scaffold(
          // 为新页面设置标题和内容(内容为一个ListView)
          appBar: new AppBar(
            title: new Text("my favorite item"),
          ),
          body: new ListView(children: dividers,),
        );
      }),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("ListView与页面跳转"),
        actions: <Widget>[
          // 设置行为，添加按钮(左上角的list按钮)，并指定监听
          new IconButton(icon: new Icon(Icons.list), onPressed: _onPressed)
        ],
      ),
      body: getView(),
    );
  }

}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;


  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    final wordPair = new WordPair.random();
    return Scaffold(
      appBar: AppBar(

        title: Text(wordPair.asPascalCase),
      ),
      body: Center(
        child: Column(

          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme
                  .of(context)
                  .textTheme
                  .display1,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
