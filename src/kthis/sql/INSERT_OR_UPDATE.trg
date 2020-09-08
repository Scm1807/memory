CREATE OR REPLACE TRIGGER TG_EVENTS_HERBAL
  AFTER INSERT OR UPDATE ON ORDERENTRY
  FOR EACH ROW
DECLARE
  /**
    Purpose:当产生草药处方记录、记录变更时写入到业务接口事件表.
    Created By: LiangXin
    Created Datetime: 2019-03-19 13:40:00
    Last_Updated By: LiangXin
    Last_Updated Datetime: 2019-03-19 13:40:00
    Requested By: LiangXin
   **/
   asCount                          NUMBER(2);
   asSum                            NUMBER(2);
   asEventsType                     BUSINESS_EVENTS.EVENTS_TYPE%TYPE := 'BVT77';
   asAdminStatus                    VISIT.ADMIT_STATUS%TYPE;
   asEntityMstrId                   VISIT.ENTITYMSTR_ID%TYPE;
   asOrderType                      ORDERENTRY.ORDER_TYPE%TYPE;
   asOrderStatus                    ORDERENTRY.ORDER_STATUS%TYPE;
   asOEOIMTxnCode                   TXNCODEMSTR.TXN_CODE%TYPE;

   CM_ADMINSTATUS_APPOINTMENT       VISIT.ADMIT_STATUS%TYPE := 'AST2'; --预约
   CM_ADMINSTATUS_APPCANCEL         VISIT.ADMIT_STATUS%TYPE := 'AST6'; --预约取消

   CM_ORDERSTATUS_ENT               ORDERENTRY.ORDER_STATUS%TYPE := 'OSTENT';    --录入
   CM_ORDERSTATUS_CAN               ORDERENTRY.ORDER_STATUS%TYPE := 'OSTCAN';    --取消
 
   CM_ORDERTYPE_OP                  ORDERENTRY.ORDER_TYPE%TYPE := 'ORT4';        --门诊医嘱
   CM_ORDERTYPE_PRE                 ORDERENTRY.ORDER_TYPE%TYPE := 'ORT8';        --预开医嘱

   PARAM_HERBAL                     PARAMETER.PARAMETER_NAME%TYPE := 'SPECIFIC_TXNCODE_HERBAL'; --草药处方
   asParamHealblTxnCode             PARAMETER.VALUE%TYPE;


BEGIN
   --- 预约相关状态不处理
   BEGIN
      SELECT V.ADMIT_STATUS,   TCM.TXN_CODE,    V.ENTITYMSTR_ID
        INTO asAdminStatus,    asOEOIMTxnCode,  asEntityMstrId
        FROM VISIT          V,
             ORDERITEMMSTR  OIM,
             TXNCODEMSTR    TCM
       WHERE V.VISIT_ID = :NEW.VISIT_ID
         AND OIM.ORDERITEMMSTR_ID = :NEW.ORDERITEMMSTR_ID
         AND TCM.TXNCODEMSTR_ID = OIM.TXNCODEMSTR_ID;
   EXCEPTION
      WHEN OTHERS THEN
         RETURN;
   END;

   IF asAdminStatus IS NOT NULL
      AND (  asAdminStatus = CM_ADMINSTATUS_APPOINTMENT
          OR asAdminStatus = CM_ADMINSTATUS_APPCANCEL ) THEN
      RETURN ;
   END IF;
   --- 跳过暂存医嘱、预开医嘱,取消医嘱
   IF   (:NEW.ORDER_TYPE = CM_ORDERTYPE_OP AND :NEW.ORDER_STATUS = CM_ORDERSTATUS_ENT)
      OR :NEW.ORDER_TYPE = CM_ORDERTYPE_PRE 
      OR :NEW.ORDER_STATUS = CM_ORDERSTATUS_CAN THEN
      RETURN ;
   END IF;
   -- 判断当前是否是草药处方
   asParamHealblTxnCode := PGCOMMON.fxGetParameterValue(PARAM_HERBAL,NULL,asEntityMstrId);
   IF asParamHealblTxnCode IS NULL OR  
     (asParamHealblTxnCode IS NOT NULL AND asOEOIMTxnCode <> asParamHealblTxnCode ) THEN
     RETURN;
   END IF;   
   ---判断是否已经写入,如果已经写入，不再写入
   SELECT COUNT(1)
     INTO asSum
     FROM CODEMSTR CM
    WHERE CM.CODE_CAT||CM.CODE_ABBR = asEventsType
      AND CM.DEFUNCT_IND            = 'N';
   IF asSum > 0 THEN
      SELECT COUNT(1)
        INTO asCount
        FROM BUSINESS_EVENTS EVE
       WHERE EVE.EVENTS_TYPE = asEventsType
         AND EVE.SUCCEED_IND = 'N'
         AND EVE.DEFUNCT_IND = 'N'
         AND EVE.BUSINESS_ID = TO_CHAR(:NEW.ORDERENTRY_ID);
      IF asCount = 0 THEN
         INSERT INTO BUSINESS_EVENTS
           (BUSINESS_EVENTS_ID,           EVENTS_TYPE,          BUSINESS_ID,         SUCCEED_IND,
            BUSINESS_TABLE,               TABLE_COL,            OPERATE_TYPE,        ORDER_STATUS,
            DEFUNCT_IND,                  CREATED_BY,           CREATED_DATETIME)
         VALUES
           (pgSysFunc.fxGenPrimaryKey(),  asEventsType,         :NEW.ORDERENTRY_ID,  'N',
           'ORDERENTRY',                  'ORDERENTRY_ID',      NULL,                :NEW.ORDER_STATUS,
           'N',                           :NEW.LAST_UPDATED_BY, SYSDATE);
      END IF;
   END IF;
END TG_EVENTS_HERBAL;
/
