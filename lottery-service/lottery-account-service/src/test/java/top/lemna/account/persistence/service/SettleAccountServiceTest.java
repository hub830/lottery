package top.lemna.account.persistence.service;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.baidu.fsg.uid.UidGenerator;
import top.lemna.account.model.constant.SettleAccountType;
import top.lemna.account.model.constant.SettleType;
import top.lemna.account.model.dto.SettleAccountInfoDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SettleAccountServiceTest {

  @Mock
  private UidGenerator uidGenerator;

  @Autowired
  @InjectMocks
  private SettleAccountService service;

  Long accountNo;
  String accountName;
  String settleAccountNo;
  SettleType settleType;
  SettleAccountType accountType;
  SettleAccountInfoDto accountInfo;

  @BeforeEach
  void setUp() throws Exception {
    accountName = "测试";
    accountNo = 1001L;
    settleAccountNo = "20001";
    settleType = SettleType.PRIVATE;
    accountType = SettleAccountType.ALIPAY;
    accountInfo = new SettleAccountInfoDto(accountName, settleAccountNo);
  }

  @Test
  void testCreate() {
    when(uidGenerator.getUID()).thenReturn(8001L);
    service.create(accountNo, settleType, accountType, accountInfo);
  }

}
