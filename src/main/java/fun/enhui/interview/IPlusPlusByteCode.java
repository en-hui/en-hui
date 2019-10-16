package fun.enhui.interview;

/**
 * i++ 字节码信息
 * @Author: HuEnhui
 * @Date: 2019/10/16 14:28
 */
public class IPlusPlusByteCode {
    public static void main(String[] args) {
        IPlusPlusByteCode code = new IPlusPlusByteCode();
        code.add();
    }
    public volatile int n;
    public void add(){
        n++;
    }
}
