package joveleex.demo.springboot.transaction.propagation.common;

public interface CommonAService {

    /**
     * 添加账户（方法附带事务）
     * @param nameA 账户名A
     * @param nameB 账户名B
     * @param isAThrowEx AService是否抛出运行时异常
     * @param isBThrowEx BService是否抛出运行时异常
     */
    void addAccountWithTransactional(String nameA, String nameB, boolean isAThrowEx, boolean isBThrowEx);

    /**
     * 添加账户（方法附带事务）
     * @param nameA 账户名A
     * @param nameB 账户名B
     * @param isAThrowEx AService是否抛出运行时异常
     * @param isBThrowEx BService是否抛出运行时异常
     * @param isCatchB 是否捕获BService抛出的运行时异常
     */
    default void addAccountWithTransactional(String nameA, String nameB, boolean isAThrowEx, boolean isBThrowEx, boolean isCatchB) {}

    /**
     * 添加账户（方法不附带事务）
     * @param nameA 账户名A
     * @param nameB 账户名B
     * @param isAThrowEx AService是否抛出运行时异常
     * @param isBThrowEx BService是否抛出运行时异常
     */
    void addAccountWithoutTransactional(String nameA, String nameB, boolean isAThrowEx, boolean isBThrowEx);
}
