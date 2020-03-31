package joveleex.demo.springboot.transaction.propagation.required;

import joveleex.demo.springboot.transaction.propagation.common.CommonBService;
import joveleex.demo.springboot.transaction.propagation.common.CommonDao;
import joveleex.demo.springboot.transaction.propagation.common.CommonAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropagationRequiredAServiceImpl implements CommonAService {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    @Qualifier("propagationRequiredBServiceImpl")
    private CommonBService commonBService;

    @Override
    @Transactional
    public void addAccountWithTransactional(String nameA, String nameB, boolean isAThrowEx, boolean isBThrowEx) {
        commonDao.addAccount(nameA);
        commonBService.addAccount(nameB, isBThrowEx);
        if (isAThrowEx) {
            throw new RuntimeException("AService RuntimeException");
        }
    }

    @Override
    public void addAccountWithoutTransactional(String nameA, String nameB, boolean isAThrowEx, boolean isBThrowEx) {
        commonDao.addAccount(nameA);
        commonBService.addAccount(nameB, isBThrowEx);
        if (isAThrowEx) {
            throw new RuntimeException("AService RuntimeException");
        }
    }

}
