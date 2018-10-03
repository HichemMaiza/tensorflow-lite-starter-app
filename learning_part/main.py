import tensorflow as tf 
import numpy as np 
from tensorflow import keras 
from tensorflow.contrib import lite 

model = keras.Sequential([keras.layers.Dense(units = 1, input_shape = [1])]) 
model.compile(optimizer = 'sgd', loss = 'mean_squared_error')

xs = np.array([-1.0, 0.0, 1.0, 2.0, 3.0,4.0, 5, 10, 12, 11], dtype = float)
ys = np.array([-2.0, 0.0, 2.0, 4.0, 6.0,8.0, 10, 20, 24, 22], dtype = float)

model.fit(xs,ys, epochs = 500)
print(model.predict([10.0])) 

keras_file = "linear.h5"
keras.models.save_model(model, keras_file)
converter = tf.contrib.lite.TocoConverter.from_keras_model_file(keras_file) 
tflite_model = converter.convert()
open("linear.tflite", "wb").write(tflite_model)