package joveleex.demo.springboot.transaction.propagation.never;

import joveleex.demo.springboot.transaction.propagation.common.CommonBService;
import joveleex.demo.springboot.transaction.propagation.common.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropagationNeverBServiceImpl implements CommonBService {

    @Autowired
    private CommonDao commonDao;

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void addAccount(String name, boolean isThrowEx) {
        commonDao.addAccount(name);
        if (isThrowEx) {
            throw new RuntimeException("BService RuntimeException");
        }
    }
}
