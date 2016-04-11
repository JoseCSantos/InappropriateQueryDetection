# -*- coding: utf-8 -*-
"""
Created on Sun Apr 10 11:58:53 2016

@author: parth
"""
import numpy as np

import sys as sys
#from sys import argv
from sklearn.datasets import load_svmlight_file
from sklearn import ensemble

if(len(sys.argv)==3):
    train_data = sys.argv[1]
    test_data = sys.argv[2]
else:
    print sys.argv[1]
    print sys.argv[2]
    sys.exit("Usage: python ensemble-learn.py <trainFile> <testFile>")
#train_data = r"C:/Users/pargu_000/Dropbox/Shared with Parth/train-dssm.only2.svm";
#test_data = r"C:/Users/pargu_000/Dropbox/Shared with Parth/test-dssm.only2.svm";

trainX, trainY = load_svmlight_file(train_data);
testX, testY = load_svmlight_file(test_data);

train_set = trainX.toarray();
test_set = testX.toarray();

trainY = [int(round(trainY[i])) for i in xrange(len(trainY))]
testY  = [int(round(testY[i] )) for i in xrange(len(testY))]


num_estimators=20

params_ETR = { 'n_estimators' : num_estimators,
               'max_depth' : 5,
               'min_samples_split' : 5,
               'criterion' : 'gini',
               #'criterion' : 'entropy',
               'bootstrap' : False,
               'n_jobs' : -1 }

ensemble_extra_trees_classifier = ensemble.ExtraTreesClassifier( **params_ETR )
print ensemble_extra_trees_classifier


clf = ensemble_extra_trees_classifier
clf.fit(train_set, trainY)
y_predicted = clf.predict( train_set )
score_train = clf.score( train_set, trainY )
#score_train = metrics.r2_score( y_train, y_predicted )

y_predicted = clf.predict( test_set )
score_test = clf.score( test_set, testY )

print score_test