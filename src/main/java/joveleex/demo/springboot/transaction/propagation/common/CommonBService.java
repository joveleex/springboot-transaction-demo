package joveleex.demo.springboot.transaction.propagation.common;

public interface CommonBService {

    /**
     * 添加账户（方法附带事务）
     * @param name 账户名
     * @param isThrowEx 是否抛出运行时异常
     */
    void addAccount(String name, boolean isThrowEx);
}
