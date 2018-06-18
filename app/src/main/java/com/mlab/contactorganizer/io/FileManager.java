package com.mlab.contactorganizer.io;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by labud on 02.03.2018.
 */

public class FileManager {

    public String readFile(String fileName, AppCompatActivity activity) {

        try {
            // Open stream to read file.
            FileInputStream in = activity.openFileInput(fileName);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s = null;
            while((s = br.readLine())!= null)  {
                sb.append(s).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            Log.e("FileManager", e.getMessage(), e);
        }

        return null;
    }

    public Properties readProperties(String fileName, AppCompatActivity activity){
        Properties properties = new Properties();

        try{
            FileInputStream in = activity.openFileInput(fileName);
            properties.load(in);

            Log.d("FileManager", "Cos odczytano? " + fileName);
        }catch (Exception e){
            Log.e("StoreManager", e.getMessage(), e);
        }

        return properties;
    }

    public void writeProperties(String fileName, Properties properties, AppCompatActivity activity) {
        OutputStream output = null;


        File path = activity.getFilesDir();


        File f = new File(path, fileName);

        try {

            output = new FileOutputStream(f);
            // save properties to project root folder
            properties.store(output, null);

            Log.d("FileManager", "File stored??! " + f.getPath());
        } catch (IOException e) {
            Log.e("FileManager", e.getMessage(), e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void writeFile(String fileName, String data, AppCompatActivity activity) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(activity.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
