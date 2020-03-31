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
public class PropagationRequiredNewTests {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    @Qualifier("propagationRequiredNewAServiceImpl")
    private CommonAService commonAService;

    @Before
    public void cleanAllData() {
        commonDao.cleanAll();
    }

    /**
     * 传播行为：PROPAGATION_REQUIRED_NEW
     * AService受事务控制，BService会先挂起AService的事务，然后创建新事物控制自己，即AService、BService被不同事务控制
     */
    @Test
    public void withTxAndAThrowEx() {
        commonAService.addAccountWithTransactional("aaa", "bbb", true, false);// bbb入库
    }

    @Test
    public void withTxAndBThrowEx() {
        // AService、BService被不同事务控制，BService抛出的运行时异常但没有catch异常，导致bbb被回滚
        // 由于BService没有catch该异常，异常抛至AService，AService也没有catch异常，导致aaa也被回滚
        commonAService.addAccountWithTransactional("aaa", "bbb", false, true);// 无数据入库
    }

    /**
     * 传播行为：PROPAGATION_REQUIRED_NEW
     * AService不受事务控制，BService会直接新建一个事务控制自己
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
