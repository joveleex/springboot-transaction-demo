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
public class PropagationNotSupportedTests {

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
     * 传播行为：PROPAGATION_NOT_SUPPORTED
     * AService受事务控制，BService会先挂起AService的事务，然后不使用事务控制自己
     */
    @Test
    public void withTxAndAThrowEx() {
        commonAService.addAccountWithTransactional("aaa", "bbb", true, false);// bbb入库
    }

    @Test
    public void withTxAndBThrowEx() {
        // BService不受事务管理，BService抛出的异常不会导致bbb回滚，bbb入库
        // BService抛出的异常没有被catch，异常抛到了AService，AService也没有catch异常，导致AService的aaa被回滚
        commonAService.addAccountWithTransactional("aaa", "bbb", false, true);// bbb入库
    }

    /**
     * 传播行为：PROPAGATION_NOT_SUPPORTED
     * AService不受事务控制，BService直接不使用事务控制自己
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
