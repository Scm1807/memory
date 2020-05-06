package com.company;
/**
 * Personnel class
 *
 * @author TransientBa
 * @date 2018/2/28
 */
public enum Personnel {
    小明("男",22,86),
    小红("女",22,55){
        @Override
        public boolean isPass(){
            return false;
        }
    },
    小蓝("男",23,73);
    public String name;
    private String sex;
    private int age;
    private int result;
    /* 构造 */
    private Personnel(String sex,int age,int result){
        this.sex = sex;
        this.age = age;
        this.result = result;
    }

    @Override
    public String toString() {
        return this.name()+"  性别:"+this.getSex() + "  年龄:"+this.getAge()+"  成绩为:"+this.getResult();
    }

    /**
     * 调出简介
     *
     * @return
     */
    public String callResume(){
        Personnel personnel = this;
        switch (personnel){
            case 小明:
                System.out.println(this.name()+"的简介为:");
                System.out.println(this.toString());
                System.out.println("是否通过了考试： "+this.isPass());
                break;
            case 小红:
                System.out.println(this.name()+"的简介为:");
                System.out.println(this.toString());
                System.out.println("是否通过了考试： "+this.isPass());
                break;
            default:
                System.out.println(this.name()+"的简介为:");
                System.out.println(this.toString());
                System.out.println("是否通过了考试： "+this.isPass());
                break;
        }
        System.out.println("--- --- --- --- --- --- ---");
        return "";
    }

    /**
     * 比较成绩
     * @param otherPersonnel
     * @return
     */
    public String compareResult(Personnel otherPersonnel){
        int pr1 = this.getResult();
        int pr2 = otherPersonnel.getResult();
        String pn1 = this.name();
        String pn2 = otherPersonnel.name();
        return pr1 > pr2 ? pn1+"比"+pn2+"高"+(pr1-pr2)+"分":(pr1 < pr2 ? pn1+"比"+pn2+"低"+(pr2-pr1)+"分":pn1+"与"+pn2+"成绩相同");
    }

    public String compareStudentNumber(Personnel personnel){
        String pn1 = this.name();
        String pn2 = personnel.name();
        String tipStuNumber = "";
        //compareTo返回负整数(小于) 零(登录) 正整数(大于)  由 pn1.ordinal - pn2.ordinal产生
        if(this.compareTo(personnel) >0){
            tipStuNumber = pn1+"的学号在"+pn2+"之后";
        }else if(this.compareTo(personnel) < 0){
            tipStuNumber = pn1+"的学号在"+pn2+"之前";
        }else {
            tipStuNumber = "两个人的学号相同";
        }
        return pn1+"的学号为:"+((int)this.ordinal()+1)+"   "+pn2+"的学号为:"+((int)personnel.ordinal()+1)+"\n"+tipStuNumber;
    }

    public static void main(String[] args) {
//        for(Personnel personnel:Personnel.values()){
//                    System.out.println(personnel.name()+"的简介为:");
//                    System.out.println(personnel.toString());
//                    System.out.println("是否通过了考试:" + personnel.isPass());
//            System.out.println("----------------------");
//        }
        Personnel.小明.callResume();
        System.out.println(Personnel.小红.compareResult(Personnel.小明));
        System.out.println(Personnel.小明.compareStudentNumber(Personnel.小蓝));
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

    public boolean isPass(){
        return true;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
