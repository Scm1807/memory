package kthis;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;

/**
 * 已上传
 * 自动生成mybatis/ibatis中resultMap结果集，
 * 属性必须有javax.persistence.Column注解，对应数据库字段。
 */
public class CreateResultMap {

    public static void main(String[] args) {
        List<ResultMapModel> result = getResultMapModel(CaBlackList.class); // 要转换的类class

        int level = 0; // 区分子父类
        for (ResultMapModel mapModel : result) {
            if (level == mapModel.level) {
                System.out.println("========================= Level " + (level++) + "===========================");
            }
            System.out.println("<result column=\"" + mapModel.column
                    + "\" property=\"" + mapModel.attribute
                    //+ "\" jdbcType=\"" + mapModel.jdbcType
                    + "\"/>");
        }
    }

    private static List<ResultMapModel> getResultMapModel(Class clazz) {
        Field[] fields;
        List<ResultMapModel> results = new ArrayList<>();
        int count = 0;
        // 通过反射查询其父类
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            // 获取到该类下所有的属性
            fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // 找到有Column注解的字段
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    ResultMapModel result = new ResultMapModel();
                    // java字段类型
                    Class<?> type = field.getType();
                    if (type == String.class) {
                        result.jdbcType = "VARCHAR";
                    } else if (type == BigDecimal.class || type == Integer.class) {
                        result.jdbcType = "DECIMAL";
                    } else if (type == Timestamp.class) {
                        result.jdbcType = "TIMESTAMP";
                    }
                    // java字段名
                    result.attribute = field.getName();
                    result.column = column.name();
                    result.level = count;
                    results.add(result);
                }
            }
            count++;
        }
        return results;
    }

    private static class ResultMapModel {
        private String attribute; //entity中属性的名称
        private String column;  //表中的字段名
        private String jdbcType;  //jdbctype类型
        private int level;  //所在类的级别，最基础为0，父类为1，祖父类2.。。以此类推
    }

    public final class CaBlackList {
        @Column(name = "PES_SUBSPECONCLUSION_ID")
        private BigDecimal pesSubspeConclusionId;
        @Column(name = "PES_SUBSPECIALTYEXAM_ID")
        private BigDecimal pesSubspecialtyExamId;
        @Column(name = "PES_CONCLUSIONMSTR_ID")
        private BigDecimal pesConclusionMstrId;
        @Column(name = "CONCLUSION_CODE")
        private String conclusionCode;
        @Column(name = "CONCLUSION_DESC")
        private String conclusionDesc;
        @Column(name = "CONCLUSION_DESC_LANG1")
        private String conclusionDescLang1;
        @Column(name = "ADVICE")
        private String advice;
        @Column(name = "SEQ_NO")
        private BigDecimal seqNo;
        @Column(name = "REMARKS")
        private String remarks;
        @Column(name = "DEFUNCT_IND")
        private String defunctInd;
        @Column(name = "CREATED_BY")
        private BigDecimal createdBy;
        @Column(name = "CREATED_DATETIME")
        private Timestamp createdDatetime;
        @Column(name = "LAST_UPDATED_BY")
        private BigDecimal lastUpdatedBy;
        @Column(name = "LAST_UPDATED_DATETIME")
        private Timestamp lastUpdatedDatetime;
    }
}
