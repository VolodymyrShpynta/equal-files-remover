package com.vshpynta.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@UtilityClass
public class FileUtils {

    /**
     * Remove given files
     *
     * @param files given files
     */
    public static void removeFiles(final File... files) {
        if (files == null) {
            return;
        }
        removeFiles(stream(files));
    }

    /**
     * Remove files of given stream
     *
     * @param files given files stream
     */
    public static void removeFiles(final Stream<File> files) {
        if (files == null) {
            return;
        }
        files.filter(Objects::nonNull)
                .filter(File::exists)
                .forEach(File::delete);
    }

    public static void removeFolder(final String folderPath) {
        removeFolder(new File(folderPath));
    }

    public static void removeFolder(final File folder) {
        removeFolders(folder.listFiles(File::isDirectory));
        removeFiles(folder.listFiles(file -> !file.isDirectory()));
        removeFiles(folder);
    }

    public static void removeFolders(final File... folders) {
        if (folders == null) {
            return;
        }
        removeFolders(stream(folders));
    }

    public static void removeFolders(final Stream<File> folders) {
        if (folders == null) {
            return;
        }
        folders.filter(Objects::nonNull)
                .filter(File::exists)
                .forEach(FileUtils::removeFolder);
    }
}
