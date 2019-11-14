package com.buysell.demo.service;

import com.buysell.demo.exception.FileStorageException;
import com.buysell.demo.exception.MyFileNotFoundException;
import com.buysell.demo.model.Image;
import com.buysell.demo.model.Item;
import com.buysell.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image storeImage(MultipartFile file, Item item) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Image image = new Image(fileName, file.getContentType(), item, file.getBytes());

            return imageRepository.save(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Image getFile(String imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + imageId));
    }
}
