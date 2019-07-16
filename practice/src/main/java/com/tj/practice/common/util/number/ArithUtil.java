package com.tj.practice.common.util.number;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数据计算和比较
 *
 * @author Antony
 * @since 9/14/2018
 */
public class ArithUtil {

    /**
     * 百分比
     */
    public static final BigDecimal PERCENT_NUMBER = new BigDecimal(100);

    /**
     * 相反数
     **/
    public static final BigDecimal OPPOSITE_NUMBER = new BigDecimal(-1);
    /**
     * 精度计算时默认2位小时
     */
    public static final int SCALE_DEF = 2;

    /**
     * 除法运行默认保留10位小数
     */
    public static final int SCALE_DIV = 10;

    /**
     * 默认值处理 BigDecimal为空是返回 new BigDecimal(0d)
     *
     * @param obj 原始值
     * @return 处理后的值
     */
    public static BigDecimal defValue(Object obj) {
        BigDecimal result = new BigDecimal(0);
        try {
            if (obj != null) {
                String s = obj + "";
                if (StringUtils.isEmpty(s)) {
                    result = new BigDecimal(s);
                }
            }
        } catch (Exception e) {
            result = new BigDecimal(0);
        }
        return result;
    }

    /**
     * 默认值处理 BigDecimal为空是返回 new BigDecimal(0d)
     *
     * @param v1 原始值
     * @return 处理后的值
     */
    public static BigDecimal defValue(BigDecimal v1) {
        if (v1 == null) {
            v1 = new BigDecimal(0d);
        }
        return v1;
    }

    /**
     * 默认值处理 BigDecimal为空是返回 new BigDecimal(0d)
     *
     * @param v1 原始值
     * @param dv 默认值
     * @return 处理后的值
     */
    public static BigDecimal defValue(BigDecimal v1, BigDecimal dv) {
        if (v1 == null) {
            v1 = dv;
        }
        return v1;
    }

    /**
     * 转换为double BigDecimal为空是返回0
     *
     * @param v1 原始值
     * @return 处理后的值
     */
    public static double doubleValue(BigDecimal v1) {
        if (v1 == null) {
            return 0d;
        }
        return v1.doubleValue();
    }

    /**
     * 转换为int BigDecimal为空是返回0
     *
     * @param v1 原始值
     * @return 处理后的值
     */
    public static int intValue(BigDecimal v1) {
        if (v1 == null) {
            return 0;
        }
        return v1.intValue();
    }

    /**
     * 百分比 最多保留4位小数 0.56 -> 56%
     *
     * @param v1 数字
     * @return 百分比字符串
     */
    public static String percentStr(BigDecimal v1) {
        return numberFormat(v1, "####.####%");
    }

    /**
     * 格式化数字 最多保留4位小数
     *
     * @param d 数字
     * @return string
     */
    public static String numberFormat(BigDecimal d) {
        return numberFormat(d, "####.####");
    }

