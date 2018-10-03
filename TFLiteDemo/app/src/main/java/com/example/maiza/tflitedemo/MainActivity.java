package com.example.maiza.tflitedemo;
import org.tensorflow.lite.Interpreter ;

import android.content.res.AssetFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    EditText inputNumber ;
    Button inferButton ;
    TextView outputNumber ;
    Interpreter tflite ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputNumber = (EditText) findViewById(R.id.inputNumber);
        inferButton = (Button) findViewById(R.id.inferButton);
        outputNumber = (TextView) findViewById(R.id.outputNumber);

        // construct the interpreter object

        try {
            tflite = new Interpreter(loadModelFile()) ;
        } catch (Exception ex){
            ex.printStackTrace();
        }

        // when we click the button we should do inference
        inferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float prediction = doInference(inputNumber.getText().toString());
                outputNumber.setText(Float.toString(prediction));
            }
        });
    }

    public float doInference(String inputString){

        // define input value
        float[] inputVal = new float[1] ;
        inputVal[0] = Float.valueOf(inputString);

        float[][] outputVal = new float[1][1];

        // run inference passing input, output shape
        tflite.run(inputVal, outputVal) ;

        float inferredValue = outputVal[0][0];
        return inferredValue ;
    }

    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("linear.tflite") ;
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor()) ;
        FileChannel fileChannel = inputStream.getChannel() ;
        long startOffset = fileDescriptor.getStartOffset() ;
        long declaredLength = fileDescriptor.getDeclaredLength() ;
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength) ;
    }
}
