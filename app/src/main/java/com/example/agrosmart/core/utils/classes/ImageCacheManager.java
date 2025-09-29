package com.example.agrosmart.core.utils.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ImageCacheManager {

    private static final String TAG = "IMAGE_CACHE_UTILS";
    private static final String TEMP_IMAGE_PREFIX = "temp_image_";

    public static String saveImageToCache(Context context, byte[] imageBytes) throws IOException {

        if (!isValidImage(imageBytes)) {
            Log.w(TAG, "Intento de guardar array de bytes vacío o de mal formato");
            return null;
        }

        try{
            String filename = TEMP_IMAGE_PREFIX + System.currentTimeMillis() + ".jpeg";
            File file = new File(context.getCacheDir(), filename);

            try(FileOutputStream fos = new FileOutputStream(file)){
                fos.write(imageBytes);
                fos.flush();
            }

            return file.getAbsolutePath();
        } catch (IOException e){
            Log.w(TAG, "Error: " + e.getMessage());
            return null;
        }
    }

    // validador de formato de imagen
    private static boolean isValidImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length < 4) {
            System.out.println("Imagen no valida");
            return false;
        }

        // Verificar signatures de formatos comunes
        // JPEG: FF D8 FF
        if (imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8 && imageBytes[2] == (byte) 0xFF) {
            System.out.println("Imagen de tipo JPEG");
            return true;
        }

        // PNG: 89 50 4E 47
        if (imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50 &&
                imageBytes[2] == (byte) 0x4E && imageBytes[3] == (byte) 0x47) {
            return true;
        }

        // WEBP: RIFF .... WEBP
        if (imageBytes[0] == 'R' && imageBytes[1] == 'I' && imageBytes[2] == 'F' && imageBytes[3] == 'F') {
            return true;
        }

        return false;
    }

    // Cargar imagen desde cache como Bitmap
    public static Bitmap loadImageFromCache(Context context, String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        try {
            File file = new File(filePath);
            if (file.exists()) {
                return BitmapFactory.decodeFile(filePath);
            } else {
                Log.w(TAG, "Archivo no encontrado: " + filePath);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar imagen desde cache: " + e.getMessage());
            return null;
        }
    }

    public static byte[] getArrayFromFile(Context context, String path){
        if (path == null || path.isEmpty()) {
            return null;
        }

        File file = new File(path);
        byte[] loadedByteArray = null;

        try (FileInputStream fis = new FileInputStream(file)) {
            int fileSize = fis.available(); // Obtiene el tamaño del archivo
            loadedByteArray = new byte[fileSize];
            fis.read(loadedByteArray);

            return loadedByteArray;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    // Limpiar archivos temporales del cache
    public static void cleanupCache(Context context) {
        try {
            File cacheDir = context.getCacheDir();
            File[] files = cacheDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith(TEMP_IMAGE_PREFIX)) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            Log.d(TAG, "Archivo eliminado: " + file.getName());
                        } else {
                            Log.w(TAG, "No se pudo eliminar: " + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al limpiar cache: " + e.getMessage());
        }
    }
}

