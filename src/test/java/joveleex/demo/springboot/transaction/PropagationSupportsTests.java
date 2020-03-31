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
public class PropagationSupportsTests {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    @Qualifier("propagationSupportsAServiceImpl")
    private CommonAService commonAService;

    @Before
    public void cleanAllData() {
        commonDao.cleanAll();
    }

    /**
     * 传播行为：PROPAGATION_SUPPORTS
     * AService受事务控制，BService会加入AService的事务，即AService、BService被同一事务控制
     */
    @Test
    public void withTxAndAThrowEx() {
        // 结果同PROPAGATION_REQUIRED
        commonAService.addAccountWithTransactional("aaa", "bbb", true, false);// 无数据入库
    }

    @Test
    public void withTxAndBThrowEx() {
        commonAService.addAccountWithTransactional("aaa", "bbb", false, true);// 无数据入库
    }

    /**
     * 传播行为：PROPAGATION_SUPPORTS
     * AService不受事务控制，BService也不会使用事务控制自己，即AService、BService都不受事务控制
     */
    @Test
    public void withoutTxAndAThrowEx() {
        commonAService.addAccountWithoutTransactional("aaa", "bbb", true, false);// aaa、bbb入库
    }

    @Test
    public void withoutTxAndBThrowEx() {
        commonAService.addAccountWithoutTransactional("aaa", "bbb", false, true);// aaa、bbb入库
    }

    @After
    public void queryAllData() {
        List<String> names = commonDao.queryAll();
        names.forEach(System.out::println);
    }
}
