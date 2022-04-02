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
        firstTestTag = new Tag(1L, "Free");
        Tag secondTestTag = new Tag(11L, "Romance");
        thirdTestTag = new Tag(13L, "14 February");
        tagList = Arrays.asList(secondTestTag,
                new Tag(12L, "Mothers day"),
                thirdTestTag,
                new Tag(14L, "Active"));
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