import 'package:flutter/material.dart';

void main() => runApp(new HomePage());

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      home: new Signature(),
    );
  }
}

class Signature extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new SignatureState();
  }
}

class SignatureState extends State<Signature> {
  List<Offset> mPoints = <Offset>[];

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new GestureDetector(
      onPanUpdate: (DragUpdateDetails details) {
        setState(() {
          RenderBox renderBox = context.findRenderObject();
          Offset localPosition =
              renderBox.globalToLocal(details.globalPosition);
          mPoints = new List.from(mPoints)..add(localPosition);
        });
      },
      onPanEnd: (DragEndDetails details) {
        mPoints.add(null);
      },
      child: new CustomPaint(painter: new MyPainter(mPoints)),
    );
  }
}

class MyPainter extends CustomPainter {
  List<Offset> mPoints;

  MyPainter(this.mPoints);

  @override
  void paint(Canvas canvas, Size size) {
    var paint = new Paint()
      ..color = Colors.blue
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 5.0;

    for (int i = 0; i < mPoints.length - 1; i++) {
      if (mPoints[i] != null && mPoints[i + 1] != null) {
        canvas.drawLine(mPoints[i], mPoints[i + 1], paint);
      }
    }
  }

  @override
  bool shouldRepaint(MyPainter oldDelegate) {
    // TODO: implement shouldRepaint
    return oldDelegate.mPoints != mPoints;
  }
}
