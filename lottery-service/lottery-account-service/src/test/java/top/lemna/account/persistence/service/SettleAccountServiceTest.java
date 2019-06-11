package top.lemna.account.persistence.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
import top.lemna.account.model.dto.AlipaySettleAccountInfoDto;
import top.lemna.account.model.dto.SettleAccountInfoDto;
import top.lemna.account.persistence.domain.SettleAccount;

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
    SettleAccount settleAccount = service.create(accountNo, settleType, accountType, accountInfo);
    assertThat(settleAccount,notNullValue());
    assertThat(settleAccount.getId(),greaterThan(0L));
  }

  @Test
  void testFindById() {
    SettleAccount settleAccount = service.findById(1L);
    
    SettleAccountInfoDto accountInfoDto = settleAccount.getSettleAccountInfo();
    assertThat(accountInfoDto,instanceOf(AlipaySettleAccountInfoDto.class));
    assertThat(settleAccount.getId(),is(1L));
  }

}
