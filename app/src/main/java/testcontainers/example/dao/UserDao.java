package testcontainers.example.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import testcontainers.example.entity.User;

@RegisterBeanMapper(User.class)
public interface UserDao {
  @SqlUpdate("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR(100))")
  void createTable();

  @SqlUpdate("INSERT INTO user(id, name) VALUES (:id, :name)")
  void insertBean(@BindBean User user);

  @SqlQuery("SELECT * FROM user WHERE id = :id")
  User findById(@Bind("id") int id);

  @SqlQuery("SELECT * FROM user ORDER BY id")
  List<User> listAll();
}
