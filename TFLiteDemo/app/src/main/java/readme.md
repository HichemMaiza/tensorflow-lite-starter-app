## Debug 

1. Add this into android {} in the build.gradle(Module:app) file 

    ```gradle 
    aaptOptions{
            noCompress "tflite"
        }
    ``` 
       
2. Add this config to build.gradle(Module:app) file

    ````gradle
    implementation 'org.tensorflow:tensorflow-lite:+'
    ````

3. Add `Base` to parent flag in res/values/styles 

    ```xml 
    Base.Theme.AppCompat.Light.DarkActionBar
    ```  
    
4. In `MainActivity.java` import the tf interpreter
    
    ```java 
    import org.tensorflow.lite.Interpreter ; 
    ```
 
 ## Dev 
 
 1. check input/output tensorflow model !
 
 2. transform tensorflow to tflite model: a quick check if
 
 there is some ops not supported by tensorflow
 
 3. if there is some ops, delete them
 
