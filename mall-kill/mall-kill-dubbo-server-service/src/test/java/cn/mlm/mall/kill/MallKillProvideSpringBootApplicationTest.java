package cn.mlm.mall.kill;

import cn.mlm.mall.kill.service.IItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 9:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallKillProvideSpringBootApplicationTest {

    @Autowired
    private IItemService iItemService;

    @Test
    public void test(){
        System.out.println(iItemService.getKillDetail(1));
    }
}
