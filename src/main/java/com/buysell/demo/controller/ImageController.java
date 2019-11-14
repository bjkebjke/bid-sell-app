package com.buysell.demo.controller;

import com.buysell.demo.model.Image;
import com.buysell.demo.payload.ImageResponse;
import com.buysell.demo.service.ImageService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(value = "/api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @GetMapping("/info/{imageId}")
    public ImageResponse getImage(@PathVariable String imageId) {
        Image image = imageService.getFile(imageId);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(image.getId())
                .toUriString();

        return new ImageResponse(image.getFileName(), fileDownloadUri,
                image.getFileType());
    }

    @GetMapping(
            value = "/download/{imageId}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public String downloadImage(@PathVariable String imageId) {
        HttpHeaders headers = new HttpHeaders();
        // Load file from database
        Image image = imageService.getFile(imageId);

        return Base64.encode(image.getPicture());
    }
}
