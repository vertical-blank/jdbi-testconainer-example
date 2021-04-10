package testcontainers.example.dao;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import testcontainers.example.entity.User;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@Testcontainers
class UserTest {
    
    @Container
    public static MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8")
        .withDatabaseName("foo")
        .withUsername("foo")
        .withPassword("secret");

    @Test void testUser() {
        Jdbi jdbi = Jdbi.create(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.registerRowMapper(BeanMapper.factory(User.class));

        // init
        jdbi.useExtension(UserDao.class, dao -> {
            dao.createTable();
            dao.insertBean(User.of(1, "Hoge"));
            dao.insertBean(User.of(2, "Fuga"));
            dao.insertBean(User.of(3, "Piyo"));
        });

        List<User> userNames = jdbi.withExtension(UserDao.class, UserDao::listAll);
        assertThat(userNames).containsExactly(User.of(1, "Hoge"), User.of(2, "Fuga"), User.of(3, "Piyo"));

        User user1 = jdbi.withExtension(UserDao.class, dao -> dao.findById(1));
        assertThat(user1).isEqualTo(User.of(1, "Hoge"));
    }
}
