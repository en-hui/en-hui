package fun.enhui.design.iterator;

/**
 * 模仿ArrayList
 *
 * @Author 胡恩会
 * @Date 2020/6/25 12:01
 **/
public class MyArrayList implements MyCollection {
    private Object[] objects = new Object[10];
    private int index = 0;

    @Override
    public void add(Object o) {
        // 扩容
        if (index == objects.length) {
            Object[] newObjects = new Object[objects.length * 2];
            System.arraycopy(objects, 0, newObjects, 0, objects.length);
            objects = newObjects;
        }
        objects[index] = o;
        index++;
    }

    @Override
    public int size() {
        return objects.length;
    }

    @Override
    public MyIterator iterator() {
        return new MyListIterator();
    }

    /**
     * 私有的内部类，只是自己遍历使用
     *
     * @Author: 胡恩会
     * @Date: 2020/6/25 12:07
     **/
    private class MyListIterator implements MyIterator {

        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            if (currentIndex >= index) {
                return false;
            }
            return true;
        }

        @Override
        public Object next() {
            Object o = objects[currentIndex];
            currentIndex++;
            return o;
        }
    }
}
