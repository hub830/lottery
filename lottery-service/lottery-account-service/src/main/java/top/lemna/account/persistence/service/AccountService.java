package top.lemna.account.persistence.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.fsg.uid.UidGenerator;

import top.lemna.account.exceptions.AccountAlreadyExistException;
import top.lemna.account.exceptions.AccountBalanceNotEnoughException;
import top.lemna.account.exceptions.AccountBlockException;
import top.lemna.account.model.constant.BillType;
import top.lemna.account.persistence.domain.Account;
import top.lemna.account.persistence.repository.AccountRepository;
import top.lemna.core.constants.YesNo;
import top.lemna.data.jpa.exception.RecordNotExistException;
import top.lemna.data.jpa.service.BaseService;

@Service
@Transactional
public class AccountService extends BaseService<Account> {

	@Autowired
	private UidGenerator uidGenerator;

	@Autowired
	private AccountLogService accountLogService;

	@Autowired
	private AccountRepository repository;

	public Account findByAccountNoForUpdate(Long accountNo) {
		Optional<Account> t = repository.findByAccountNoForUpdate(accountNo);
		return t.orElseThrow(() -> new RecordNotExistException(accountNo));
	}

	/**
	 * 创建账号
	 * 
	 * @param accountName
	 * @return
	 */
	@Transactional
	public Account create(String accountName) {
		Optional<Account> optional = repository.findByAccountName(accountName);
		if (optional.isPresent()) {
			throw new AccountAlreadyExistException(accountName);
		}
		long uid = uidGenerator.getUID();
		Account account = new Account(uid, accountName);
		return save(account);
	}

	/**
	 * 借记操作
	 * 
	 * @param accountNo
	 * @param amount
	 */
	@Transactional
	public void credit(Long accountNo, Long amount, Long externalNo, BillType type, String remark) {
		Account account = findByAccountNoForUpdate(accountNo);
		Long beforeBalance = account.getBalance();
		account.setBalance(beforeBalance + amount);

		// 创建余额流水记录
		accountLogService.credit(accountNo, amount, externalNo, beforeBalance, type, remark);

		save(account);
	}

	/**
	 * 
	 * 贷记操作
	 * 
	 * @param accountNo
	 * @param externalNo
	 * @param amount
	 * @param type
	 * @param remark
	 */
	@Transactional
	void debit(Long accountNo, Long amount, Long externalNo, BillType type, String remark) {
		Account account = findByAccountNoForUpdate(accountNo);
		// 判断账户是否被锁定
		if (account.getBlock() == YesNo.YES) {
			throw new AccountBlockException(account.getAccountNo(), account.getAccountName());
		}
		// 判断账户余额是否足够
		if (account.getBalance() < amount) {
			throw new AccountBalanceNotEnoughException(account.getAccountNo(), account.getAccountName(),
					account.getBalance(), amount);
		}

		Long beforeBalance = account.getBalance();
		account.setBalance(beforeBalance - amount);

		// 创建余额流水记录
		accountLogService.debit(accountNo, amount, externalNo, beforeBalance, type, remark);
		save(account);
	}

	public Account findByAccountNo(Long accountNo) {
		Optional<Account> t = repository.findById(accountNo);
		return t.orElseThrow(() -> new RecordNotExistException(accountNo));
	}
}
