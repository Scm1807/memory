package kthis;

/**
 * 创建接口相关的Issue所需的文件
 */
public class CreateInterfaceFile {

    public static void main(String[] args) {
        String names = "";


        printHas_NamesProperty(names);

        // 打印java属性
        createAttribute();
    }

    /**
     * 用于生成相关代码文件
     */
    private static void createAttribute() {

        String name = "";
        String type = "";

        String[] nameArr = name.split("\n");
        System.out.println("nameArr 长度: " + nameArr.length);

        String[] typeArr = type.split("\n");
        System.out.println("typeArr 长度: " + typeArr.length + "\n\n");

        for (int i = 0; i < nameArr.length; i++) {
            String string = "";
            if (typeArr[i] == null || typeArr[i].equals(" ")) {
                string = nameArr[i] + " ";
            } else if (typeArr[i].equals("S")) {
                string = "String ";
            } else if (typeArr[i].equals("F") || typeArr[i].equals("D") || typeArr[i].equals("I")) {
                string = "BigDecimal ";
            } else if (typeArr[i].equals("DT")) {
                string = "Timestamp ";
            }

            System.out.println("private " + string + nameArr[i] + ";");
            System.out.println("");
        }
    }

    /**
     * 打印带有 xxx_xxx 的属性
     */
    private static void printHas_NamesProperty(String str) {
        String[] names = str.split("\n");

        for (int i = 0; i < names.length; i++) {
            String node = names[i];
            String name = names[i];
            String setMedthod = "set";

            if (name.contains("_")) {
                String[] nameSplit = name.split("_");
                name = "";
                for (int j = 0; j < nameSplit.length; j++) {
                    if (j == 0) {
                        name += nameSplit[j];
                    } else {
                        name += firstUpper(nameSplit[j]);
                    }
                    setMedthod += firstUpper(nameSplit[j]);
                }
            } else {
                setMedthod += firstUpper(name);
            }

            // 打印resultMap
            System.out.println("<result property=\"" + name + "\" column=\"\"/>");

            // 打印属性
//            System.out.println("private String " + name + ";");

            // 打印set方法
//            System.out.println("item." + setMedthod + "(objectToStrng(view.get));");

            // 打印json
//            System.out.println("{ \"key\":\"" + name + "\",  \"path\":\"" + name + "\" },");

            // 打印xml标签:
//            System.out.println("<" + node + ">${item." + name + "}</" + node + ">");
        }
    }

    private static String firstUpper(String string) {
        return (string.toCharArray()[0] + "").toUpperCase() + string.substring(1);
    }

}

