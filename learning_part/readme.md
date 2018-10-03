# Notes on keras_to_tensorflow lite conversion

This tutoriels details the use of TOCO converter to convert to tensorflow lite format for mobile applications

## running tensorflow docker image

link ()[<https://www.youtube.com/watch?v=W3bk2pojLoU>]

1. Download the official tensorflow image

    ```shell
    docker pull tensorflow/tensorflow
    ```

2. run the tensoflow image

    ```shell
    docker run -it -p 8888:8888 tensorflow/tensorflow
    ```
3. run and share files in the docker container

    ```shell
    docker run -it --rm --name tf -v C:\Users\maiza\Desktop\test_keras_to_tf:/notebooks -p 8888:8888  tensorflow/tensorflow
    ```

## Debug

* Error

    ```md
        Error response from daemon: error while creating mount source path '/host_mnt/c/Users/maiza/Desktop/test_keras_to_tf': mkdir /host_mnt/c: file exists.
    ```

* Solution

    ```shell
    docker volume rm -f tf
    ```
then restart docker

* link for the solution ()[<https://github.com/docker/for-win/issues/1560]>

## upgrade tensorflow

1. show version

    ```shell
    python -c 'import tensorflow as tf; print(tf.__version__)'
    ```

2. upgrade tensorflow

    ```shell
    pip install --upgrade tensorflow
    ```

## importants links

```shell
https://www.youtube.com/watch?v=RhjBDxpAOIc

https://github.com/tensorflow/tensorflow/blob/master/tensorflow/contrib/lite/toco/g3doc/python_api.md#exporting-a-savedmodel-
```

## Starter Exemple from documentation

there is a problem in the converted tflite model. It works but it gives wrong results

```python
import numpy as np
import tensorflow as tf

# Load TFLite model and allocate tensors.
interpreter = tf.contrib.lite.Interpreter(model_path="converted_model.tflite")
interpreter.allocate_tensors()

# Get input and output tensors.
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Test model on random input data.
input_shape = input_details[0]['shape']
input_data = np.array(np.random.random_sample(input_shape), dtype=np.float32)
interpreter.set_tensor(input_details[0]['index'], input_data)

interpreter.invoke()
output_data = interpreter.get_tensor(output_details[0]['index'])
print(output_data)
```

## Working Method

1. convert keras .h5 to .pb tensorflow graphDef file

    * link to the proposed approach <https://github.com/amir-abdi/keras_to_tensorflow/blob/master/keras_to_tensorflow.py>

    * sample code

        ```python
        from tensorflow.python.framework import graph_util
        from tensorflow.python.framework import graph_io
        import tensorflow as tf

        # import .h5 keras model
        net_model = tf.keras.models.load_model("linear_model.h5")

        tf.keras.backend.set_learning_phase(0)

        # run session
        sess = tf.keras.backend.get_session()

        # [None]*1 , 1 here for num_output = 1, it's hard coded !!
        # for more details see the github link
        pred = [None]*1
        pred_node_names = [None]*1

        for i in range(1):
            pred_node_names[i] = 'output_node'+str(i)
            pred[i] = tf.identity(net_model.outputs[i], name=pred_node_names[i])

        print('output nodes names are: ', pred_node_names)

        # freeze the graph with its weights.

        constant_graph = graph_util.convert_variables_to_constants(sess, sess.graph.as_graph_def(), pred_node_names)
        graph_io.write_graph(constant_graph, '.' , "newModel.pb", as_text=False)
        ```

2. convert tensorflow .pb file to tflite graph file

* tensorflow provide a ready to use script to convert from .pb tensorflow format to tflite format

```shell
!toco \
  --graph_def_file=newModel.pb \
  --output_file=optimized_graph.lite \
  --input_format=TENSORFLOW_GRAPHDEF \
  --output_format=TFLITE \
  --input_shape=1,1\
  --input_arrays= dense_1_input\
  --output_arrays= dense_1/BiasAdd\
  --inference_type= FLOAT \
  --input_data_type= FLOAT
```

* graph_def_file : the protobuff model

* output_file : the name of the desired output model

* input_shape : the input tensor shape in the training step 

* input_arrays : the name of the input array

        - we can get it by runing
            ```python
            model.input
            ```

the result of this command is `<tf.Tensor 'dense_1_input:0' shape=(?, 1) dtype=float32>`
and the *input_arrays* in this case is  *dense_1_input*

* output_arrays : the name of the output array

        - we can get it by runing
            ```python
            model.output
            ```

the result of this command is `<tf.Tensor 'dense_1/BiasAdd:0' shape=(?, 1) dtype=float32>`
and the *output_arrays* in this case is  *dense_1/BiasAdd*

### debug

1. check always input_arrays and output_arrays