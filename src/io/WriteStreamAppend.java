package io;
 
import java.io.BufferedWriter;   

import java.io.FileOutputStream;   
import java.io.FileWriter;   
import java.io.IOException;   
import java.io.OutputStreamWriter;   
import java.io.RandomAccessFile;   

public class WriteStreamAppend {  
      /**  
       * @param fileName 
       * @param content 
       */  
public static void appendBufferWriter(String file, String conent) {   
    BufferedWriter out = null;   
    try {   
         out = new BufferedWriter(new OutputStreamWriter(   
                  new FileOutputStream(file, true)));   
                 out.write(conent);   
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                out.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   
    }   
  
    /**  
     * @param fileName 
     * @param content 
     */  
    public static void appendFileWriter(String fileName, String content) {   
        try {   
           
            FileWriter writer = new FileWriter(fileName, true);   
            writer.write(content);   
            writer.close();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }   
   
    public static void appendRandAcc(String fileName, String content) {   
        try {   
           
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");   
         
            long fileLength = randomFile.length();   
       
            randomFile.seek(fileLength);   
            randomFile.writeBytes(content);   
            randomFile.close();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
    }  
}
