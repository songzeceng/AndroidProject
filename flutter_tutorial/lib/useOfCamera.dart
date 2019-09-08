import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

void main() {
  runApp(new MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: "照相",
      home: new ImagePickPage(),
    );
  }
}

class ImagePickPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new ImagePickSate();
  }
}

class ImagePickSate extends State<ImagePickPage> {
  var _image;

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("照相"),
      ),
      body: new Center(
        child:
            _image == null ? new Text("no image selected") : Image.file(_image), // 读取图片文件
      ),
      floatingActionButton: new FloatingActionButton(
          onPressed: getImage,
          tooltip: "get a image file",
          child: Icon(Icons.add_a_photo),
      ),
    );
  }

  Future getImage() async {
    var image = await ImagePicker.pickImage(source: ImageSource.camera);
    // 也可以选择数据源为ImageSource.gallery，表示从图库里选择图片
    print(image.path);

    setState(() {
      _image = image;
    });
  }
}
