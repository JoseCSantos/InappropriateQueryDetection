"# InappropriateQueryDetection" 


Assuming the raw data exists in the data_full directory with one query per line with format:

<query>\t<score>

where a query is considered offensive if the score is 0.2 or higher.


Requirements
============

- To run this programs you will need following things.

1. Java >=1.6
2. Ant (to compile)
3. libSVM to run (svm-train and svm-predict) commands
4. etools for sampling tsv file


Step:1 Extract features
=======================

Use train file to extract features. The command is

sh run.sh io.FormatData


This will generate following usage output.
---------
Usage: sh run.sh -Doperation=extractFeatures io.FormatData <input-data> <feature-map-output>

OR

Usage: sh run.sh -Doperation=svmFormat io.FormatData <input-feature-map> <input-data> <svm-data-output>
----------


input-data  - This is raw data explained above
feature-map-output  - Feature map will be outputted here in <id>\t<feature> format


input-feature-map - This is the same feature map file acting as input
svm-data-output - output file where the svm data will be stored

Step:2 Generate SVM data
========================

Once the features are extracted using Step:1 and feature map file is generated. Convert train and test file in SVM format by running following command:

sh run.sh -Doperation=svmFormat eval.Eval <input-feature-map> <input-data> <svm-data-output>


Step:3 Train SVM
================

Use the generated train data to train svm using libsvm. Issue following command,

./svm-train -c 0 -t 0 <train-file> <model-file>

Step:4 Do SVM predictions
==========================

Classify test using the trained model using following command:

./svm-predict <test-file> <model-file> <output-file>

Step:5 Evaluate
===============

Issue following command to generate P, R and F1 scores.

sh run.sh eval.Eval

This will generate output:
sh run.sh eval.Eval <svm-test-gold> <svm-pred-output>

<svm-test-gold> the same file which was used in Step:4 as <test-file>
<svm-pred-output> the same file which was used in Step:4 as <output-file> 


Step:6 Tune parameters
======================

If interested try different cost "c" from 0.1 to 1.0 with intermediate values such as (0.1, 0.3, 0.5, 0.7, 0.9)

