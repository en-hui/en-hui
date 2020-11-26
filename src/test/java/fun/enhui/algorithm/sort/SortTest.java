package fun.enhui.algorithm.sort;

import fun.enhui.algorithm.base.a_sort.SelectionSort;
import fun.enhui.algorithm.sdk.BaseSort;
import fun.enhui.algorithm.sdk.SortCheck;
import org.junit.jupiter.api.Test;

/**
 * 排序测试
 *
 * @author 胡恩会
 * @date 2020/11/15 22:02
 */
public class SortTest {

    /**
     * 测试选择排序
     *
     * @author 胡恩会
     * @date 2020/11/15 22:34
     **/
    @Test
    public void testSelectionSort() {
        BaseSort sort = new SelectionSort();
        boolean result = SortCheck.check(sort);
        System.out.println(result);
    }
}
