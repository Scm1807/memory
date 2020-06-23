package kthis;

public class CreateTableInfo {

    public static void main(String[] args) {

        String s = "YYZBDM\n" +
                "WSJGDM\n" +
                "MXXMBMYB\n" +
                "XMMC\n" +
                "YLJGDM\n" +
                "SFDW\n" +
                "SFDJ\n" +
                "SFXDLB\n" +
                "GZHCBZ\n" +
                "ZRCLBZ\n" +
                "SYBZ\n" +
                "YNZJBZ\n" +
                "TBSM\n" +
                "BZSM";

        String s1 = "医院自编代码\n" +
                "卫生机构（组织）代码\n" +
                "公示项目编码\n" +
                "项目名称\n" +
                "医疗机构代码\n" +
                "收费单位\n" +
                "收费单价\n" +
                "收费项目类别\n" +
                "高值耗材标志\n" +
                "植入材料标志\n" +
                "使用标志\n" +
                "院内自制标志\n" +
                "分类上或使用上的特别说明\n" +
                "备注说明";

        String s2 = "32\n" +
                "22\n" +
                "32\n" +
                "100\n" +
                "11\n" +
                "64\n" +
                "10,2\n" +
                "1\n" +
                "1\n" +
                "1\n" +
                "1\n" +
                "1\n" +
                "100\n" +
                "100";

        String[] strings = s.split("\n");
        System.out.println(strings.length);
        String[] strings1 = s1.split("\n");
        System.out.println(strings1.length);
        String[] strings2 = s2.split("\n");
        System.out.println(strings2.length);


//        System.out.println("\n==================属性==================");
//        for (String string : strings) {
//            System.out.println("private Text " + string.toLowerCase() + "T;");
//        }

        System.out.println("\n============== createText ================");
        for (int i = 0; i < strings1.length; i++) {
            // cyhT = createStrText(Messages.mf_healthcommission_TbDicMaterialMstrDialog_cyhT, 64);
            System.out.println(strings[i].toLowerCase()
                    + "T = createStrText(Messages.mf_healthcommission_TbDicMaterialMstrDialog_"
                    + strings[i].toLowerCase() + "T, "
                    + strings2[i] + ");");
        }


        System.out.println("\n============== message属性 ================");
        for (int i = 0; i < strings1.length; i++) {
            System.out.println("public static String mf_healthcommission_TbDicMaterialMstrDialog_"
                    + strings[i].toLowerCase() + "T;");
        }


        System.out.println("\n============== message 中文property ================");
        for (int i = 0; i < strings1.length; i++) {
            System.out.println("mf_healthcommission_TbDicMaterialMstrDialog_" + strings[i].toLowerCase()
                    + "T=" + strings1[i]);
        }


        System.out.println("\n============== message 英文property ================");
        for (int i = 0; i < strings1.length; i++) {
            System.out.println("mf_healthcommission_TbDicMaterialMstrDialog_" + strings[i].toLowerCase()
                    + "T=[EN]" + strings1[i]);
        }


        System.out.println("\n============== 数组国际化 ================");
        for (int i = 0; i < strings1.length; i++) {
            System.out.println("Messages.mf_healthcommission_TbDicMaterialMstrDialog_" + strings[i].toLowerCase() + "T,");
        }


//        System.out.println("\n============== fillControls ==============");
//        for (String string : strings) {
//            System.out.println("UIUtil.setValue("
//                    + string.toLowerCase() + "T, view.get"
//                    + string.substring(0, 1)
//                    + string.substring(1).toLowerCase() + "());");
//        }
//
//        System.out.println("\n============== buildView ==============");
//        for (String string : strings) {
//            System.out.println("bo.set" + string.substring(0, 1)
//                    + string.substring(1).toLowerCase() + "(UIUtil.getValue(" + string.toLowerCase() + "T));");
//        }
//
//        System.out.println("\n============== titles[] ==============");
//        for (String s3 : strings1) {
//            System.out.println("\"" + s3 + "\",");
//        }

    }

}
