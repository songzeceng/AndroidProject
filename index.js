import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    TextInput,
    View
} from 'react-native';

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
                fontSize: this.props.fontSize
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

class CityInput extends Component {
    constructor(props) {
        super(props);
        this.state = {text: ""};
    }

    render() {
        return (
            <View>
                <TextInput
                    style={{height: 40}}
                    placeholder={"输入你要去的城市"} // 占位字符串
                    // onChangeText={ // 输入内容有变时调用
                    //     (text) => this.setState({text})
                    // }
                    onSubmitEditing={ // 按下软键盘上的确认键后调用，参数是event
                        (event) => this.setState({text:event.nativeEvent.text})
                        // 通过调用event.nativeEvent.text获取输入的字符串
                    }
                ></TextInput>

                <Text style={{padding: 10, fontSize: 30}}>
                    {
                        this.state.text.split('-').map( // split函数：分割，map函数：转换
                            (str) => "那就去" + str
                        ).join('\n') // 字符串拼接
                    }
                </Text>
            </View>
        );
    }
}

/**
 * width和height可以指定元素的尺寸，RN的尺寸是没有单位的逻辑像素，和设备无关
 * 当style以数组的形式设定时，后面的元素会覆盖前面相同类型的元素
 * */

class AwesomeProject extends Component {
    render() { // render()方法用来渲染UI
        let pic = {
            uri: 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=746951351,3068781139&fm=200&gp=0.jpg'
        };
        return ( // return的内容就是渲染的内容
            <View style={{
                alignItems: 'center',
                flex: 1,
                // flex是权重
                flexDirection: 'row',
                // flexDirection是排列方向，排成一列column或者排成一排row
                flexWrap: 'wrap',
                // flexWrap:内容包裹形式。nowrap:不换行，此为默认 wrap:换行，wrap-reverse:换行，第一行在下面
                justifyContent: 'space-around'
                // justifyContent意为内容的分布方式，可选项有：flex-start、center、flex-end、space-around、space-between
                // 以横向排列为例，意思依次是左对齐，居中，右对齐，每个子项两侧间隔相等(所以不会靠两侧)，每个子项之间的间隔相等(所以会靠两侧)
            }}>
                {/*<Image source={pic} style={{width: 200, height: 200}}/>*/}
                {/*<City name='安阳' fontSize={50}/>*/}
                {/*<City name='新乡' fontSize={30}/>*/}
                {/*<City name='洛阳' fontSize={20}/>*/}
                {/*<Blink/>*/}
                {/*<Text style={styles.red}>红色</Text>*/}
                {/*<Text style={styles.bigBlue}>蓝色大号</Text>*/}
                {/*<Text style={[styles.red, styles.bigBlue]}>红色、蓝色大号</Text>*/}
                {/*<Text style={[styles.bigBlue, styles.red]}>蓝色大号、红色</Text>*/}
                <CityInput/>
            </View>
        );
    }
}

// 注意，这里用引号括起来的'AwesomeProject'必须和你init创建的项目名一致
// 只把应用作为一个整体注册一次，而不是每个组件或模块都注册
// 经过下面的注册，AwesomeProject类就相当于AwesomeProject工程的main类了
AppRegistry.registerComponent('AwesomeProject', () => AwesomeProject);