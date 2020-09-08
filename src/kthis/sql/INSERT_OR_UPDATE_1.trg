CREATE OR REPLACE TRIGGER TG_AF_IU_PERSON_PERSONNAME
   AFTER INSERT OR UPDATE ON PERSON
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
DECLARE
   asBusinessId        EVENTSLOG.BUSINESS_ID%TYPE;
   
BEGIN
   IF (UPDATING) THEN
      IF :new.PERSON_NAME <> :OLD.PERSON_NAME OR (:new.PERSON_NAME IS NOT NULL AND :OLD.PERSON_NAME IS NULL)
         OR :new.FIRSTNAME_CN <> :old.FIRSTNAME_CN OR (:new.FIRSTNAME_CN IS NOT NULL AND :OLD.FIRSTNAME_CN IS NULL)
         OR :new.LASTNAME_CN <> :old.LASTNAME_CN OR (:new.LASTNAME_CN IS NOT NULL AND :OLD.LASTNAME_CN IS NULL)
         OR :new.FIRSTNAME <> :old.FIRSTNAME OR (:new.FIRSTNAME IS NOT NULL AND :OLD.FIRSTNAME IS NULL)
         OR :new.LASTNAME <> :old.LASTNAME OR (:new.LASTNAME IS NOT NULL AND :OLD.LASTNAME IS NULL) THEN
         --获取患者当前对应的事件最新历史记录
         BEGIN
            SELECT BUSINESS_ID
              INTO asBusinessId
              FROM (SELECT EL.BUSINESS_ID BUSINESS_ID
                      FROM PATIENT PT, EVENTSLOG EL
                     WHERE PT.PATIENT_ID = EL.PATIENT_ID
                       AND PT.PERSON_ID = :NEW.PERSON_ID
                     ORDER BY EL.CREATED_DATETIME DESC)
             WHERE ROWNUM = 1;
         EXCEPTION 
            WHEN no_data_found THEN
                SELECT BUSINESS_ID
                  INTO asBusinessId
                  FROM (SELECT EL.BUSINESS_ID BUSINESS_ID
                          FROM EVENTSLOG EL
                         WHERE TRUNC(EL.CREATED_DATETIME) = TRUNC(SYSDATE)
                         ORDER BY EL.CREATED_DATETIME DESC)
                 WHERE ROWNUM = 1;    
         END;
         INSERT INTO PERSONNAMEHISTORY_LS ( PERSONNAMEHISTORY_LS_ID,          person_name,            firstname_cn,
                                            lastname_cn,                      firstname,              LASTNAME,
                                            BUSINESS_ID,                      CREATED_BY,             CREATED_DATETIME,
                                            RECORD_NEW_IND,                   Person_Id
                                             ) VALUES
                                          ( PGSYSFUNC.fxGenPrimaryKey(),     :OLD.PERSON_NAME,        :OLD.FIRSTNAME_CN,
                                            :OLD.LASTNAME_CN,                :OLD.FIRSTNAME,          :OLD.LASTNAME,
                                            asBusinessId,                    :OLD.LAST_UPDATED_BY,    :OLD.LAST_UPDATED_DATETIME,
                                            'N',                             :OLD.PERSON_ID );   
         INSERT INTO PERSONNAMEHISTORY_LS ( PERSONNAMEHISTORY_LS_ID,          person_name,            firstname_cn,
                                            lastname_cn,                      firstname,              LASTNAME,
                                            BUSINESS_ID,                      CREATED_BY,             CREATED_DATETIME,
                                            RECORD_NEW_IND,                   Person_Id
                                             ) VALUES
                                          ( PGSYSFUNC.fxGenPrimaryKey(),     :NEW.PERSON_NAME,        :NEW.FIRSTNAME_CN,
                                            :NEW.LASTNAME_CN,                :NEW.FIRSTNAME,          :NEW.LASTNAME,
                                            asBusinessId,                    :NEW.LAST_UPDATED_BY,    :NEW.LAST_UPDATED_DATETIME,
                                            'Y',                             :NEW.PERSON_ID );
       END IF;
    END IF;
    IF ( INSERTING )  THEN
       INSERT INTO PERSONNAMEHISTORY_LS ( PERSONNAMEHISTORY_LS_ID,          person_name,            firstname_cn,
                                            lastname_cn,                      firstname,              LASTNAME,
                                            BUSINESS_ID,                      CREATED_BY,             CREATED_DATETIME,
                                            RECORD_NEW_IND,                   Person_Id
                                             ) VALUES
                                          ( PGSYSFUNC.fxGenPrimaryKey(),     :NEW.PERSON_NAME,        :NEW.FIRSTNAME_CN,
                                            :NEW.LASTNAME_CN,                :NEW.FIRSTNAME,          :NEW.LASTNAME,
                                            1,                               :NEW.LAST_UPDATED_BY,    :NEW.LAST_UPDATED_DATETIME,
                                            'Y',                             :NEW.PERSON_ID );
    END IF;
END TG_AF_IU_PERSON_PERSONNAME;
/
