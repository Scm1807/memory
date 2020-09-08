 PROCEDURE prOMEntryCheck (
       piCareproviderId           IN CAREPROVIDER.CAREPROVIDER_ID%TYPE,
       piSubSpecialtyMstrId       IN SUBSPECIALTYMSTR.SUBSPECIALTYMSTR_ID%TYPE,
       piOrderItemMstrIdStr       IN VARCHAR2,
       piLangType                 IN VARCHAR2,
       poCheckInfo                OUT VARCHAR2,
       poErrorCode                OUT VARCHAR2,
       poErrorCnMsg               OUT VARCHAR2,
       poErrorEnMsg               OUT VARCHAR2
   ) IS
      asCareProviderGrade          CAREPROVIDER.GRADE%TYPE;
      asOrderItemMstrId            ORDERENTRYITEM.ORDERENTRYITEM_ID%TYPE;
      asOrderItemMstrIdArray       pgCommon.Varchar2Array;
      asTxnCode                    TXNCODEMSTR.TXN_CODE%TYPE;
      asItemSubCatCode             ITEMSUBCATEGORYMSTR.ITEM_SUBCAT_CODE%TYPE;
      asItemCatCode                ITEMCATEGORYMSTR.ITEM_CAT_CODE%TYPE;
      asItemType                   ITEMCATEGORYMSTR.ITEM_TYPE%TYPE;
      asAnaestheticItemInd         DRUGMSTR.ANAESTHETIC_ITEM_IND%TYPE;
      asPoisonousItemInd           DRUGMSTR.POISONOUS_ITEM_IND%TYPE;
      asPsychotropicItemInd        DRUGMSTR.PSYCHOTROPIC_ITEM_IND%TYPE;
      asPsychotropicItemGrade      DRUGMSTR.PSYCHOTROPIC_ITEM_GRADE%TYPE;
      asAntibioticInd              DRUGMSTR.ANTIBIOTIC_IND%TYPE;
      asAntibioticGrade            DRUGMSTR.ANTIBIOTIC_GRADE%TYPE;
      asBloodProductInd            DRUGMSTR.BLOOD_PRODUCT_IND%TYPE;

      --asSurgeryType                ORDERITEMMSTR.SURGERY_TYPE%TYPE;
      asSurgeryLevel               ORDERITEMMSTR.SURGERY_LEVEL%TYPE;
      asSubSpecialtyDesc           SUBSPECIALTYMSTR.SUBSPECIALTY_DESC%TYPE;
      asTxnDesc                    TXNCODEMSTR.TXN_DESC%TYPE;
      asTxnDescLang1               TXNCODEMSTR.TXN_DESC_LANG1%TYPE;
      asTxnDescNormal              TXNCODEMSTR.TXN_DESC%TYPE;
      asItemSubCatDesc             ITEMSUBCATEGORYMSTR.ITEM_SUBCAT_DESC%TYPE;
      asItemSubCatDescLang1        ITEMSUBCATEGORYMSTR.ITEM_SUBCAT_DESC_LANG1%TYPE;
      asItemSubCatDescNormal       ITEMSUBCATEGORYMSTR.ITEM_SUBCAT_DESC%TYPE;
      asItemCatDesc                ITEMCATEGORYMSTR.ITEM_CAT_DESC%TYPE;
      asItemCatDescLang1           ITEMCATEGORYMSTR.ITEM_CAT_DESC_LANG1%TYPE;
      asItemCatDescNormal          ITEMCATEGORYMSTR.ITEM_CAT_DESC%TYPE;

      --处方权限表
      asUnAuthorisedItemId         UNAUTHORISEDITEM.UNAUTHORISEDITEM_ID%TYPE;
      asUnOrderLevel               UNAUTHORISEDITEM.ORDER_LEVEL%TYPE;
      asUnOrderLevelCode           UNAUTHORISEDITEM.ORDER_LEVEL_CODE%TYPE;
      asUnAnaestheticItemInd       UNAUTHORISEDITEM.ANAESTHETIC_ITEM_IND%TYPE;
      asUnPoisonousItemInd         UNAUTHORISEDITEM.POISONOUS_ITEM_IND%TYPE;
      asUnPsychotropicItemInd      UNAUTHORISEDITEM.PSYCHOTROPIC_ITEM_IND%TYPE;
      asUnPsychotropicItemGrade    UNAUTHORISEDITEM.PSYCHOTROPIC_ITEM_GRADE%TYPE;
      asUnAntibioticInd            UNAUTHORISEDITEM.ANTIBIOTIC_IND%TYPE;
      asUnAntibioticGrade          UNAUTHORISEDITEM.ANTIBIOTIC_GRADE%TYPE;
      asUnBloodProductInd          UNAUTHORISEDITEM.BLOOD_PRODUCT_IND%TYPE;
      asFormularyItemInd           UNAUTHORISEDITEM.FORMULARY_ITEM_IND%TYPE;  --处方标识 Added By LiangXin 2016-08-23
      asGestationalWeeksLit        UNAUTHORISEDITEM.GESTATIONAL_WEEKS%TYPE;      
      asUnSurgeryLevel             UNAUTHORISEDITEM.SURGERY_LEVEL%TYPE;
      asUnSubSpecialtyMstrId       UNAUTHORISEDITEMDETAIL.SUBSPECIALTYMSTR_ID%TYPE;
      asUnCareProviderGrade        UNAUTHORISEDITEMDETAIL.GRADE%TYPE;
      asUnCareproviderId           UNAUTHORISEDITEMDETAIL.CAREPROVIDER_ID%TYPE;
      asUnOrderLevelCodeDesc       VARCHAR2(300);

      --计数器
      asCount                      NUMBER;
      asErrorInfo                  VARCHAR2(4000);
      ERRORTYPE_UNAUTHORISED       VARCHAR2(20) := 'UNAUTHORISED';

      -- CAT = 'CLL' DESC = 'CHARGE LEVEL'
      CM_CHARGELEVEL_ITEMTYPE          CODEMSTR.CODE_ABBR%TYPE := 'CLLITY';
      CM_CHARGELEVEL_ITEMCATEGORY      CODEMSTR.CODE_ABBR%TYPE := 'CLLICT';
      CM_CHARGELEVEL_ITEMSUBCATEGORY   CODEMSTR.CODE_ABBR%TYPE := 'CLLISC';
      CM_CHARGELEVEL_ITEMCODE          CODEMSTR.CODE_ABBR%TYPE := 'CLLITC';

      -- CAT = 'LMT' DESC = 'LIMIT MODE TYPE'
      CM_LIMITMODE_LIMIT               CODEMSTR.CODE_ABBR%TYPE := 'LMT2';

   BEGIN
      -- get CareProvider Grade
      -- 读取医生级别
      SELECT CP.GRADE
        INTO asCareProviderGrade
        FROM CAREPROVIDER CP
       WHERE CP.CAREPROVIDER_ID = piCareproviderId;

      -- split OrderInfoStr
      -- 分解医嘱信息字符串
      asOrderItemMstrIdArray       := pgCommon.fxSplitStrToCollection(piOrderItemMstrIdStr, ',', asCount);     --Move Sequence for OP Order 2016-06-19

      -- check Unauthorised And DosageLimited
      -- 未授权检查和处方量限制
      FOR i IN 1..asCount LOOP
         IF asOrderItemMstrIdArray(i) IS NOT NULL THEN
            asOrderItemMstrId := TO_NUMBER(asOrderItemMstrIdArray(i));
         ELSE
            asOrderItemMstrId := NULL;
         END IF;

         -- get Item Info
         -- 读取项目信息
         SELECT TCM.TXN_CODE,
                ISCM.ITEM_SUBCAT_CODE,
                ICM.ITEM_CAT_CODE,
                ICM.ITEM_TYPE,
                TCM.TXN_DESC,
                TCM.TXN_DESC_LANG1,
                ISCM.ITEM_SUBCAT_DESC,
                ISCM.ITEM_SUBCAT_DESC_LANG1,
                ICM.ITEM_CAT_DESC,
                ICM.ITEM_CAT_DESC_LANG1,
                DM.ANAESTHETIC_ITEM_IND,
                DM.POISONOUS_ITEM_IND,
                DM.PSYCHOTROPIC_ITEM_IND,
                DM.PSYCHOTROPIC_ITEM_GRADE,
                DM.ANTIBIOTIC_IND,
                DM.ANTIBIOTIC_GRADE,
                DM.BLOOD_PRODUCT_IND,
                DM.FORMULARY_ITEM_IND,
                OIM.SURGERY_LEVEL
           INTO asTxnCode,
                asItemSubCatCode,
                asItemCatCode,
                asItemType,
                asTxnDesc,
                asTxnDescLang1,
                asItemSubCatDesc,
                asItemSubCatDescLang1,
                asItemCatDesc,
                asItemCatDescLang1,
                asAnaestheticItemInd,
                asPoisonousItemInd,
                asPsychotropicItemInd,
                asPsychotropicItemGrade,
                asAntibioticInd,
                asAntibioticGrade,
                asBloodProductInd,
                asFormularyItemInd,
                asSurgeryLevel
           FROM ORDERITEMMSTR OIM,
                TXNCODEMSTR TCM,
                ITEMSUBCATEGORYMSTR ISCM,
                ITEMCATEGORYMSTR ICM,
                DRUGMSTR DM,
                MATERIALITEMMSTR MIM,
                CHARGEITEMMSTR CIM
          WHERE OIM.ORDERITEMMSTR_ID = asOrderItemMstrId
            AND OIM.TXNCODEMSTR_ID = TCM.TXNCODEMSTR_ID
            AND OIM.ITEMSUBCATEGORYMSTR_ID = ISCM.ITEMSUBCATEGORYMSTR_ID
            AND ISCM.ITEMCATEGORYMSTR_ID = ICM.ITEMCATEGORYMSTR_ID
            AND OIM.MATERIALITEMMSTR_ID = DM.MATERIALITEMMSTR_ID(+)
            AND OIM.MATERIALITEMMSTR_ID = MIM.MATERIALITEMMSTR_ID(+)
            AND OIM.CHARGEITEMMSTR_ID = CIM.CHARGEITEMMSTR_ID(+);

         asTxnDescNormal := PGSYSFUNC.fxGetNormalDesc(piLangType, asTxnDesc, asTxnDescLang1);
         asItemSubCatDescNormal := PGSYSFUNC.fxGetNormalDesc(piLangType, asItemSubCatDesc, asItemSubCatDescLang1);
         asItemCatDescNormal := PGSYSFUNC.fxGetNormalDesc(piLangType, asItemCatDesc, asItemCatDescLang1);

         -- check Unauthorised
         -- get Unauthorised Item
         BEGIN
            SELECT UNITEM.UNAUTHORISEDITEM_ID,
                   UNITEM.ORDER_LEVEL,
                   UNITEM.ORDER_LEVEL_CODE,
                   UNITEM.ANAESTHETIC_ITEM_IND,
                   UNITEM.POISONOUS_ITEM_IND,
                   UNITEM.PSYCHOTROPIC_ITEM_IND,
                   UNITEM.PSYCHOTROPIC_ITEM_GRADE,
                   UNITEM.ANTIBIOTIC_IND,
                   UNITEM.ANTIBIOTIC_GRADE,
                   UNITEM.BLOOD_PRODUCT_IND,
                   UNITEM.FORMULARY_ITEM_IND,
                   UNITEM.SURGERY_LEVEL,
                   UNITEM.GESTATIONAL_WEEKS,
                   UNITEMDETAIL.SUBSPECIALTYMSTR_ID,
                   UNITEMDETAIL.GRADE,
                   UNITEMDETAIL.CAREPROVIDER_ID
              INTO asUnAuthorisedItemId,
                   asUnOrderLevel,
                   asUnOrderLevelCode,
                   asUnAnaestheticItemInd,
                   asUnPoisonousItemInd,
                   asUnPsychotropicItemInd,
                   asUnPsychotropicItemGrade,
                   asUnAntibioticInd,
                   asUnAntibioticGrade,
                   asUnBloodProductInd,
                   asFormularyItemInd,
                   asUnSurgeryLevel,
                   asGestationalWeeksLit,
                   asUnSubSpecialtyMstrId,
                   asUnCareProviderGrade,
                   asUnCareproviderId
              FROM ( SELECT UAI.UNAUTHORISEDITEM_ID,
                            UAI.ORDER_LEVEL,
                            UAI.ORDER_LEVEL_CODE,
                            UAI.ANAESTHETIC_ITEM_IND,
                            UAI.POISONOUS_ITEM_IND,
                            UAI.PSYCHOTROPIC_ITEM_IND,
                            UAI.PSYCHOTROPIC_ITEM_GRADE,
                            UAI.ANTIBIOTIC_IND,
                            UAI.ANTIBIOTIC_GRADE,
                            UAI.BLOOD_PRODUCT_IND,
                            UAI.FORMULARY_ITEM_IND,
                            UAI.SURGERY_LEVEL,
                            UAI.GESTATIONAL_WEEKS
                       FROM UNAUTHORISEDITEM UAI
                      WHERE ( ( UAI.ORDER_LEVEL IS NULL OR UAI.ORDER_LEVEL_CODE IS NULL ) OR
                              ( UAI.ORDER_LEVEL = CM_CHARGELEVEL_ITEMCODE AND UAI.ORDER_LEVEL_CODE = asTxnCode ) OR
                              ( UAI.ORDER_LEVEL = CM_CHARGELEVEL_ITEMSUBCATEGORY AND UAI.ORDER_LEVEL_CODE = asItemSubcatCode ) OR
                              ( UAI.ORDER_LEVEL = CM_CHARGELEVEL_ITEMCATEGORY AND UAI.ORDER_LEVEL_CODE = asItemCatCode ) OR
                              ( UAI.ORDER_LEVEL = CM_CHARGELEVEL_ITEMTYPE AND UAI.ORDER_LEVEL_CODE = asItemType )
                            )
                        AND ( ( UAI.ANAESTHETIC_ITEM_IND IS NULL ) OR
                              ( UAI.ANAESTHETIC_ITEM_IND = asAnaestheticItemInd )
                            )
                        AND ( ( UAI.POISONOUS_ITEM_IND IS NULL ) OR
                              ( UAI.POISONOUS_ITEM_IND = asPoisonousItemInd )
                            )
                        AND ( ( UAI.PSYCHOTROPIC_ITEM_IND IS NULL ) OR
                              ( UAI.PSYCHOTROPIC_ITEM_IND = asPsychotropicItemInd )
                            )
                        AND ( ( UAI.PSYCHOTROPIC_ITEM_GRADE IS NULL ) OR
                              ( UAI.PSYCHOTROPIC_ITEM_GRADE = asPsychotropicItemGrade )
                            )
                        AND ( ( UAI.ANTIBIOTIC_IND IS NULL ) OR
                              ( UAI.ANTIBIOTIC_IND = asAntibioticInd )
                            )
                        AND ( ( UAI.ANTIBIOTIC_GRADE IS NULL ) OR
                              ( UAI.ANTIBIOTIC_GRADE = asAntibioticGrade )
                            )
                        AND ( ( UAI.BLOOD_PRODUCT_IND IS NULL ) OR
                              ( UAI.BLOOD_PRODUCT_IND = asBloodProductInd )
                            )
                        AND ( ( UAI.FORMULARY_ITEM_IND IS NULL ) OR
                              ( UAI.FORMULARY_ITEM_IND = asFormularyItemInd )
                            )
                        AND ( ( UAI.SURGERY_LEVEL IS NULL ) OR
                              ( UAI.SURGERY_LEVEL = asSurgeryLevel )
                            )
                        AND ( ( UAI.GESTATIONAL_WEEKS IS NULL ) OR
                              ((UAI.GESTATIONAL_WEEKS*7) > asGesWeeksDayPt)
                            )
                        AND UAI.EFFECTIVE_DATE <= sysdate
                        AND ( UAI.EXPIRY_DATE >= sysdate OR UAI.EXPIRY_DATE IS NULL )
                        AND UAI.DEFUNCT_IND = 'N'
                   ) UNITEM,
                   ( SELECT UAID.UNAUTHORISEDITEM_ID,
                            UAID.SUBSPECIALTYMSTR_ID,
                            UAID.GRADE,
                            UAID.CAREPROVIDER_ID
                       FROM UNAUTHORISEDITEMDETAIL UAID
                      WHERE ( ( UAID.SUBSPECIALTYMSTR_ID IS NULL ) OR
                              ( UAID.SUBSPECIALTYMSTR_ID = piSubSpecialtyMstrId )
                            )
                        AND ( ( UAID.GRADE IS NULL ) OR
                              ( UAID.GRADE = asCareProviderGrade )
                            )
                        AND ( ( UAID.CAREPROVIDER_ID IS NULL ) OR
                              ( UAID.CAREPROVIDER_ID = piCareproviderId )
                            )
                        AND UAID.DEFUNCT_IND = 'N'
                        AND NOT EXISTS ( SELECT 1
                                           FROM AUTHORISEDITEMDETAIL AID
                                          WHERE AID.UNAUTHORISEDITEM_ID = UAID.UNAUTHORISEDITEM_ID
                                            AND ( ( AID.SUBSPECIALTYMSTR_ID IS NULL ) OR
                                                  ( AID.SUBSPECIALTYMSTR_ID = piSubSpecialtyMstrId )
                                                )
                                            AND ( ( AID.GRADE IS NULL ) OR
                                                  ( AID.GRADE = asCareProviderGrade )
                                                )
                                            AND ( ( AID.CAREPROVIDER_ID IS NULL ) OR
                                                  ( AID.CAREPROVIDER_ID = piCareproviderId )
                                                )
                                            AND AID.DEFUNCT_IND = 'N'
                                       )
                   ) UNITEMDETAIL
             WHERE UNITEM.UNAUTHORISEDITEM_ID = UNITEMDETAIL.UNAUTHORISEDITEM_ID
               AND ROWNUM = 1;
         EXCEPTION
            WHEN NO_DATA_FOUND THEN
               asUnAuthorisedItemId := NULL;
         END;

         IF asUnAuthorisedItemId IS NOT NULL THEN
            -- ErrorRowNumber
            IF poCheckInfo IS NULL THEN
               poCheckInfo := TO_CHAR(i - 1); -- begin form zero
            ELSE
               poCheckInfo := poCheckInfo || ';' || TO_CHAR(i - 1);
            END IF;
            -- ErrorType
            poCheckInfo := poCheckInfo || ',' || ERRORTYPE_UNAUTHORISED;
            -- ErrorInfo
            IF asUnCareproviderId IS NOT NULL THEN
               asErrorInfo := PGSYSFUNC.fxGetNormalText(piLangType,'您 ','you ');
            ELSE
               asErrorInfo := '';
               IF asUnCareProviderGrade IS NOT NULL THEN
                  asErrorInfo := pgCommon.fxGetCodeDesc(asUnCareProviderGrade,piLangType) || ' ';
               END IF;
               IF asUnSubSpecialtyMstrId IS NOT NULL THEN
                  SELECT pgsysfunc.fxGetNormalDesc(piLangType,SSPM.SUBSPECIALTY_DESC,SSPM.SUBSPECIALTY_DESC_LANG1)
                    INTO asSubSpecialtyDesc
                    FROM SUBSPECIALTYMSTR SSPM
                   WHERE SSPM.SUBSPECIALTYMSTR_ID = asUnSubSpecialtyMstrId;

                  asErrorInfo := asErrorInfo || asSubSpecialtyDesc || ' ';
               END IF;
            END IF;
            asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'没有开立 ','Have Not Authorised For ');

            IF asUnOrderLevel IS NOT NULL AND asUnOrderLevelCode IS NOT NULL THEN
               IF asUnOrderLevel = CM_CHARGELEVEL_ITEMTYPE THEN
                  asUnOrderLevelCodeDesc := pgCommon.fxGetCodeDesc(asUnOrderLevelCode,piLangType);
               ELSIF asUnOrderLevel = CM_CHARGELEVEL_ITEMCATEGORY THEN
                  asUnOrderLevelCodeDesc := asItemCatDescNormal;
               ELSIF asUnOrderLevel = CM_CHARGELEVEL_ITEMSUBCATEGORY THEN
                  asUnOrderLevelCodeDesc := asItemSubCatDescNormal;
               ELSE
                  asUnOrderLevelCodeDesc := asTxnDescNormal;
               END IF;
               asErrorInfo := asErrorInfo || asUnOrderLevelCodeDesc || ' ';
            END IF;

            IF asUnAnaestheticItemInd IS NOT NULL THEN
               asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'麻醉药品 ','anaesthetic drug ');
            END IF;

            IF asUnPoisonousItemInd IS NOT NULL THEN
               asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'有毒药品 ','poisonous drug ');
            END IF;

            IF asUnPsychotropicItemInd IS NOT NULL OR asUnPsychotropicItemGrade IS NOT NULL THEN
               IF asUnPsychotropicItemGrade IS NOT NULL THEN
                  asErrorInfo := asErrorInfo || pgCommon.fxGetCodeDesc(asUnPsychotropicItemGrade,piLangType) || ' ';
               ELSE
                  asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'精神类药品 ','psychotropic drug ');
               END IF;
            END IF;

            IF asUnAntibioticInd IS NOT NULL OR asUnAntibioticGrade IS NOT NULL THEN
               IF asUnAntibioticGrade IS NOT NULL THEN
                  asErrorInfo := asErrorInfo || pgCommon.fxGetCodeDesc(asUnAntibioticGrade,piLangType) || ' ';
               ELSE
                  asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'抗生素药品 ','antibiotic drug ');
               END IF;
            END IF;

            IF asUnBloodProductInd IS NOT NULL THEN
               asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'血液制品 ','blood product drug ');
            END IF;

            IF asFormularyItemInd IS NOT NULL THEN
               asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'处方药品 ','formulary product drug ');
            END IF;

            IF asGestationalWeeksLit IS NOT NULL AND asCheckGesWeeksInd = 'Y'
               AND asGesWeeksDayPt < (asGestationalWeeksLit*7) THEN
               asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'当前医嘱( 孕周在'||asGestationalWeeksLit||'周以上可开立) ','the order (Gestational Weeks need more than '||asGestationalWeeksLit||' weeks)');
            END IF;

            IF asUnSurgeryLevel IS NOT NULL THEN
               asErrorInfo := asErrorInfo || pgCommon.fxGetCodeDesc(asUnSurgeryLevel,piLangType) || ' ';
            END IF;

            asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,' 的权限！','!');

            asErrorInfo := asErrorInfo||PGSYSFUNC.fxGetNormalText(piLangType,'（禁止）','(Limited)');

            -- LimitMode
            poCheckInfo := poCheckInfo || ',' || asErrorInfo || ',' || CM_LIMITMODE_LIMIT;
         END IF;
         
         <<CONTINUE>>
         
         NULL;
      END LOOP;

      poErrorCode := NULL;
      poErrorCnMsg := NULL;
      poErrorEnMsg := NULL;
   EXCEPTION
      WHEN OTHERS THEN
         poErrorCode := sqlcode;
         poErrorCnMsg := 'prOMEntryCheck:' || sqlerrm;
         poErrorEnMsg := sqlerrm;
         dbms_output.put_line('prOMEntryCheck:' || poErrorCnMsg || '  ' || poErrorEnMsg || '  ' ||poErrorCode);
   END prOMEntryCheck;
