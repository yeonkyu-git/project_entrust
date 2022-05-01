package project.entrust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.entrust.entity.ItemImage;
import project.entrust.repository.ItemImageRepository;
import project.entrust.util.ImageStore;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemImageService {

    private final ImageStore imageStore;
    private final ItemImageRepository itemImageRepository;

    /**
     * 상품 이미지 저장
     * (실제 ItemImage 객체의 DB 저장은 item이 저장될 때 같이 저장됨 cascade)
     */
    @Transactional
    public List<ItemImage> saveItemImage(List<MultipartFile> imageFiles) throws IOException {
        return imageStore.storeFiles(imageFiles);
    }


    /**
     * 상품 이미지 삭제
     */
    @Transactional
    public void deleteItemImage(Long itemImageId) {
        // 1. 아이템 이미지 객체 조회 및 저장된 파일 경로 조회
        ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);
        String filePath = itemImage.getStoredFileName();

        // 2. 아이템 이미지 삭제 (서버에 저장된 이미지)
        imageStore.deleteFile(filePath);

        // 3. 아이템 객체에서의 이미지 연관관계 삭제 및 아이템이미지 객체 삭제
        itemImage.deleteItemListImageClass();
        itemImageRepository.delete(itemImage);
    }

    /**
     * 아이템 객체 삭제 시 상품 이미지 삭제
     */
    public void deleteAllItemImage(List<ItemImage> itemImages) {
        for (ItemImage itemImage : itemImages) {
            imageStore.deleteFile(itemImage.getStoredFileName());
        }
    }

    /**
     * 상품 이미지 수정
     */
    @Transactional
    public void updateItemImage(Long itemImageId, MultipartFile imageFile) throws IOException {
        // 1. 아이템 이미지 객체 조회 및 저장된 파일 경로 조회
        ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);
        String filePath = itemImage.getStoredFileName();

        // 2. 아이템 이미지 수정 (서버에 저장된 이미지)
        imageStore.updateFile(itemImage, imageFile);

    }

    /**
     * 상품 이미지 조회
     */
}
