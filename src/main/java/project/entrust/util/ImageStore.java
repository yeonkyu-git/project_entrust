package project.entrust.util;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ImageStore {

    @Value("${file.dir}")
    private String fileDir;

    // 파일 경로 반환
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    // 파일 저장 및 itemImages 객체 반환
    public List<ItemImage> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<ItemImage> itemImages = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                itemImages.add(storeFile(multipartFile));
            }
        }

        return itemImages;
    }

    // 실제 파일을 저장함
    private ItemImage storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        long fileSize = multipartFile.getSize();

        // 썸네일 이미지와 그냥 이미지 구별
        if (multipartFile.getName().equals("thumnail")) {
            return new ItemImage(originalFilename, storeFileName, fileSize, ItemImageType.THUMNAIL);
        }
        return new ItemImage(originalFilename, storeFileName, fileSize, ItemImageType.NORMAL);
    }

    // 서버에 저장할 파일 이름 추출
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자 반환
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


    // 파일 삭제
    public void deleteFile(String filePath) {
        File storedFile = new File(getFullPath(filePath));

        if (storedFile.exists()) {
            storedFile.delete();
            log.info("파일 삭제 완료");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

    // 파일 수정
    public void updateFile(ItemImage itemImage, MultipartFile multipartFile) throws IOException {
        // 1. 기존 파일 삭제
        deleteFile(itemImage.getStoredFileName());

        // 2. 새로운 파일 저장 및 이미지 객체 수정
        if (!multipartFile.isEmpty()) {
            String newOriginFileName = multipartFile.getOriginalFilename();
            String newStoredFileName = createStoreFileName(newOriginFileName);
            long fileSize = multipartFile.getSize();

            itemImage.updateItemImage(newOriginFileName, newStoredFileName, fileSize);
            multipartFile.transferTo(new File(getFullPath(newStoredFileName)));
        }

    }
}
