package com.javacore.hw32;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static ArrayList<GameProgress> createRandomGameProgresses(int count){
        ArrayList<GameProgress> gameProgresses = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            gameProgresses.add(new GameProgress(new Random().nextInt(GameProgress.MAX_HEALTH),
                                                new Random().nextInt(GameProgress.MAX_WEAPON),
                                                new Random().nextInt(GameProgress.MAX_LEVEL),
                                                new Random().nextInt(GameProgress.MAX_DISTANCE)
                                                )
            );
        }

        return gameProgresses;
    }

    public static ArrayList<String> serializeObjectsToFiles(String path, ArrayList<GameProgress> gameProgresses){
        ArrayList<String> fNames = new ArrayList<>();
        for(int i = 0; i < gameProgresses.size(); i++){
            fNames.add("saveObj" + String.valueOf(i) + ".dat");
            try (FileOutputStream fos = new FileOutputStream(path + "//" + fNames.get(i));
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(gameProgresses.get(i));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        return fNames;
    }

    public static boolean filesToArchive(String path, ArrayList<String> files, String archiveName){
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path + "//" + archiveName))) {
            for (int i = 0; i < files.size(); i++) {
                try (FileInputStream fis = new FileInputStream(path + "//" + files.get(i))) {
                    ZipEntry entry = new ZipEntry(files.get(i)); // для сохранения пути в архиве, можно path + "//" + files.get(i)
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                File delFile = new File(path + "//" + files.get(i));
                delFile.delete();
            }
        } catch (Exception exc) {
        System.out.println(exc.getMessage());
        }

        return true;
    }

    public static void main(String[] args){
        String path = "Games//savegames";
        String archiveName = "save.zip";
        filesToArchive(path, serializeObjectsToFiles(path, createRandomGameProgresses(3)), archiveName);
    }
}
