import 'package:flutter/material.dart';

void main() => runApp(HomePage());

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      home: new AnimationPage(),
    );
  }

}

class AnimationPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new AnimationPageState();
  }

}

class AnimationPageState extends State<AnimationPage> with TickerProviderStateMixin {
  AnimationController mAnimationController;
  CurvedAnimation mCurvedAnimation;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    mAnimationController = new AnimationController(
        duration: const Duration(milliseconds: 2 * 1000),
        vsync: this);
    mCurvedAnimation = new CurvedAnimation(
        parent: mAnimationController,
        curve: Curves.easeOut);
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: Text("flutter中的动画"),),
      body: new Center(
        child: new Container(
          child: new FadeTransition(
              opacity: mCurvedAnimation,
              child: new FlutterLogo(
                size: 100,
              ),
          ),
        ),),
      floatingActionButton: new FloatingActionButton(
          tooltip: "开始动画",
          child: new Icon(Icons.brush),
        onPressed: () {
            mAnimationController.forward();
        },
      ),
    );
  }

}