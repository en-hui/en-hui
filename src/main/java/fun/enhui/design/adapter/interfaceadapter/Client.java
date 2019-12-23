package fun.enhui.design.adapter.interfaceadapter;

public class Client {

    public static void main(String[] args) {

        new AbsAdapter(){
            @Override
            public void method1() {
                System.out.println("只需重写实际需要的方法");
            }
        };
    }
}
