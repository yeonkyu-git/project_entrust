package project.entrust.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.entrust.entity.assistant.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String categoryName;


    /* static Method */
    public static Category createCategory(String categoryName) {
        return new Category(categoryName);
    }

    /* 생성 메소드 */
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    /* 비즈니스 로직 */
    // 카테고리 이름 수정
    public void changeCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
