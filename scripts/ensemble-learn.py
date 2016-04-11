# -*- coding: utf-8 -*-
"""
Created on Sun Apr 10 11:58:53 2016

@author: parth
"""
import numpy as np

import sys
import math
#from sys import argv
from sklearn.datasets import load_svmlight_file
from sklearn import ensemble
from sklearn import tree
from sklearn.metrics import precision_recall_fscore_support

if(len(sys.argv)==3):
    train_data = sys.argv[1]
    test_data = sys.argv[2]
else:
    sys.exit("Usage: python ensemble-learn.py <trainFile> <testFile>")
#train_data = r"/home/pgupta/Dropbox/Shared with Parth/train-dssm.only2.svm";
#test_data = r"/home/pgupta/Dropbox/Shared with Parth/test-dssm.only2.svm";

trainX, trainY = load_svmlight_file(train_data);
testX, testY = load_svmlight_file(test_data);

train_set = trainX.toarray();
test_set = testX.toarray();

trainY = [int(round(trainY[i])) for i in xrange(len(trainY))]
testY  = [int(round(testY[i] )) for i in xrange(len(testY))]

n_features=24
num_estimators = 20
##DTC
clf = tree.DecisionTreeClassifier(max_depth=None, 
                                  min_samples_split=1,
                                  random_state=0)
clf.fit(train_set, trainY)
y_predicted = clf.predict( train_set )
score_train = clf.score( train_set, trainY )
y_predicted = clf.predict( test_set )
score_test = clf.score( test_set, testY )
print "DTC"
print precision_recall_fscore_support(testY, y_predicted, average='binary')
                    

##RFC
clf = ensemble.RandomForestClassifier(n_estimators=num_estimators, 
                                      max_features = 5,
                                      max_depth=None, 
                                      min_samples_split=1, 
                                      random_state=0)
clf.fit(train_set, trainY)
y_predicted = clf.predict( train_set )
score_train = clf.score( train_set, trainY )
y_predicted = clf.predict( test_set )
score_test = clf.score( test_set, testY )
print "RFC"
print precision_recall_fscore_support(testY, y_predicted, average='binary')


## ETR
clf = ensemble.ExtraTreesClassifier(n_estimators=num_estimators, 
                                    max_features = 5,
                                    max_depth=None, 
                                    min_samples_split=1, 
                                    random_state=0)
clf.fit(train_set, trainY)
y_predicted = clf.predict( train_set )
score_train = clf.score( train_set, trainY )
y_predicted = clf.predict( test_set )
score_test = clf.score( test_set, testY )
print "ETR"
print precision_recall_fscore_support(testY, y_predicted, average='binary')

