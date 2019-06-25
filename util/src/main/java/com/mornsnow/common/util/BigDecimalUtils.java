package com.mornsnow.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * BigDecimal工具类
 * <p>
 * 解决浮点型数据计算导致的精度问题
 *
 * @author jianyang 2018/8/23
 */
public class BigDecimalUtils {
    private static final int SCALE = 4;

    private static NumberFormat devideResultFormat = NumberFormat.getInstance();
    private static NumberFormat paramNumberFormat = NumberFormat.getInstance();

    static {
        paramNumberFormat.setGroupingUsed(false);
        paramNumberFormat.setMinimumFractionDigits(0);
        paramNumberFormat.setMaximumFractionDigits(10);

        devideResultFormat.setGroupingUsed(false);
        devideResultFormat.setMinimumFractionDigits(0);
        devideResultFormat.setMaximumFractionDigits(SCALE);
    }

    /**
     * 加
     * 参数个数≥2
     *
     * @param numbers
     * @return
     */
    public static double add(double... numbers) {
        BigDecimal decimal = new BigDecimal(paramNumberFormat.format(numbers[0]));

        for (int i = 1; i < numbers.length; i++) {
            decimal = decimal.add(new BigDecimal(paramNumberFormat.format(numbers[i])));
        }

        return decimal.doubleValue();
    }

    /**
     * 减
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double subtract(double d1, double d2) {

        BigDecimal decimal = new BigDecimal(paramNumberFormat.format(d1));
        decimal = decimal.subtract(new BigDecimal(paramNumberFormat.format(d2)));

        return decimal.doubleValue();
    }

    /**
     * 乘
     * 参数个数≥2
     *
     * @param numbers
     * @return
     */
    public static double multiply(double... numbers) {
        BigDecimal decimal = new BigDecimal(paramNumberFormat.format(numbers[0]));
        for (int i = 1; i < numbers.length; i++) {
            decimal = decimal.multiply(new BigDecimal(paramNumberFormat.format(numbers[i])));
        }
        return decimal.doubleValue();
    }

    /**
     * 除
     * 除不尽时默认保留4位小数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double divide(double d1, double d2) {
        double d = new BigDecimal(paramNumberFormat.format(d1))
                .divide(new BigDecimal(paramNumberFormat.format(d2)), new MathContext(32, RoundingMode.CEILING))
                .doubleValue();

        return Double.parseDouble(devideResultFormat.format(d));
    }
}
