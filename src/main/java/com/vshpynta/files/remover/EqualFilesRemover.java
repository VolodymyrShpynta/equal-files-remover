package com.vshpynta.files.remover;

import com.vshpynta.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

/**
 * Created by Volodymyr Shpynta on 14.09.2017.
 */
public class EqualFilesRemover {

    private static int removedFilesNumber = 0;

    public static void main(String[] args) {
        String rootFolderPath = "G:\\Backup of Disc E\\PHOTOS\\";
        Map<String, List<File>> groupedFilesByNameAndSize = groupFilesByNameAndSize(rootFolderPath);
        deleteEqualFiles(groupedFilesByNameAndSize);

        System.out.println(format("Removed %s files", removedFilesNumber));
    }

    //Only first file of each group will be retained
    private static void deleteEqualFiles(Map<String, List<File>> groupedFilesByNameAndSize) {
        groupedFilesByNameAndSize.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> {
                    entry.getValue().remove(0); //Retain only first file
                    return entry;
                })
                .map(Map.Entry::getValue)
                .forEach(filesToRemove -> filesToRemove.forEach(file -> {
                    removedFilesNumber++;
                    FileUtils.removeFiles(file);
                }));
    }

    private static Map<String, List<File>> groupFilesByNameAndSize(String rootFolderPath) {
        return getAllFiles(new File(rootFolderPath))
                .collect(toMap(file -> file.getName() + getFileSize(file),
                        file -> {
                            List<File> files = new ArrayList<>();
                            files.add(file);
                            return files;
                        },
                        (files, files2) -> {
                            files.addAll(files2);
                            return files;
                        }));
    }

    private static Stream<File> getAllFiles(File rootFolder) {
        if (rootFolder.listFiles() == null) {
            return Stream.empty();
        }
        return Arrays.stream(rootFolder.listFiles())
                .flatMap(file -> file.isDirectory() ? getAllFiles(file) : Stream.of(file));
    }

    private static long getFileSize(File file) {
        return file.length();
    }
}
