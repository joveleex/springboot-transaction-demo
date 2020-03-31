package joveleex.demo.springboot.transaction.propagation.common;

import java.util.List;

public interface CommonDao {

    /**
     * 添加账户
     * @param name 账户名
     */
    void addAccount(String name);

    /**
     * 清空数据
     */
    void cleanAll();

    /**
     * 查询所有数据
     * @return 所有账户名
     */
    List<String> queryAll();
}
