package com.example.sara.grammy.Utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FileSearch {

    /*search directory and return list of all directories inside*/
    public static ArrayList<String> getDirectoryPaths(String directory){

        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();

        for(int i = 0; i < listFiles.length; i++){
            if(listFiles[i].isDirectory()){
                pathArray.add(listFiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
    /*search directory and return list of all files inside*/
    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();

        for(int i = 0; i < listFiles.length; i++){
            if(listFiles[i].isFile()){
                pathArray.add(listFiles[i].getAbsolutePath());
            }
        }
        return pathArray;

    }


}
