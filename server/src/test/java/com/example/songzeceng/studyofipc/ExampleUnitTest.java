package com.example.songzeceng.studyofipc;

import org.junit.Test;

import java.util.HashMap;
import java.util.Stack;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class Tree {
    Tree left;
    Tree right;
    int data;
}

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Tree root1 = new Tree();
        Stack<Tree> stack = new Stack<>();
        HashMap<Tree, Integer> layerMap = new HashMap<>();
        stack.push(root1);
        Tree node = null;
        int layer = 1;

        Tree root2 = new Tree();
        Tree root3 = new Tree();
        Tree root4 = new Tree();
        Tree root5 = new Tree();
        Tree root6 = new Tree();
        Tree root7 = new Tree();

        root1.data = 1;
        root2.data = 2;
        root3.data = 3;
        root4.data = 4;
        root5.data = 5;
        root6.data = 6;
        root7.data = 7;

        root1.left = root2;
        root1.right = root3;
        root2.left = root4;
        root2.right = root5;
        root5.left = root7;
        root3.right = root6;

        while (!stack.empty()) { // 开始下一次从右上到左下的循环
            node = stack.pop(); // 出栈的是某一个右结点

            /*
              保存每一个结点和其父结点，做为键值对存储
                每一个结点再和其层数做为键值对
             */
            if (node != null && layerMap.get(node) != null) {
                layer = layerMap.get(node);
            }
            while (node != null) {
                System.out.println("data:" + node.data + "--layer:" + layer);
                layerMap.put(node, layer);
                if (node.right != null) { // 右结点入栈
                    layerMap.put(node.right,layer + 1);
                    stack.push(node.right);
                }

                node = node.left;
                layer++;
            }
        }
    }
}