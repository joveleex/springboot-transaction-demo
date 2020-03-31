package joveleex.demo.springboot.transaction;

import joveleex.demo.springboot.transaction.propagation.common.CommonAService;
import joveleex.demo.springboot.transaction.propagation.common.CommonDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PropagationNestedTests {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    @Qualifier("propagationNestedAServiceImpl")
    private CommonAService commonAService;

    @Before
    public void cleanAllData() {
        commonDao.cleanAll();
    }

    /**
     * 传播行为：PROPAGATION_NESTED
     * AService受事务控制，BService会使用AService事务的嵌套事务控制自己，AService、BService受不同事务控制
     * 但BService的事务是嵌套在AService的事务内的，属于AService的事务，因此无论AService还是BService发生异常都将回滚
     *
     * 与PROPAGATION_REQUIRED的区别：NESTED是嵌套事务，AService、BService受不同事务控制
     * 与PROPAGATION_REQUIRED_NEW的区别：NESTED不会挂起事务，BService受嵌套事务管理时，AService事务仍然是Active的
     */
    @Test
    public void withTxAndAThrowEx() {
        commonAService.addAccountWithTransactional("aaa", "bbb", true, false);// 无数据入库
    }

    @Test
    public void withTxAndBThrowEx() {
        commonAService.addAccountWithTransactional("aaa", "bbb", false, true);// 无数据入库
    }

    @Test
    public void withTxAndBThrowExCatchB() {
        commonAService.addAccountWithTransactional("aaa", "bbb", false, true, true);// aaa入库且控制台无异常抛出
    }

    /**
     * 传播行为：PROPAGATION_NESTED
     * AService不受事务控制，BService会按照PROPAGATION_REQUIRED方式创建一个新事物控制自己
     */
    @Test
    public void withoutTxAndAThrowEx() {
        commonAService.addAccountWithoutTransactional("aaa", "bbb", true, false);// aaa、bbb入库
    }

    @Test
    public void withoutTxAndBThrowEx() {
        commonAService.addAccountWithoutTransactional("aaa", "bbb", false, true);// aaa入库
    }

    @After
    public void queryAllData() {
        List<String> names = commonDao.queryAll();
        names.forEach(System.out::println);
    }
}
