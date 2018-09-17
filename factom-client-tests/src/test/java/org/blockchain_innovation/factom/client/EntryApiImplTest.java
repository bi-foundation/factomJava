package org.blockchain_innovation.factom.client;

import org.blockchain_innovation.factom.client.api.listeners.CommitAndRevealListener;
import org.blockchain_innovation.factom.client.api.model.Chain;
import org.blockchain_innovation.factom.client.api.model.Entry;
import org.blockchain_innovation.factom.client.api.model.response.CommitAndRevealChainResponse;
import org.blockchain_innovation.factom.client.api.model.response.CommitAndRevealEntryResponse;
import org.blockchain_innovation.factom.client.api.model.response.factomd.CommitChainResponse;
import org.blockchain_innovation.factom.client.api.model.response.factomd.CommitEntryResponse;
import org.blockchain_innovation.factom.client.api.model.response.factomd.EntryTransactionResponse;
import org.blockchain_innovation.factom.client.api.model.response.factomd.RevealResponse;
import org.blockchain_innovation.factom.client.api.model.response.walletd.ComposeResponse;
import org.blockchain_innovation.factom.client.api.ops.Encoding;
import org.blockchain_innovation.factom.client.api.rpc.RpcErrorResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class EntryApiImplTest extends AbstractClientTest {

    @Test
    public void testChain() {
        Chain chain = chain();
        AtomicReference<CommitEntryResponse> commitChainResponse = new AtomicReference<>();
        AtomicReference<RevealResponse> revealChainResponse = new AtomicReference<>();
        AtomicReference<EntryTransactionResponse> transactionAcknowledgedResponse = new AtomicReference<>();

        CommitAndRevealListener listener = new CommitAndRevealListener() {

            @Override
            public void onCompose(ComposeResponse composeResponse) {
                System.out.println("> Compose = " + composeResponse);
            }

            @Override
            public void onCommit(CommitEntryResponse commitResponse) {
                System.out.println("> Commit = " + commitResponse);
            }

            @Override
            public void onCommit(CommitChainResponse commitResponse) {
                System.out.println("> Commit = " + commitResponse);
                commitChainResponse.set(commitResponse);
            }

            @Override
            public void onReveal(RevealResponse revealResponse) {
                System.out.println("> Reveal = " + revealResponse);
                revealChainResponse.set(revealResponse);
            }

            @Override
            public void onTransactionAcknowledged(EntryTransactionResponse transactionResponse) {
                System.out.println("> TransactionAcknowledged = " + transactionResponse);
                transactionAcknowledgedResponse.set(transactionResponse);
            }

            @Override
            public void onCommitConfirmed(EntryTransactionResponse transactionResponse) {
                System.out.println("> ChainCommitConfirmed = " + transactionResponse);
            }

            @Override
            public void onError(RpcErrorResponse e) {
                System.out.println("e = " + e);
                Assert.fail(e.getJsonrpc());
            }
        };

        entryClient.clearListeners().addListener(listener);

        CommitAndRevealChainResponse commitAndRevealChain = entryClient.commitAndRevealChain(chain, EC_PUBLIC_ADDRESS).join();

        Assert.assertEquals("Chain Commit Success", commitAndRevealChain.getCommitChainResponse().getMessage());
        Assert.assertEquals("Entry Reveal Success", commitAndRevealChain.getRevealResponse().getMessage());
        Assert.assertEquals(commitAndRevealChain.getCommitChainResponse().getEntryHash(), commitAndRevealChain.getRevealResponse().getEntryHash());
        Assert.assertEquals("Chain Commit Success", commitChainResponse.get().getMessage());
        Assert.assertEquals("Entry Reveal Success", revealChainResponse.get().getMessage());
        Assert.assertEquals(commitChainResponse.get().getEntryHash(), revealChainResponse.get().getEntryHash());

        Assert.assertNotNull(transactionAcknowledgedResponse.get());
        Assert.assertNotNull(commitAndRevealChain.getCommitChainResponse().getEntryHash(), transactionAcknowledgedResponse.get().getEntryHash());

        System.out.println("commitAndRevealChain = " + commitAndRevealChain);
    }


    @Test
    public void testEntry() throws InterruptedException {
        Entry entry = entry();
        AtomicBoolean transactionAcknowledge = new AtomicBoolean(false);
        AtomicReference<CommitEntryResponse> commitEntryResponse = new AtomicReference<>();
        AtomicReference<RevealResponse> revealEntryResponse = new AtomicReference<>();
        AtomicReference<EntryTransactionResponse> transactionAcknowledgedResponse = new AtomicReference<>();

        final CommitAndRevealListener listener = new CommitAndRevealListener() {

            @Override
            public void onCompose(ComposeResponse composeResponse) {
                System.out.println("> Compose = " + composeResponse);
            }

            @Override
            public void onCommit(CommitEntryResponse commitResponse) {
                System.out.println("> Commit = " + commitResponse);
                commitEntryResponse.set(commitResponse);
            }

            @Override
            public void onCommit(CommitChainResponse commitResponse) {
                System.out.println("> Commit = " + commitResponse);
                commitEntryResponse.set(commitResponse);
            }

            @Override
            public void onReveal(RevealResponse revealResponse) {
                System.out.println("> Reveal = " + revealResponse);
                revealEntryResponse.set(revealResponse);
            }

            @Override
            public void onTransactionAcknowledged(EntryTransactionResponse transactionResponse) {
                System.out.println("> TransactionAcknowledged = " + transactionResponse);
                transactionAcknowledgedResponse.set(transactionResponse);
            }

            @Override
            public void onCommitConfirmed(EntryTransactionResponse transactionResponse) {
                System.out.println("> ChainCommitConfirmed = " + transactionResponse);
            }

            @Override
            public void onError(RpcErrorResponse e) {
                System.out.println("e = " + e);
                Assert.fail(e.getJsonrpc());
            }
        };

        entryClient.clearListeners().addListener(listener);
        CompletableFuture<CommitAndRevealEntryResponse> commitFuture = entryClient.commitAndRevealEntry(entry, EC_PUBLIC_ADDRESS);
        CommitAndRevealEntryResponse commitAndRevealEntry = commitFuture.join();

        Assert.assertEquals("Entry Commit Success", commitAndRevealEntry.getCommitEntryResponse().getMessage());
        Assert.assertEquals("Entry Reveal Success", commitAndRevealEntry.getRevealResponse().getMessage());
        Assert.assertEquals(commitAndRevealEntry.getCommitEntryResponse().getEntryHash(), commitAndRevealEntry.getRevealResponse().getEntryHash());

        Assert.assertEquals("Entry Commit Success", commitEntryResponse.get().getMessage());
        Assert.assertEquals("Entry Reveal Success", revealEntryResponse.get().getMessage());
        Assert.assertEquals(commitAndRevealEntry.getCommitEntryResponse().getEntryHash(), commitEntryResponse.get().getEntryHash());
        Assert.assertEquals(commitAndRevealEntry.getRevealResponse().getEntryHash(), revealEntryResponse.get().getEntryHash());

        Assert.assertNotNull(transactionAcknowledgedResponse.get());
        Assert.assertNotNull(commitAndRevealEntry.getCommitEntryResponse().getEntryHash(), transactionAcknowledgedResponse.get().getEntryHash());
    }

    private Chain chain() {
        int randomness = 10000000 + new Random().nextInt(90000000);
        List<String> externalIds = Arrays.asList(
                String.valueOf(randomness),
                "80731950",
                "61626364");

        Entry firstEntry = new Entry();
        firstEntry.setExternalIds(externalIds);
        firstEntry.setContent("3132333461626364");

        Chain chain = new Chain();
        chain.setFirstEntry(firstEntry);
        return chain;
    }

    private Entry entry() {
        List<String> externalIds = Arrays.asList("cd94", "90cd", Encoding.HEX.encode(Encoding.UTF_8.decode("TEST2")));

        Entry entry = new Entry();
        entry.setChainId("8409e82079e958e9beb31e76634234c3dc2b8ecf5db888e440541678a79dd4db");
        entry.setContent("abcdef");
        entry.setExternalIds(externalIds);

        return entry;
    }


    @Test
    public void testChainAsync() throws InterruptedException {
        Chain chain = chain();
        AtomicReference<CommitEntryResponse> commitChainResponse = new AtomicReference<>();
        AtomicReference<RevealResponse> revealChainResponse = new AtomicReference<>();
        AtomicReference<EntryTransactionResponse> transactionAcknowledgedResponse = new AtomicReference<>();

        CommitAndRevealListener listener = new CommitAndRevealListener() {

            @Override
            public void onCompose(ComposeResponse composeResponse) {
                System.out.println("> Compose = " + composeResponse);
            }

            @Override
            public void onCommit(CommitEntryResponse commitResponse) {
                System.out.println("> Commit = " + commitResponse);
            }

            @Override
            public void onCommit(CommitChainResponse commitResponse) {
                System.out.println("> Commit = " + commitResponse);
                commitChainResponse.set(commitResponse);
            }

            @Override
            public void onReveal(RevealResponse revealResponse) {
                System.out.println("> Reveal = " + revealResponse);
                revealChainResponse.set(revealResponse);
            }

            @Override
            public void onTransactionAcknowledged(EntryTransactionResponse transactionResponse) {
                System.out.println("> TransactionAcknowledged = " + transactionResponse);
                transactionAcknowledgedResponse.set(transactionResponse);
            }

            @Override
            public void onCommitConfirmed(EntryTransactionResponse transactionResponse) {
                System.out.println("> ChainCommitConfirmed = " + transactionResponse);
            }

            @Override
            public void onError(RpcErrorResponse e) {
                System.out.println("e = " + e);
                Assert.fail(e.getJsonrpc());
            }
        };

        entryClient.clearListeners().addListener(listener);
        CompletableFuture<CommitAndRevealChainResponse> future = entryClient.commitAndRevealChain(chain, EC_PUBLIC_ADDRESS, true);

        int count = 0;
        while(transactionAcknowledgedResponse.get() == null && count < 100) {
            count++;
            Thread.sleep(1000);
        }
        future.cancel(true);

        Assert.assertEquals("Chain Commit Success", commitChainResponse.get().getMessage());
        Assert.assertEquals("Entry Reveal Success", revealChainResponse.get().getMessage());
        Assert.assertEquals(commitChainResponse.get().getEntryHash(), revealChainResponse.get().getEntryHash());

        Assert.assertNotNull(transactionAcknowledgedResponse.get());
        Assert.assertNotNull(commitChainResponse.get().getEntryHash(), transactionAcknowledgedResponse.get().getEntryHash());
    }
}
