package project.entrust.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.entrust.entity.ItemImage;
import project.entrust.entity.assistant.ItemImageType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ImageStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<ItemImage> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<ItemImage> itemImages = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                itemImages.add(storeFile(multipartFile));
            }
        }

        return itemImages;
    }

    private ItemImage storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        long fileSize = multipartFile.getSize();
        String name = multipartFile.getName();

        // 썸네일 이미지와 그냥 이미지 구별
        if (multipartFile.getName().equals("thumnail")) {
            return new ItemImage(originalFilename, storeFileName, fileSize, ItemImageType.THUMNAIL);
        }
        return new ItemImage(originalFilename, storeFileName, fileSize, ItemImageType.NORMAL);

    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
