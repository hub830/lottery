package top.lemna.account.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.fsg.uid.UidGenerator;

import top.lemna.account.model.constant.BillType;
import top.lemna.account.persistence.domain.AccountLog;
import top.lemna.account.persistence.repository.AccountLogRepository;
import top.lemna.data.jpa.service.BaseService;

@Service
@Transactional
public class AccountLogService extends BaseService<AccountLog> {

	@SuppressWarnings("unused")
	@Autowired
	private AccountLogRepository repository;

	@Autowired
	private UidGenerator uidGenerator;

	/**
	 * 借记操作
	 * 
	 * @param accountNo
	 * @param amount
	 * @param externalNo
	 * @param beforeBalance
	 * @param type
	 * @param remark
	 * @return
	 */
	AccountLog credit(Long accountNo, Long amount, Long externalNo, Long beforeBalance, BillType type, String remark) {
		AccountLog accountLog = AccountLog.builder()//
				.withLogNo(uidGenerator.getUID())//
				.withAccountNo(accountNo)//
				.withExternalNo(String.valueOf(externalNo))//
				.withAmount(amount)//
				.withBeforeBalance(beforeBalance)//
				.withAfterBalance(beforeBalance + amount)//
				.withType(type)//
				.withRemark(remark).build();
		return save(accountLog);
	}

	/**
	 * 贷记操作
	 * 
	 * @param accountNo
	 * @param amount
	 * @param externalNo
	 * @param beforeBalance
	 * @param type
	 * @param remark
	 * @return
	 */
	AccountLog debit(Long accountNo, Long amount, Long externalNo, Long beforeBalance, BillType type, String remark) {
		AccountLog accountLog = AccountLog.builder()//
				.withLogNo(uidGenerator.getUID())//
				.withAccountNo(accountNo)//
				.withExternalNo(String.valueOf(externalNo))//
				.withAmount(amount)//
				.withBeforeBalance(beforeBalance)//
				.withAfterBalance(beforeBalance - amount)//
				.withType(type)//
				.withRemark(remark).build();
		return save(accountLog);
	}

}
