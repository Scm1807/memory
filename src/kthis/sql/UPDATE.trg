CREATE OR REPLACE TRIGGER TG_LOG_ORDERENTRY_INTERVENTION
   AFTER UPDATE ON ORDERENTRY
   FOR EACH ROW
DECLARE
   /**
    Purpose: 干预医嘱时写入操作日志.
    Created By: LiangXin
    Created Datetime: 2018-10-08 10:30
    Last_Updated By: LiangXin
    Last_Updated Datetime: 2018-10-31 10:30
    Requested By: LiangXin
   **/

   asCurrentDatetime                DATE;
   asInterventionRole               ORDERINTERVENTIONLOG.INTERVENTION_ROLE%TYPE;
   asInterventionOutcome            ORDERINTERVENTIONLOG.INTERVENTION_OUTCOME%TYPE;
   asInterventionType               ORDERINTERVENTIONLOG.INTERVENTION_TYPE%TYPE;
   asInterventionRemarks            ORDERINTERVENTIONLOG.INTERVENTION_REMARKS%TYPE;
   asIntervenedBy                   ORDERINTERVENTIONLOG.INTERVENED_BY%TYPE;
   asIntervenedDatetime             ORDERINTERVENTIONLOG.INTERVENED_DATETIME%TYPE;
   

   CM_OPERATORROLE_PHA              CODEMSTR.CODE_ABBR%TYPE := 'CPR5';  --药剂师
   CM_OPERATORROLE_DOC              CODEMSTR.CODE_ABBR%TYPE := 'CPR1';  --医生

BEGIN
   SELECT SYSDATE INTO asCurrentDatetime FROM DUAL;
   asInterventionOutcome := :NEW.INTERVENTION_OUTCOME;
   ---1.药房：干预医嘱
   ---2.门诊医生站：医嘱内容调整
   ---3.医生站：拒绝干预
   ---4.医生站：作废/停止干预医嘱
   IF    ( :NEW.INTERVENTION_OUTCOME IS NULL AND :NEW.INTERVENTION_IND = 'Y' 
      AND (:NEW.ORDER_STATUS = 'OSTCON' OR :NEW.ORDER_STATUS = 'OSTORD') )
      OR (:OLD.INTERVENTION_IND = 'N' AND :NEW.INTERVENTION_IND = 'Y') 
      OR (:OLD.INTERVENED_DATETIME <> :NEW.INTERVENED_DATETIME) THEN
      asInterventionRole := CM_OPERATORROLE_PHA;  --药房干预
      asInterventionOutcome := NULL;
      asInterventionType := :NEW.INTERVENTION_TYPE;
      asInterventionRemarks := :NEW.INTERVENTION_REMARKS;      
      asIntervenedBy := :NEW.INTERVENED_BY;
      asIntervenedDatetime := :NEW.INTERVENED_DATETIME;      
   ELSIF :OLD.INTERVENTION_IND = 'Y' AND :NEW.INTERVENTION_IND = 'N' THEN
      asInterventionRole := CM_OPERATORROLE_PHA; 
      asInterventionOutcome := 'IOTCI';   --取消干预
      asInterventionType := NULL;
      asInterventionRemarks := NULL;      
      asIntervenedBy := NULL;
      asIntervenedDatetime := NULL;  
   ELSIF   :OLD.INTERVENTION_OUTCOME IS NULL
      AND (:NEW.INTERVENTION_OUTCOME = 'IOT2' OR :NEW.INTERVENTION_OUTCOME = 'IOT1') THEN
      asInterventionRole := CM_OPERATORROLE_DOC;
      asInterventionOutcome := :NEW.INTERVENTION_OUTCOME;     --拒绝干预
      asInterventionType := NULL;
      asInterventionRemarks := NULL;      
      asIntervenedBy := NULL;
      asIntervenedDatetime := NULL; 
   ELSIF   /*:NEW.INTERVENTION_OUTCOME IS NULL AND*/ :NEW.INTERVENTION_IND = 'Y'
      AND (:NEW.ORDER_STATUS = 'OSTCAN' OR 
          (:NEW.ORDER_STATUS = 'OSTSTO' AND :OLD.ORDER_STATUS <> 'OSTSTO') OR
          (:NEW.END_DATETIME <> :OLD.END_DATETIME)) THEN
      asInterventionRole := CM_OPERATORROLE_DOC;
      asInterventionOutcome := 'IOTCO';   --作废干预医嘱
      asInterventionType := NULL;
      asInterventionRemarks := :NEW.STOP_REMARKS;      
      asIntervenedBy := NULL;
      asIntervenedDatetime := NULL; 

   ELSE
       asInterventionRole := NULL;
       RETURN;
   END IF;

   IF asInterventionRole IS NOT NULL THEN

      BEGIN
         INSERT INTO ORDERINTERVENTIONLOG
            (ORDERINTERVENTIONLOG_ID,      ORDERENTRY_ID,              INTERVENTION_ROLE,    INTERVENTION_TYPE,
             INTERVENTION_OUTCOME,         INTERVENTION_REMARKS,       INTERVENED_BY,        INTERVENED_DATETIME,
             CREATED_BY,                   CREATED_DATETIME)
         SELECT
             PGSYSFUNC.fxGenPrimaryKey(),  :NEW.ORDERENTRY_ID,         asInterventionRole,   asInterventionType,
             asInterventionOutcome,        asInterventionRemarks,      asIntervenedBy,       asIntervenedDatetime,
             :NEW.LAST_UPDATED_BY,         asCurrentDatetime 
       FROM  DUAL
       WHERE NOT EXISTS
          (
             SELECT 1 FROM ORDERINTERVENTIONLOG OIL 
              WHERE OIL.ORDERENTRY_ID = :NEW.ORDERENTRY_ID
                --AND OIL.INTERVENTION_ROLE = asInterventionRole
                --AND OIL.INTERVENTION_TYPE = asInterventionType
                AND OIL.INTERVENTION_OUTCOME = 'IOTCO' --asInterventionOutcome 作废医嘱
                --AND OIL.INTERVENTION_REMARKS = asInterventionRemarks
                AND OIL.INTERVENED_BY = asIntervenedBy
                AND OIL.INTERVENED_DATETIME = asIntervenedDatetime
                AND OIL.CREATED_BY = :NEW.LAST_UPDATED_BY
                --AND OIL.CREATED_DATETIME = asCurrentDatetime                
          );
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            RETURN;
      END;

   END IF;

END;
/
