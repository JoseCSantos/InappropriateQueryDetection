SET FULL_DATA=data_full\queries_scores.tsv
SET RAW_TRAIN=%FULL_DATA%.train
SET RAW_TEST=%FULL_DATA%.test
SET SVM_TRAIN=%RAW_TRAIN%.svm
SET SVM_TEST=%RAW_TEST%.svm
SET TRAIN_FEATS=%RAW_TRAIN%.feats
SET SVM_PREDS=%SVM_TEST%.preds
SET SVM_MODEL=%SVM_TRAIN%.model

REM split data in training and test
esample -s 2016 -n 64000 %FULL_DATA% -O %RAW_TEST% > %RAW_TRAIN%

REM extract features for train and convert data to svm format (same for test with train features)
run.sh -Doperation=extractFeatures io.FormatData %RAW_TRAIN% %TRAIN_FEATS%
run.sh -Doperation=svmFormat io.FormatData %TRAIN_FEATS% %RAW_TRAIN% %SVM_TRAIN%
run.sh -Doperation=svmFormat io.FormatData %TRAIN_FEATS% %RAW_TEST% %SVM_TEST%

REM learn a model
svm-train -c 0.3 -t 0 %SVM_TRAIN% %SVM_MODEL%
svm-predict %SVM_TEST% %SVM_MODEL% %SVM_PREDS% 
run.sh eval.Eval %SVM_TEST% %SVM_PREDS%
