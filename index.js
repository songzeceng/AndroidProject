import React, {Component} from 'react';
import {AppRegistry, Image, StyleSheet, Text, View} from 'react-native';

const styles = StyleSheet.create({
    bigBlue: {
        color: "blue",
        fontWeight: "bold",
        fontSize: 30
    },

    red: {
        color: "red",
    }
});

class City extends Component {
    render() {
        return (
            <Text style={{
                fontSize:this.props.fontSize
            }}>{this.props.name}</Text>
        );
    }
}

class Blink extends Component {
    constructor(props) {
        super(props);
        this.state = {showText: true};

        setInterval(() => {
            this.setState({showText: !this.state.showText});
        }, 1000);
    }

    render() {
        let display = this.state.showText ? "黄埃散漫风萧索，云栈萦纡登剑阁" : "";
        return (
            <Text>{display}</Text>
        );
    }
}

class AwesomeProject extends Component {
    // 和项目名同名的class是主class
    render() { // render()方法用来渲染UI
        let pic = {
            uri: 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=746951351,3068781139&fm=200&gp=0.jpg'
        };
        return ( // return的内容就是渲染的内容
            <View style={{alignItems: 'center'}}>
                <Image source={pic} style={{width:200, height:200}}/>
                <City name='安阳' fontSize={50}/>
                <City name='新乡' fontSize={30}/>
                <City name='洛阳' fontSize={20}/>
                <Blink/>
                <Text style={styles.red}>红色</Text>
                <Text style={styles.bigBlue}>蓝色大号</Text>
                <Text style={[styles.red, styles.bigBlue]}>红色、蓝色大号</Text>
                <Text style={[styles.bigBlue, styles.red]}>蓝色大号、红色</Text>
            </View>
        );
    }
}

// 注意，这里用引号括起来的'AwesomeProject'必须和你init创建的项目名一致
// 只把应用作为一个整体注册一次，而不是每个组件或模块都注册
AppRegistry.registerComponent('AwesomeProject', () => AwesomeProject);