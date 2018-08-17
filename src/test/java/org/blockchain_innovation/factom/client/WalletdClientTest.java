package org.blockchain_innovation.factom.client;

import org.blockchain_innovation.factom.client.data.FactomException;
import org.blockchain_innovation.factom.client.data.model.Address;
import org.blockchain_innovation.factom.client.data.model.Chain;
import org.blockchain_innovation.factom.client.data.model.Entry;
import org.blockchain_innovation.factom.client.data.model.Range;
import org.blockchain_innovation.factom.client.data.model.response.walletd.AddressResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.AddressesResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.BlockHeightTransactionResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.BlockHeightTransactionsResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.ComposeResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.ComposeTransactionResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.DeleteTransactionResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.ExecutedTransactionResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.GetHeightResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.PropertiesResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.TransactionResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.TransactionsResponse;
import org.blockchain_innovation.factom.client.data.model.response.walletd.WalletBackupResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WalletdClientTest extends AbstractClientTest {

    private final WalletdClient client = new WalletdClient();

    private static String transactionName = "TransactionName";
    private static String address = "EC3cqLZPq5ypwRB5CLfXnud5vkWAV2sd235CFf9KcWcE3FH9GRxv";
    private static String transactionId = "7552f169062885624ffbfb759c26c586121f43f5a49ee537ffa5ffb8f860eb10";
    private static int height = 40879;

    @Before
    public void setup() throws MalformedURLException {
        client.setUrl(new URL("http://136.144.204.97:8089/v2"));
    }

    @Test
    public void _00_properties() throws FactomException.ClientException {
        FactomResponse<PropertiesResponse> response = client.properties();
        assertValidResponse(response);

        PropertiesResponse properties = response.getResult();
        Assert.assertNotNull(properties);
        Assert.assertNotNull(properties.getWalletVersion());
        Assert.assertNotNull(properties.getWalletApiVersion());
    }

    @Test
    public void _01_newTransaction() throws FactomException.ClientException {
        transactionName = transactionName + System.currentTimeMillis();
        FactomResponse<TransactionResponse> response = client.newTransaction(transactionName);
        assertValidResponse(response);
        TransactionResponse transaction = response.getResult();

        Assert.assertNotNull(transaction);
        transactionId = transaction.getTxId();
        Assert.assertNotNull(transactionId);
        Assert.assertEquals(transactionName, transaction.getName());
    }

    @Test
    public void _02_generateEntryCreditAddress() throws FactomException.ClientException {
        FactomResponse<AddressResponse> response = client.generateEntryCreditAddress();
        assertValidResponse(response);

        AddressResponse address = response.getResult();
        Assert.assertNotNull(address);
        this.address = address.getPublicAddress();
        Assert.assertNotNull(address);
        Assert.assertNotNull(address.getSecret());
    }

    @Test
    public void _02_generateFactoidAddress() throws FactomException.ClientException {
        FactomResponse<AddressResponse> response = client.generateFactoidAddress();
        assertValidResponse(response);

        AddressResponse address = response.getResult();
        Assert.assertNotNull(address);
        Assert.assertNotNull(address.getSecret());
        Assert.assertNotNull(address.getPublicAddress());
    }

    @Test
    public void _11_importAddresses() throws FactomException.ClientException {
        String secret = "Fs1jQGc9GJjyWNroLPq7x6LbYQHveyjWNPXSqAvCEKpETNoTU5dP";

        Address address = new Address();
        address.setSecret(secret);
        List<Address> addresses = Arrays.asList(address);

        FactomResponse<AddressesResponse> response = client.importAddresses(addresses);
        assertValidResponse(response);

        AddressesResponse addressesResponse = response.getResult();
        Assert.assertNotNull(addressesResponse);
        Assert.assertNotNull(addressesResponse.getAddresses());
        Assert.assertNotNull(addressesResponse.getAddresses().get(0));
        Assert.assertNotNull(addressesResponse.getAddresses().get(0).getPublicAddress());
        Assert.assertNotNull(secret, addressesResponse.getAddresses().get(0).getSecret());
    }

    @Test
    public void _11_importKoinify() throws FactomException.ClientException {
        String words = "yellow yellow yellow yellow yellow yellow yellow yellow yellow yellow yellow yellow";
        FactomResponse<AddressResponse> response = client.importKoinify(words);
        assertValidResponse(response);
    }

    @Test
    public void _12_address() throws FactomException.ClientException {
        FactomResponse<AddressResponse> response = client.address(address);
        assertValidResponse(response);
        AddressResponse addressResponse = response.getResult();
        Assert.assertEquals(address, addressResponse.getPublicAddress());
        Assert.assertNotNull(addressResponse.getSecret());
    }

    @Test
    public void _13_allAddresses() throws FactomException.ClientException {
        FactomResponse<AddressesResponse> response = client.allAddresses();
        assertValidResponse(response);

        AddressesResponse addresses = response.getResult();
        Assert.assertNotNull(addresses);
        Assert.assertNotNull(addresses.getAddresses());
        Assert.assertFalse(addresses.getAddresses().isEmpty());

        AddressResponse address = addresses.getAddresses().get(0);
        Assert.assertNotNull(address);
        Assert.assertNotNull(address.getSecret());
        Assert.assertNotNull(address.getPublicAddress());
    }

    @Test
    public void _21_addEntryCreditOutput() throws FactomException.ClientException {
        int amount = 10000;
        FactomResponse<TransactionResponse> response = client.addEntryCreditOutput(transactionName, address, amount);
        assertValidResponse(response);

        TransactionResponse transaction = response.getResult();
        Assert.assertNotNull(transaction);
        Assert.assertEquals(10000, transaction.getTotalEntryCreditOutputs());
        Assert.assertEquals(10000, transaction.getEntryCreditOutputs().get(0).getAmount());
        Assert.assertEquals(address, transaction.getEntryCreditOutputs().get(0).getAddress());
        transactionId = transaction.getTxId();
    }

    @Test
    public void _22_tmpTransactions() throws FactomException.ClientException {
        FactomResponse<TransactionsResponse> response = client.tmpTransactions();
        assertValidResponse(response);

        TransactionsResponse transactions = response.getResult();
        Assert.assertNotNull(transactions);
        Assert.assertFalse(transactions.getTransactions().isEmpty());

        boolean found = false;
        for (TransactionResponse transaction : transactions.getTransactions()) {
            if (transactionName.equalsIgnoreCase(transaction.getName())) {
                Assert.assertEquals(transactionId, transaction.getTxId());
                Assert.assertEquals(transactionName, transaction.getName());
            }
        }
    }

    @Test
    public void _23_composeTransaction() throws FactomException.ClientException {
        FactomResponse<ComposeTransactionResponse> response = client.composeTransaction(transactionName);
        assertValidResponse(response);

        ComposeTransactionResponse composeTransaction = response.getResult();
        Assert.assertNotNull(composeTransaction);
        Assert.assertNotNull(composeTransaction.getParams());
        Assert.assertNotNull(composeTransaction.getParams().getTransaction());
    }

    //@Test
    public void _24_signTransaction() throws FactomException.ClientException {
        FactomResponse<ExecutedTransactionResponse> response = client.signTransaction(transactionName);
        assertValidResponse(response);

        ExecutedTransactionResponse executedTransaction = response.getResult();
        Assert.assertNotNull(executedTransaction);
        Assert.assertTrue(executedTransaction.isSigned());
    }

    //@Test
    public void _25_addInput() throws FactomException.ClientException {
        int amount = 10000;
        FactomResponse<ExecutedTransactionResponse> response = client.addInput(transactionName, address, amount);
        assertValidResponse(response);

        TransactionResponse transaction = response.getResult();
        Assert.assertNotNull(transaction);
        Assert.assertEquals(transactionName, transaction.getName());
        Assert.assertEquals(10000, transaction.getTotalEntryCreditOutputs());
        Assert.assertEquals(10000, transaction.getEntryCreditOutputs().get(0).getAmount());
        Assert.assertEquals(address, transaction.getEntryCreditOutputs().get(0).getAddress());
    }

    //@Test
    public void _25_addOutput() throws FactomException.ClientException {
        int amount = 10000;
        FactomResponse<ExecutedTransactionResponse> response = client.addOutput(transactionName, address, amount);
        assertValidResponse(response);

        TransactionResponse transaction = response.getResult();
        Assert.assertNotNull(transaction);
        Assert.assertEquals(transactionName, transaction.getName());
        Assert.assertEquals(10000, transaction.getTotalEntryCreditOutputs());
        Assert.assertEquals(10000, transaction.getEntryCreditOutputs().get(0).getAmount());
        Assert.assertEquals(address, transaction.getEntryCreditOutputs().get(0).getAddress());
    }

    //@Test
    public void _26_addFee() throws FactomException.ClientException {
        FactomResponse<ExecutedTransactionResponse> response = client.addFee(transactionName, address);
        assertValidResponse(response);

        TransactionResponse transaction = response.getResult();
        Assert.assertNotNull(transaction);
        Assert.assertEquals(10000, transaction.getTotalEntryCreditOutputs());
        Assert.assertEquals(10000, transaction.getEntryCreditOutputs().get(0).getAmount());
        Assert.assertEquals(address, transaction.getEntryCreditOutputs().get(0).getAddress());
    }

    //@Test
    public void _26_subFee() throws FactomException.ClientException {
        FactomResponse<ExecutedTransactionResponse> response = client.subFee(transactionName, address);
        assertValidResponse(response);

        TransactionResponse transaction = response.getResult();
        Assert.assertNotNull(transaction);
        Assert.assertEquals(1000, transaction.getTotalEntryCreditOutputs());
        Assert.assertEquals(1000, transaction.getEntryCreditOutputs().get(0).getAmount());
        Assert.assertEquals(address, transaction.getEntryCreditOutputs().get(0).getAddress());
    }

    //@Test
    public void _27_transactionsByAddress() throws FactomException.ClientException {
        FactomResponse<BlockHeightTransactionsResponse> response = client.transactionsByAddress(address);
        assertValidResponse(response);

        BlockHeightTransactionsResponse transactions = response.getResult();
        Assert.assertNotNull(transactions);
        Assert.assertNotNull(transactions.getTransactions());
        Assert.assertFalse(transactions.getTransactions().isEmpty());
        Assert.assertTrue(transactions.getTransactions().get(0).isSigned());
        Assert.assertNotNull(transactions.getTransactions().get(0).getTxId());
    }

    // @Test
    public void _28_transactionsByTransaction() throws FactomException.ClientException {
        FactomResponse<TransactionsResponse> response = client.transactionsByTransaction(transactionId);
        assertValidResponse(response);

        TransactionsResponse transactions = response.getResult();
        Assert.assertNotNull(transactions);
        Assert.assertNotNull(transactions.getTransactions());
        Assert.assertFalse(transactions.getTransactions().isEmpty());
        Assert.assertEquals(transactionId, transactions.getTransactions().get(0).getTxId());
    }

    @Test
    public void _29_deleteTransaction() throws FactomException.ClientException {
        FactomResponse<DeleteTransactionResponse> response = client.deleteTransaction(transactionName);
        assertValidResponse(response);

        DeleteTransactionResponse deletedTransaction = response.getResult();
        Assert.assertNotNull(deletedTransaction);
        Assert.assertEquals(transactionName, deletedTransaction.getName());
    }

    @Test
    public void _31_composeChain() throws FactomException.ClientException {
        List<String> externalIds = Arrays.asList(
                "61626364",
                "31323334");
        Chain.Entry firstEntry = new Chain.Entry();
        firstEntry.setExternalIds(externalIds);
        firstEntry.setContent("3132333461626364");
        Chain chain = new Chain();
        chain.setFirstEntry(firstEntry);

        FactomResponse<ComposeResponse> response = client.composeChain(chain, "EC3cqLZPq5ypwRB5CLfXnud5vkWAV2sd235CFf9KcWcE3FH9GRxv");
        assertValidResponse(response);

        ComposeResponse composeResponse = response.getResult();
        Assert.assertNotNull(composeResponse.getCommit());
        Assert.assertNotNull(composeResponse.getCommit().getId());
        Assert.assertNotNull(composeResponse.getCommit().getParams());
        Assert.assertNotNull(composeResponse.getCommit().getParams().getMessage());
        Assert.assertNotNull(composeResponse.getReveal());
        Assert.assertNotNull(composeResponse.getReveal().getId());
        Assert.assertNotNull(composeResponse.getReveal().getParams());
        Assert.assertNotNull(composeResponse.getReveal().getParams().getEntry());
    }

    @Test
    public void _32_composeEntry() throws FactomException.ClientException {
        List<String> externalIds = Arrays.asList("cd90", "90cd");
        Entry entry = new Entry();
        entry.setChainId("041ffaf76eb6370c94701c7aa60cc8c114fc68ede00d28389bc31850ef732c4f");
        entry.setContent("abcdef");
        entry.setExternalIds(externalIds);

        FactomResponse<ComposeResponse> response = client.composeEntry(entry, "EC3cqLZPq5ypwRB5CLfXnud5vkWAV2sd235CFf9KcWcE3FH9GRxv");
        assertValidResponse(response);

        ComposeResponse composeResponse = response.getResult();
        Assert.assertNotNull(composeResponse.getCommit());
        Assert.assertNotNull(composeResponse.getCommit().getId());
        Assert.assertNotNull(composeResponse.getCommit().getParams());
        Assert.assertNotNull(composeResponse.getCommit().getParams().getMessage());
        Assert.assertNotNull(composeResponse.getReveal());
        Assert.assertNotNull(composeResponse.getReveal().getId());
        Assert.assertNotNull(composeResponse.getReveal().getParams());
        Assert.assertNotNull(composeResponse.getReveal().getParams().getEntry());
    }

    @Test
    public void _41_getHeight() throws FactomException.ClientException {
        FactomResponse<GetHeightResponse> response = client.getHeight();
        assertValidResponse(response);

        GetHeightResponse heightResponse = response.getResult();
        Assert.assertNotNull(heightResponse);
        height = heightResponse.getHeight();
        Assert.assertTrue(height > 0);
    }

    @Test
    public void _42_transactionsByRange() throws FactomException.ClientException {
        int start = height - 10;
        int end = height + 10;

        Range range = new Range();
        range.setStart(start);
        range.setEnd(end);

        FactomResponse<BlockHeightTransactionsResponse> response = client.transactionsByRange(range);
        assertValidResponse(response);

        BlockHeightTransactionsResponse transactions = response.getResult();
        Assert.assertNotNull(transactions);
        Assert.assertNotNull(transactions.getTransactions());
        Assert.assertFalse(transactions.getTransactions().isEmpty());

        BlockHeightTransactionResponse transaction = transactions.getTransactions().get(0);
        Assert.assertNotNull(transaction);
        Assert.assertTrue(start <= transaction.getBlockHeight() && end >= transaction.getBlockHeight());
    }

    @Test
    public void walletBackup() throws FactomException.ClientException {
        FactomResponse<WalletBackupResponse> response = client.walletBackup();
        assertValidResponse(response);
        WalletBackupResponse walletBackup = response.getResult();
        Assert.assertNotNull(walletBackup.getWalletSeed());
        Assert.assertNotNull(walletBackup.getAddresses());
        Assert.assertFalse(walletBackup.getAddresses().isEmpty());
    }
}