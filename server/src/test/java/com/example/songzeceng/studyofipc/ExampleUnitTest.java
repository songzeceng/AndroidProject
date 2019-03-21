package com.example.songzeceng.studyofipc;

import org.junit.Test;

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
    int[][] matrix = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
    int[][] hasPrintedMatrix = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
    int[] a = {3, 2, 4, 7, 1, 5};

    @Test
    public void addition_isCorrect() throws Exception {
        //  travelsTreeNoDigui();
        //   printMatrixShunShiZhen();
        quickSort(a, 0, a.length - 1);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    private void quickSort(int[] nums, int left, int right) {
        int tLeft = left;
        int tRight = right;

        if (tLeft <= tRight) {
            int temp = nums[tLeft];
            while (tLeft != tRight) {
                while (tRight > tLeft && nums[tRight] >= temp) {
                    tRight--;
                }
                nums[tLeft] = nums[tRight];

                while (tLeft < tRight && nums[tLeft] <= temp) {
                    tLeft++;
                }
                nums[tRight] = nums[tLeft];
            }

            //  出来之后tLeft = tRight
            nums[tLeft] = temp;
            quickSort(nums, left, tLeft - 1);
            quickSort(nums, tLeft + 1, right);
        }
    }

    private void printMatrixShunShiZhen() {
        int endLine = 3, endColumn = 2;
        int startLine = 0, startColumn = 0;
        int i, j;

        while (startLine <= endLine && startColumn <= endColumn) {
            i = startLine;
            j = startColumn;

            for (; j < endColumn + 1; j++) {
                printData(i, j);
            }
            j--;
            i++;

            for (; i < endLine + 1; i++) {
                printData(i, j);
            }
            i--;
            j--;

            for (; j > startColumn - 1; j--) {
                printData(i, j);
            }
            i--;
            j++;

            for (; i > startLine; i--) {
                printData(i, j);
            }

            startLine++;
            startColumn++;
            endColumn--;
            endLine--;

        }
    }

    private void printData(int i, int j) {
        if (hasPrintedMatrix[i][j] == 0) {
            System.out.print(matrix[i][j] + " ");
            hasPrintedMatrix[i][j] = 1;
        }
    }

    private void travelsTreeNoDigui() {
        Tree root1 = new Tree();
        Tree node = null;

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

        beforeOrdertraverseTree(root1);
    }

    private void beforeOrdertraverseTree(Tree root1) {
        Tree node;
        Stack<Tree> stack = new Stack<>();
        stack.push(root1);
        while (!stack.empty()) { // 开始下一次从右上到左下的循环
            node = stack.pop(); // 出栈的是某一个右结点

            while (node != null) {
                System.out.println("data:" + node.data);
                if (node.right != null) { // 右结点入栈
                    stack.push(node.right);
                }
                node = node.left;
            }
        }
    }

    private void midOrderTraverseTree(Tree root) {
        if (root == null) {
            return;
        }

        Stack<Tree> stack = new Stack<>();

        Tree node = root;
        while (node != null || !stack.isEmpty()) {
            while(node != null) {
                stack.push(node);
                node = node.left;
            }
            node = stack.pop(); // 此时已到最左下边
            System.out.println(node.data);
            node = node.right;
        }
    }

    private void afterOrderTraverseTree(Tree root) {
        if (root == null) {
            return;
        }
        Stack<Tree> stack = new Stack<>();

        Tree node = root;
        while(node != null || !stack.isEmpty()) {
            while(node != null) {
                stack.push(node);
                node = node.left == null ? node.right : node.left;
            }

            node = stack.pop();
            System.out.println(node.data);
            if (!stack.isEmpty() && node == stack.peek().left) {
                node = stack.peek().right;
            } else {
                node = null;
            }
        }
    }
}