package alararestaurant.service;

import alararestaurant.common.FilePaths;
import alararestaurant.common.OutputMessages;
import alararestaurant.domain.dtos.item.ImportItemJson;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemServiceImpl implements ItemService {

    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validator;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ItemServiceImpl(Gson gson, FileUtil fileUtil, ValidationUtil validator, ModelMapper modelMapper, ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validator = validator;
        this.modelMapper = modelMapper;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Boolean itemsAreImported() {
        return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {
        return fileUtil.readFile(FilePaths.ITEM_JSON_PATH);
    }

    @Override
    public String importItems(String items) {
        StringBuilder importInfo = new StringBuilder();

        ImportItemJson[] allItems = gson.fromJson(items, ImportItemJson[].class);

        for (ImportItemJson itemJson : allItems) {
            if (! validator.isValid(itemJson)) {
                importInfo.append(OutputMessages.INVALID_DATA_FOR_ENTITY);
                continue;
            }

            Item item = itemRepository.findByName(itemJson.getName()).orElse(null);

            if (item != null) {
                importInfo.append(OutputMessages.INVALID_DATA_FOR_ENTITY);
                continue;
            }

            Category category = this.addCategoryIfMissing(itemJson.getCategory());

            item = modelMapper.map(itemJson, Item.class);

            item.setCategory(category);

            itemRepository.saveAndFlush(item);

            importInfo.append(String.format(OutputMessages.SUCCESSFULLY_IMPORT_ENTITY, item.getName()));
        }

        return importInfo.toString();
    }

    private Category addCategoryIfMissing(String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElse(null);

        if (category == null) {
            category = new Category();

            category.setName(categoryName);

            categoryRepository.saveAndFlush(category);
        }

        return category;
    }

}
