package fun.enhui.interview;

/**
 * 参数传递的问题
 * @Author: HuEnhui
 * @Date: 2019/10/17 13:59
 */
public class TestTrantsferValue {

    public void changeValue1(int age) {
        age = 30;
    }
    public void changeValue2(Person person) {
        person.setPersonName("XXX");
    }
    public void changeValue3(String str) {
        System.out.println(str);
        str = "xxx";
    }
    public void changeValue4(char[] str) {
        System.out.println(str);
        str[0] = 'a';
        str[1] = 'b';
        str[2] = 'c';
    }

    public static void main(String[] args) {
        TestTrantsferValue test = new TestTrantsferValue();
        int age = 20;
        test.changeValue1(age);
        System.out.println("age-----"+age);

        Person person = new Person("abc");
        test.changeValue2(person);
        System.out.println("person-----"+person.getPersonName());


        String str = "abc";
        test.changeValue3(str);
        System.out.println("String-----"+str);

        char[] strc = new char[10];
        strc[0] = 'a';
        strc[1] = 'a';
        strc[2] = 'a';
        test.changeValue4(strc);
        System.out.println(strc);
    }
}

class Person{
    int age;
    String personName;
    public Person(String name) {
        this.personName = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
