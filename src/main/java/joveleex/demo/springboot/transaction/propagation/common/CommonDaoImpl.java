package joveleex.demo.springboot.transaction.propagation.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommonDaoImpl implements CommonDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addAccount(String name) {
        String sql = "insert into account (name) values (?)";
        jdbcTemplate.update(sql, name);
    }

    @Override
    public void cleanAll() {
        String sql = "delete from account";
        jdbcTemplate.execute(sql);
    }

    @Override
    public List<String> queryAll() {
        String sql = "select name from account";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
