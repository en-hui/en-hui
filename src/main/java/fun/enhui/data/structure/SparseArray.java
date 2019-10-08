package fun.enhui.data.structure;

/**
 * 稀疏数组
 * @Author HuEnhui
 * @Date 2019/9/29 23:21
 **/
public class SparseArray {
    private static SparseArray sparseArray = new SparseArray();
    public static void main(String[] args) {
        // 创建一个原始的二维数组  11*11
        // 0表示没有棋子，1表示黑子  2表示蓝子
        int chessArray[][] = new int[11][11];
        chessArray[1][2] = 1;
        chessArray[2][3] = 2;
        // 输出二维数组
        System.out.println("原始的二维数组");
        sparseArray.showArray(chessArray);
        // 将二维数组转为稀疏数组
        chessArray = sparseArray.array2SparseArray(chessArray);
        System.out.println("转换后的稀疏数组");
        sparseArray.showArray(chessArray);

        chessArray = sparseArray.sparseArray2Array(chessArray);
        System.out.println("稀疏数组还原为二维数组");
        sparseArray.showArray(chessArray);


    }

    /**
     *  得到二维数组中，非0数据的个数
     * @author: HuEnhui
     * @date: 2019/9/25 11:21
     * @param chessArray 二维数组
     * @return: int
     */
    public int getArrayTotal(int[][] chessArray){
        int total = 0;
        for (int i=0;i<chessArray.length;i++){
            for(int j=0;j<chessArray[i].length;j++){
                if(chessArray[i][j] != 0) {
                    total++;
                }
            }
        }
        return total;
    }
    /**
     *  遍历二维数组并展示
     * @author: HuEnhui
     * @date: 2019/9/25 11:22
     * @param
     * @return: void
     */
    public void showArray(int[][] array) {
        for (int[] row :array) {
            for (int data :row) {
                System.out.printf("%d\t",data);
            }
            System.out.println();
        }
    }
    /**
     *  将二维数组转化为稀疏数组
     * @author: HuEnhui
     * @date: 2019/9/25 11:26
     * @param chessArray
     * @return: int[][]
     */
    public int[][] array2SparseArray(int[][] chessArray) {
        // 1.遍历二维数组，得到非0数据的个数
        int total = sparseArray.getArrayTotal(chessArray);
        // 2.创建对应的稀疏数组
        int sparseArray[][] = new int[total+1][3];
        // 给稀疏数组赋值  第一行含义    行  列   非0个数
        sparseArray[0][0] = chessArray.length;
        sparseArray[0][1] = chessArray[0].length;
        sparseArray[0][2] = total;

        // 遍历二维数组，将非0数据存入sparseArray
        // count 用于记录是第几个非0数据
        int count = 0;
        for (int i=0;i<chessArray.length;i++){
            for(int j=0;j<chessArray[i].length;j++){
                if(chessArray[i][j] != 0) {
                    count++;
                    // 记录第几行
                    sparseArray[count][0] = i;
                    // 记录第几列
                    sparseArray[count][1] = j;
                    // 记录非0数据的值
                    sparseArray[count][2] = chessArray[i][j];
                }
            }
        }
        return sparseArray;
    }

    /**
     *  将稀疏数组转化为二维数组
     * @author: HuEnhui
     * @date: 2019/9/25 11:39
     * @param sparseArray 稀疏数组
     * @return: int[][]
     */
    public int[][] sparseArray2Array(int[][] sparseArray) {
        // 根据稀疏数组的第一行创建二维数组
        int[][] cheesArray = new int[sparseArray[0][0]][sparseArray[0][1]];
        for(int i=1;i<sparseArray.length;i++) {
            // 稀疏数组的值赋给二维数组
            cheesArray[sparseArray[i][0]][sparseArray[i][1]] = sparseArray[i][2];
        }
        return cheesArray;
    }
}
