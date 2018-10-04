# TFlite Starter App

## Goal

Illustrate the different steps to deploy a tensorflow deep learning model. this tuto is divided into two parts

1. [The first part](<https://github.com/deepKratos/tflite_starter_app/tree/master/learning_part>)

    * training the model => result : model.h5
    * transforming into protobuff tensorflow graph format => result : model.pb
    * transforming into tflite format => result : model.tflite

2. [The second part](<https://github.com/deepKratos/tflite_starter_app/tree/master/TFLiteDemo>)

    * build the android app
    * compile tflite dependencies
    * define input/output model shapes
    * run inference

![Demo](test.PNG)