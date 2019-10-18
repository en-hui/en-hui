package fun.enhui.interview.Enum;

/**
 *  演示CountDownLatch使用
 * @author: HuEnhui
 * @date: 2019/10/18 10:38
 */
public enum CountryEnum {

    ONE(1,"齐"),
    TWO(2,"楚"),
    THREE(3,"燕"),
    FOUR(4,"赵"),
    FIVE(5,"魏"),
    SIX(6,"韩");

    private Integer retCode;
    private String retMessage;

    CountryEnum(Integer retCode, String retMessage) {
        this.retCode = retCode;
        this.retMessage = retMessage;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    /**
     *  根据retCode获取message
     * @author: HuEnhui
     * @date: 2019/10/18 10:42
     * @param index
     * @return: fun.enhui.interview.Enum.CountryEnum
     */
    public static CountryEnum forEach_CountryEnum(int index) {
        CountryEnum[] myArray = CountryEnum.values();
        for (CountryEnum element :myArray) {
            if(index == element.retCode) {
                return element;
            }
        }
        return null;
    }
}
