package alararestaurant.service;

import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String exportCategoriesByCountOfItems() {
        List<Category> allCategories = categoryRepository.findAllCategories();

        allCategories.sort((f,s) -> {
            Double first = f.getItems().stream()
                    .map(Item::getPrice)
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();

            Double second = s.getItems().stream()
                    .map(Item::getPrice)
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();
            return second.compareTo(first);
        });

        StringBuilder categoriesCount = new StringBuilder();

        for (Category category : allCategories) {
            categoriesCount.append("Category: ")
                    .append(category.getName())
                    .append(System.lineSeparator());

            for (Item item : category.getItems()) {
                categoriesCount.append("--- Item Name: ")
                        .append(item.getName())
                        .append(System.lineSeparator());

                categoriesCount.append("--- Item Price: ")
                        .append(item.getPrice())
                        .append(System.lineSeparator());
            }
        }

        return categoriesCount.toString();
    }
}
