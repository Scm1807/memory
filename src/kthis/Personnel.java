package kthis;

/**
 * Java 语言: 枚举
 *
 * @author sx
 * @date 2018/10/25
 */
@SuppressWarnings("unused")
public enum Personnel {
    // 下面的顺序就是每个人（enum）的ordinal，ordinal默认从0开始，
    // 每个人的名字就是enum中的name属性，所以personnel里面不需要继续定义name属性
    小明("男", 22, 88),//ordinal = 0
    小蓝("男", 22, 77),//ordinal = 1
    小白("女", 22, 44),//ordinal = 2
    小红("女", 11, 55) {//ordinal = 3

        @Override
        public boolean isPass() {
            System.out.println("小红的成绩通过方法重写，输出错误的成绩结果。");
            return true;
        }
    };

    private String sex;
    private int age;
    private int score;

    /**
     * 构造
     */
    Personnel(String sex, int age, int score) {
        this.sex = sex;
        this.age = age;
        this.score = score;
    }

    /**
     * 用来判断是否通过考试
     */
    public boolean isPass() {
        return score - 60 >= 0;
    }

    @Override
    public String toString() {
        return this.name() + "  性别:" + this.getSex() + "  年龄:" + this.getAge() + "  成绩为:" + this.getScore();
    }

    /**
     * 调出简介
     */
    public void callResume() {
        Personnel personnel = this;
        System.out.println("----------------开始调用简历信息----------------");
        System.out.println(this.name() + "的简介为:");
        System.out.println(this.toString());
        System.out.println("是否通过了考试： " + this.isPass());
        System.out.println("----------------调用简历信息结束----------------");
    }

    /**
     * 本人与其他同学比较成绩
     *
     * @param otherPersonnel
     * @return
     */
    public String compareScore(Personnel otherPersonnel) {
        int myScore = this.getScore();
        int otherScore = otherPersonnel.getScore();
        String myName = this.name();
        String otherName = otherPersonnel.name();
        return myScore > otherScore ? myName + "比" + otherName + "高" + (myScore - otherScore) + "分" :
                (myScore < otherScore ? myName + "比" + otherName + "低" + (otherScore - myScore) + "分" :
                        myName + "与" + otherName + "成绩相同");
    }

    /**
     * 比较学号大小
     *
     * @param personnel
     * @return
     */
    public String compareStudentNumber(Personnel personnel) {
        String myName = this.name();
        String otherName = personnel.name();
        String tipStuNumber;
        //compareTo返回负整数(小于) 零(登录) 正整数(大于)  由 myName.ordinal - otherName.ordinal产生
        if (this.compareTo(personnel) > 0) {
            tipStuNumber = myName + "的学号在" + otherName + "之后";
        } else if (this.compareTo(personnel) < 0) {
            tipStuNumber = myName + "的学号在" + otherName + "之前";
        } else {
            tipStuNumber = "两个人的学号相同";
        }
        return myName + "的学号为:" + this.ordinal() + "   " + otherName + "的学号为:" + personnel.ordinal() + "\n" + tipStuNumber;
    }

    public static void main(String[] args) {
        // 手工读取信息，
        for (Personnel personnel : Personnel.values()) {
            System.out.println(personnel.name() + "的简介为:");
            System.out.println(personnel.toString());
            System.out.println("是否通过了考试:" + personnel.isPass());
            System.out.println("----------------------");
        }
        // 调用小明的简历
        Personnel.小明.callResume();
        // 比较小红和小明的成绩
        System.out.println(Personnel.小红.compareScore(Personnel.小明));
        // 比较小明和小蓝色的学号
        System.out.println(Personnel.小明.compareStudentNumber(Personnel.小蓝));
        System.out.println(Personnel.小白.compareStudentNumber(Personnel.小红));
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}