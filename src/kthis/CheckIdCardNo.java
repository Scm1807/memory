package kthis;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CheckIdCardNo {
    /**
     * 身份证号码验证结果 :: 正确
     */
    private final static String CHECK_RESULT_CORRECT = "CHECK_RESULT_CORRECT";
    /**
     * 身份证号码验证结果 :: 证件号码为null
     */
    private final static String CHECK_RESULT_NULL = "CHECK_RESULT_NULL";
    /**
     * 身份证号码验证结果 :: 证件号码长度错误
     */
    private final static String CHECK_RESULT_LENGTH_ERROR = "CHECK_RESULT_LENGTH_ERROR";
    /**
     * 身份证号码验证结果 :: 非法字符
     */
    private final static String CHECK_RESULT_ILLEGAL_CHAR = "CHECK_RESULT_ILLEGAL_CHAR";
    /**
     * 身份证号码验证结果 :: 出生日期错误
     */
    private final static String CHECK_RESULT_BIRTH_ERROR = "CHECK_RESULT_BIRTH_ERROR";
    /**
     * 身份证号码验证结果 :: 18位身份证号码效验错误
     */
    private final static String CHECK_RESULT_CHECK_ERROR = "CHECK_RESULT_CHECK_ERROR";
    /**
     * <p>Description: 旧身份证号码长度</p>
     */
    private final static int IDNO_LENGTH_OLD = 15;
    /**
     * <p>Description: 新身份证号码长度</p>
     */
    private final static int IDNO_LENGTH_NEW = 18;

    /**
     * <p>Description: 判断arg是否是身份证号码</p>
     *
     * @param idCardNo 身份证号码
     * @return s
     */
    private String isIdNo(String idCardNo) {
        // 证件号码为null
        if (idCardNo == null)
            return CHECK_RESULT_NULL;
        if ("".equals(idCardNo.trim())) {
            return CHECK_RESULT_NULL;
        }
        // 证件号码长度错误
        int idCardNoLen;
        idCardNoLen = idCardNo.length();
        if (idCardNoLen != IDNO_LENGTH_OLD && idCardNoLen != IDNO_LENGTH_NEW)
            return CHECK_RESULT_LENGTH_ERROR;
        // 非法字符
        Pattern p = Pattern.compile("[0-9]+([0-9]|X|Ⅹ)");
        Matcher m = p.matcher(idCardNo);
        if (!m.matches())
            return CHECK_RESULT_ILLEGAL_CHAR;
        // 新旧身份证判断
        if (idCardNoLen == IDNO_LENGTH_OLD) {
            // 15位
            // 生日判断
            String birthStr = "19" + idCardNo.substring(6, 12);
            if (!this.checkBirth(birthStr))
                return CHECK_RESULT_BIRTH_ERROR;
            // 15位正确
            return CHECK_RESULT_CORRECT;
        } else {
            // 18位
            String idCardNoChar[] = new String[18];
            for (int i = 0; i < idCardNoLen; i++) {
                idCardNoChar[i] = idCardNo.substring(i, i + 1);
            }
            // 生日判断
            String birthStr = idCardNo.substring(6, 14);
            if (!this.checkBirth(birthStr))
                return CHECK_RESULT_BIRTH_ERROR;
            // 号码效验
            int numCheck = 0;
            String[] S = {String.valueOf(1), String.valueOf(0), String.valueOf("X"), String.valueOf(9), String.valueOf(8), String.valueOf(7),
                    String.valueOf(6), String.valueOf(5), String.valueOf(4), String.valueOf(3), String.valueOf(2)};
            int[] N = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
            for (int i = 0; i < 17; i++) {
                numCheck += Integer.parseInt(idCardNoChar[i]) * N[i];

            }
            String lastValue = idCardNoChar[idCardNoLen - 1];
            if (lastValue.equalsIgnoreCase("Ⅹ")) {
                lastValue = "X";
            }
            if (!lastValue.equals(S[numCheck % 11])) {
                return CHECK_RESULT_CHECK_ERROR;
            }
            // 18位正确
            return CHECK_RESULT_CORRECT;
        }
    }

    /**
     * 检查生日
     */
    private boolean checkBirth(String birth) {
        if (birth == null || birth.equals("") || birth.length() != 8)
            return false;
        try {
            int n = Integer.parseInt(birth.substring(0, 4));
            int y = Integer.parseInt(birth.substring(4, 6));
            int r = Integer.parseInt(birth.substring(6, 8));
            return checkDate(n, y, r);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 检查日期
     */
    private boolean checkDate(int year, int month, int day) {
        //检查年
        if ((year < 1800) || (year > 2200))
            return false;
        //检查月
        if ((month < 1) || (month > 12))
            return false;
        //检查日
        if (day < 1)
            return false;
        if (month == 4 || month == 6 || month == 9 || month == 11) {//小月
            return day <= 30;
        } else if (month == 2) {//2月
            if (year % 100 != 0 && year % 4 == 0 || year % 400 == 0) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        } else {//大月
            return day <= 31;
        }
        // 正确
    }

    private void checkIdCardNo(String idNoStr) {
        String result = isIdNo(idNoStr);
        switch (result) {
            case CHECK_RESULT_NULL:
                System.out.println("身份证号码为空!");
                break;
            case CHECK_RESULT_LENGTH_ERROR:
                System.out.println("身份证号码长度错误!");
                break;
            case CHECK_RESULT_ILLEGAL_CHAR:
                System.out.println("身份证号码中存在非法字符!");
                break;
            case CHECK_RESULT_BIRTH_ERROR:
                System.out.println("身份证号码中出生日期错误!");
                break;
            case CHECK_RESULT_CHECK_ERROR:
                System.out.println("不存在此身份证号码!");
                break;
            default:
                System.out.println("身份证号码正确!");
                break;
        }
    }

    public static void main(String[] args) {
        String idNo = "1111111111111";
        CheckIdCardNo c = new CheckIdCardNo();
        c.checkIdCardNo(idNo);
    }

}
