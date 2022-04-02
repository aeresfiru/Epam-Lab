package jdbc;

import com.epam.esm.dao.config.DevDatasourceConfig;
import com.epam.esm.dao.jdbc.JdbcTagDaoImpl;
import com.epam.esm.domain.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DevDatasourceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcTagDaoImplTest {

    @Autowired
    private JdbcTagDaoImpl tagDao;
    private Tag firstTestTag;
    private Tag thirdTestTag;
    private List<Tag> tagList;

    @BeforeEach
    void setUp() {
        firstTestTag = Tag.builder().id(1L).name("Test").build();
        Tag secondTestTag = Tag.builder().id(11L).name("Romance").build();
        thirdTestTag = Tag.builder().id(13L).name("14 February").build();
        tagList = Arrays.asList(secondTestTag,
                Tag.builder().id(12L).name("Mothers day").build(),
                thirdTestTag,
                Tag.builder().id(14L).name("Active").build());
    }

    @Test
    void findAllTagsTest() {
        Assertions.assertEquals(tagList, tagDao.readAll());
    }

    @Test
    void addTagTest() {
        Assertions.assertTrue(tagDao.create(firstTestTag));
    }

    @Test
    void findByIdTest() {
        Assertions.assertEquals(tagDao.read(13).get(), thirdTestTag);
    }

    @Test
    void findByIdWithInvalidIdTest() {
        Assertions.assertFalse(tagDao.read(333).isPresent());
    }

    @Test
    void removeTagPositiveTest() {
        Assertions.assertTrue(tagDao.delete(11));
    }

    @Test
    void removeUnknownTagTest() {
        Assertions.assertFalse(tagDao.delete(444));
    }
}