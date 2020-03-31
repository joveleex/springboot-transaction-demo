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
public class PropagationRequiredTests {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    @Qualifier("propagationRequiredAServiceImpl")
    private CommonAService commonAService;

    @Before
    public void cleanAllData() {
        commonDao.cleanAll();
    }

    /**
     * 传播行为：PROPAGATION_REQUIRED
     * AService受事务控制，BService会加入AService的事务，即AService、BService被同一事务控制
     */
    @Test
    public void withTxAndAThrowEx() {
        // 无论是在AService还是BService中抛出运行时异常，AService、BService内所作的操作都将回滚，因此没有任何数据入库
        commonAService.addAccountWithTransactional("aaa", "bbb", true, false);// 无数据入库
    }

    @Test
    public void withTxAndBThrowEx() {
        commonAService.addAccountWithTransactional("aaa", "bbb", false, true);// 无数据入库
    }

    /**
     * 传播行为：PROPAGATION_REQUIRED
     * AService不受事务控制，BService会新建一个事务控制自己
     */
    @Test
    public void withoutTxAndAThrowEx() {
        // AService不受事务控制，无论AService是否抛出运行时异常，aaa都将入库，BService会新建事务控制自己，因此BService中抛出运行时异常，bbb将被回滚
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
