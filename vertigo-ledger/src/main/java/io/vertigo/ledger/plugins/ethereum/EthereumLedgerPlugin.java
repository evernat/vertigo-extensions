/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.ledger.plugins.ethereum;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import io.vertigo.commons.eventbus.EventBusManager;
import io.vertigo.core.component.Activeable;
import io.vertigo.core.param.ParamValue;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.VSystemException;
import io.vertigo.lang.WrappedException;
import io.vertigo.ledger.impl.services.LedgerPlugin;
import io.vertigo.ledger.services.LedgerAddress;
import io.vertigo.ledger.services.LedgerTransactionEvent;
import io.vertigo.ledger.services.LedgerTransaction;
import io.vertigo.ledger.services.LedgerTransactionPriorityEnum;
import rx.Subscription;

/**
 * Client RPC Ethereum (for Geth and Parity)
 * @author xdurand
 *
 */
public final class EthereumLedgerPlugin implements LedgerPlugin, Activeable {
	private static final Logger LOGGER = LogManager.getLogger(EthereumLedgerPlugin.class);

	private final EventBusManager eventBusManager;

	private final Web3j web3j;
	private final Credentials credentials;
	private final LedgerAddress defaultDestPublicAddr;
	private final LedgerAddress myWalletAddress;

	private Subscription subscription;

	@Inject
	public EthereumLedgerPlugin(
			final EventBusManager eventBusManager,
			@ParamValue("urlRpcEthNode") final String urlRpcEthNode,
			@ParamValue("myAccountName") final String myAccountName,
			@ParamValue("myPublicAddr") final String myPublicAddr,
			@ParamValue("defaultDestAccountName") final String defaultDestAccountName,
			@ParamValue("defaultDestPublicAddr") final String defaultDestPublicAddr,
			@ParamValue("walletPassword") final String walletPassword,
			@ParamValue("walletPath") final String walletPath) {
		Assertion.checkNotNull(eventBusManager);
		//---
		Assertion.checkArgNotEmpty(myAccountName);
		Assertion.checkArgNotEmpty(myPublicAddr);
		Assertion.checkArgNotEmpty(defaultDestAccountName);
		Assertion.checkArgNotEmpty(defaultDestPublicAddr);
		Assertion.checkArgNotEmpty(walletPassword);
		Assertion.checkArgNotEmpty(walletPath);
		Assertion.checkNotNull(eventBusManager);
		//---
		this.eventBusManager = eventBusManager;
		//---
		myWalletAddress = new LedgerAddress(myAccountName, myPublicAddr);
		this.defaultDestPublicAddr = new LedgerAddress(defaultDestAccountName, defaultDestPublicAddr);

		LOGGER.info("Connecting to RPC Ethereum Node: {}", urlRpcEthNode);
		web3j = Web3j.build(new HttpService(urlRpcEthNode));
		try {
			final Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
			LOGGER.info("Connected to RPC Ethereum Node: {}. Client version: {}", urlRpcEthNode, web3ClientVersion.getWeb3ClientVersion());
			credentials = WalletUtils.loadCredentials(walletPassword, walletPath);
		} catch (final IOException | CipherException e) {
			throw WrappedException.wrap(e);
		}
	}

	@Override
	public void start() {
		subscription = web3j.transactionObservable()
				.filter(tx -> tx.getTo().equals(myWalletAddress.getPublicAddress()))
				.map(EthereumLedgerPlugin::convertTransactionToLedgerTransaction)
				.subscribe(ledgerTransaction -> eventBusManager.post(new LedgerTransactionEvent(ledgerTransaction)));
		LOGGER.info("Getting new messages sent to {}.", myWalletAddress);
	}

	@Override
	public void stop() {
		subscription.unsubscribe();

	}

	@Override
	public BigInteger getMyWalletBalance() {
		return getWalletBalance(myWalletAddress);
	}

	@Override
	public BigInteger getWalletBalance(final LedgerAddress ledgerAddress) {
		Assertion.checkNotNull(ledgerAddress);
		//---
		final EthGetBalance balance;
		try {
			balance = web3j.ethGetBalance(ledgerAddress.getPublicAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			throw WrappedException.wrap(e);
		}

		if (balance.hasError()) {
			throw new VSystemException(balance.getError().getMessage());
		}

		return balance.getBalance();
	}

	@Override
	public void sendData(final String data) {
		sendData(data, defaultDestPublicAddr, LedgerTransactionPriorityEnum.VERYFAST);
	}

	private void sendData(final String data, final LedgerAddress ledgerAddress, final LedgerTransactionPriorityEnum priority) {
		Assertion.checkArgNotEmpty(data);
		Assertion.checkNotNull(ledgerAddress);
		Assertion.checkNotNull(priority);
		//---
		try {
			final TransactionReceipt transactionReceipt = EthereumTransfer.sendFunds(web3j, credentials, ledgerAddress.getPublicAddress(),
					BigDecimal.valueOf(0), Convert.Unit.WEI, data, priority)
					.send();

			if (!transactionReceipt.isStatusOK()) {
				throw new VSystemException("Ethereum write failed", transactionReceipt.getStatus());
			}

		} catch (final Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	private static LedgerTransaction convertTransactionToLedgerTransaction(final Transaction transaction) {
		return LedgerTransaction.builder()
				.withHash(transaction.getBlockHash())
				.withBlockNumber(transaction.getBlockNumber())
				.withFrom(transaction.getFrom())
				.withTo(transaction.getTo())
				.withNonce(transaction.getNonce())
				.withTransactionIndex(transaction.getTransactionIndex())
				.withValue(transaction.getValue())
				.withMessage(transaction.getInput())
				.build();
	}

}
