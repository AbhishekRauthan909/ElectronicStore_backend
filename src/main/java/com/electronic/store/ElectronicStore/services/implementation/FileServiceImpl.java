package com.electronic.store.ElectronicStore.services.implementation;
import com.electronic.store.ElectronicStore.exceptions.BadApiRequest;
import com.electronic.store.ElectronicStore.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService
{
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {
        String originalFileName=file.getOriginalFilename();
        String fileName= UUID.randomUUID().toString();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
        fileName=fileName+extension;
        String fullPath=path+fileName;
        if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")
        )
        {
            File folder=new File(path);
            if(!folder.exists())
            {
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPath));
            return fileName;
        }
        else
        {
            throw new BadApiRequest("Try with correct extension");
        }
    }
    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