    /**
     * 格式化数字
     *
     * @param d   数字
     * @param fmt 格式化字符串
     * @return string
     */
    public static String numberFormat(BigDecimal d, String fmt) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.applyPattern(fmt);
        return df.format(defValue(d));
    }

    /**
     * 金额相加
     *
     * @param v1 基础值
     * @param v2 被加数
     * @return value
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        v1 = defValue(v1);
        v2 = defValue(v2);
        return v1.add(v2);
    }

    /**
     * 金额相加
     *
     * @param i1 基础值
     * @param i2 被加数
     * @return value
     */
    public static BigDecimal add(int i1, int i2) {
        BigDecimal v1 = new BigDecimal(i1);
        BigDecimal v2 = new BigDecimal(i2);
        return v1.add(v2);
    }

    /**
     * 金额相减
     *
     * @param v1 基础值
     * @param v2 减数
     * @return value
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        v1 = defValue(v1);
        v2 = defValue(v2);
        return v1.subtract(v2);
    }

    /**
     * 合计
     *
     * @param v1 数值集合
     * @return value
     */
    public static BigDecimal sum(Object... v1) {
        BigDecimal v = new BigDecimal(0);
        if (v1 != null) {
            Object[] vals = v1;
            int tm = v1.length;
            for (int i = 0; i < tm; ++i) {
                Object one = vals[i];
                v = add(v, defValue(one));
            }
        }
        return v;
    }

    /**
     * 合计
     *
     * @param v1 数值集合
     * @return value
     */
    public static BigDecimal sum(BigDecimal... v1) {
        BigDecimal v = new BigDecimal(0);
        if (v1 != null) {
            BigDecimal[] vals = v1;
            int tm = v1.length;
            for (int i = 0; i < tm; ++i) {
                BigDecimal one = vals[i];
                one = defValue(one);
                v = add(v, one);
            }
        }
        return v;
    }

    /**
     * 金额相除 <br/>
     *
     * @param v1 基础值
     * @param v2 被乘数
     * @return value
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, SCALE_DIV);
    }

    /**
     * 金额相除 <br/>
     * 精确小位小数
     *
     * @param v1    基础值
     * @param v2    被乘数
     * @param scale 精确到小数点多少位
     * @return value
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        v1 = defValue(v1);
        v2 = defValue(v2);
        if (compareToEqual(v2, new BigDecimal(0))) {
            return new BigDecimal(0);
        }
        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 金额相乘
     *
     * @param v1 基础值
     * @param v2 被乘数
     * @return value
     */
    public static BigDecimal mul(Object v1, Object v2) {
        BigDecimal vv1 = defValue(v1);
        BigDecimal vv2 = defValue(v2);
        return vv1.multiply(vv2);
    }

    /**
     * 金额相乘
     *
     * @param v1 基础值
     * @param v2 被乘数
     * @return value
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        v1 = defValue(v1);
        v2 = defValue(v2);
        return v1.multiply(v2);
    }

    /**
     * 判断是否相等
     *
     * @param v1 v1
     * @param v2 v2
     * @return boolean
     */
    public static boolean compareToEqual(BigDecimal v1, BigDecimal v2) {
        v1 = defValue(v1);
        v2 = defValue(v2);
        return v1.compareTo(v2) == 0;
    }

    /**
     * 绝对大于 v1 大于 t1 为 true
     *
     * @param v1 比数
     * @param v2 被比数
     * @return boolean
     */
    public static boolean compareToBig(Object v1, Object v2) {
        BigDecimal vv1 = defValue(v1);
        BigDecimal vv2 = defValue(v2);
        return vv1.compareTo(vv2) > 0;
    }

    /**
     * 绝对大于 v1 大于 t1 为 true
     *
     * @param v1 比数
     * @param t1 被比数
     * @return boolean
     */
    public static boolean compareToBig(BigDecimal v1, int t1) {
        v1 = defValue(v1);
        return v1.compareTo(new BigDecimal(t1)) > 0;
    }

    /**
     * 比较大小  大于或等于返回true 小于返回false
     *
     * @param v1 数值1
     * @param t1 数值2
     * @return boolean
     */
    public static boolean compareTo(BigDecimal v1, int t1) {
        v1 = defValue(v1);
        return v1.compareTo(new BigDecimal(t1)) >= 0;
    }

    /**
     * 比较大小  大于或等于返回true 小于返回false
     *
     * @param b1 数值1
     * @param b2 数值2
     * @return boolean
     */
    public static boolean compareTo(BigDecimal b1, BigDecimal b2) {
        b1 = defValue(b1);
        b2 = defValue(b2);
        return b1.compareTo(b2) >= 0;
    }

    /**
     * 比较大小  大于1 等于0 小于-1
     *
     * @param v1 数值1
     * @param t1 数值2
     * @return int
     */
    public static int compare(BigDecimal v1, int t1) {
        v1 = defValue(v1);
        return v1.compareTo(new BigDecimal(t1));
    }

    /**
     * 把小数变成百分比数字(不会计算精度)
     *
     * @param v1 数字
     * @return 百分比数字
     */
    public static BigDecimal percent(BigDecimal v1) {
        return mul(v1, PERCENT_NUMBER);
    }

    /**
     * 把百分比变成小数
     *
     * @param v1 v1
     * @return BigDecimal
     */
    public static BigDecimal numToPercent(BigDecimal v1) {
        return div(v1, PERCENT_NUMBER);
    }

    /**
     * 把小数变成百分比数字，并自动保留2位小数
     *
     * @param v1 格式的对象
     * @return 格式后的值
     */
    public static BigDecimal percentScale(BigDecimal v1) {
        return precision(mul(v1, PERCENT_NUMBER));
    }

    /**
     * 把小数变成百分比数字 并保留小数位数
     *
     * @param v1    格式化对象
     * @param scale 精度
     * @return 格式后的值
     */
    public static BigDecimal percent(BigDecimal v1, int scale) {
        BigDecimal v2 = mul(v1, PERCENT_NUMBER);
        return precision(v2, scale);
    }

    /**
     * 取一个数字的相反数
     *
     * @param v1 数据
     * @return 结果
     */
    public static BigDecimal opposite(BigDecimal v1) {
        return mul(v1, OPPOSITE_NUMBER);
    }

    /**
     * 精度计算
     *
     * @param v1           数据
     * @param scale        精度
     * @param roundingMode 舍去的最大值
     * @return 结果
     */
    public static BigDecimal precision(BigDecimal v1, int scale, int roundingMode) {
        v1 = defValue(v1);
        return v1.setScale(scale, roundingMode);
    }

    /**
     * 精度计算(四舍五入)
     *
     * @param v1    数据
     * @param scale 精度(4以下舍去)
     * @return 结果
     */
    public static BigDecimal precision(BigDecimal v1, int scale) {
        return v1.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精度计算，默认2位小数
     *
     * @param v1 数据
     * @return 结果
     */
    public static BigDecimal precision(BigDecimal v1) {
        return v1.setScale(SCALE_DEF, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 默认值处理
     *
     * @param v1     原始值
     * @param defVal 默认值
     * @return 处理后的值
     */
    public static BigDecimal bigDecimalDefValue(BigDecimal v1, BigDecimal defVal) {
        if (null == defVal) {
            defVal = new BigDecimal(0);
        }
        if (v1 == null) {
            return defVal;
        }
        return v1;
    }
}
