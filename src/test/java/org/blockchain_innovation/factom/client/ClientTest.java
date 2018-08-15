/*
 * Copyright 2018 Blockchain Innovation Foundation <https://blockchain-innovation.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.blockchain_innovation.factom.client;

import org.blockchain_innovation.factom.client.data.FactomException;
import org.blockchain_innovation.factom.client.data.model.response.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class ClientTest {


    private final FactomdClient client = new FactomdClient();


    @Before
    public void setup() throws MalformedURLException {
        client.setUrl(new URL("http://136.144.204.97:8088/v2"));
    }

    @Test
    public void testHeights() throws FactomException.ClientException {
        FactomResponse<HeightsResponse> response = client.heights();
        Assert.assertNotNull(response);
    }

    @Test
    public void testAdminBlockHeight() throws FactomException.ClientException {
        FactomResponse<AdminBlockResponse> response = client.adminBlockByHeight(10);
        Assert.assertNotNull(response);
    }

    @Test
    public void testAdminBlockKeyMr() throws FactomException.ClientException {
        FactomResponse<AdminBlockResponse> response = client.adminBlockByKeyMerkleRoot("343ffe17ca3b9775196475380feb91768e8cb3ceb888f2d617d4f0c2cc84a26a");
        Assert.assertNotNull(response);
    }

    @Test
    public void testChainHead() throws FactomException.ClientException {
        FactomResponse<ChainHeadResponse> response = client.chainHead("000000000000000000000000000000000000000000000000000000000000000a");
        Assert.assertNotNull(response);

    }

    @Test
    public void testDirectoryBlockHeight() throws FactomException.ClientException, InterruptedException {
        FactomResponse<DirectoryBlockResponse> response = client.directoryBlockByHeight(39251);
        Assert.assertNotNull(response);
        Assert.assertNull(response.getRpcErrorResponse());
    }

    @Test
    public void testDirectoryBlockHead() throws FactomException.ClientException, InterruptedException {
        FactomResponse<DirectoryBlockHeadResponse> response = client.directoryBlockHead();
        Assert.assertNotNull(response);
        Assert.assertNull(response.getRpcErrorResponse());
    }

    @Test
    public void testAckEntryTransactions() throws FactomException.ClientException, InterruptedException {
        FactomResponse<EntryTransactionsResponse> response = client.ackEntryTransactions("e96cca381bf25f6dd4dfdf9f7009ff84ee6edaa3f47f9ccf06d2787482438f4b");
        Assert.assertNotNull(response);
        Assert.assertNull(response.getRpcErrorResponse());
    }

    @Test
    public void testAckFactoidTransactions() throws FactomException.ClientException, InterruptedException {
        FactomResponse<FactoidTransactionsResponse> response = client.ackFactoidTransactions("e96cca381bf25f6dd4dfdf9f7009ff84ee6edaa3f47f9ccf06d2787482438f4b");
        Assert.assertNotNull(response);
        Assert.assertNull(response.getRpcErrorResponse());
    }

    @Test
    public void testRawData() throws FactomException.ClientException, InterruptedException {
        FactomResponse<RawDataResponse> response = client.rawData("e84cabc86d26b548da00d28ff48bb458610b255b762be44597e5b971bd75f8d7");
        Assert.assertNotNull(response);
        Assert.assertNull(response.getRpcErrorResponse());
    }

    @Test
    public void testProperties() throws FactomException.ClientException, InterruptedException {
        FactomResponse<PropertiesResponse> response = client.properties();
        Assert.assertNotNull(response);
        Assert.assertNull(response.getRpcErrorResponse());
    }

    @Test
    public void testTransactions() throws FactomException.ClientException, InterruptedException {
        FactomResponse<TransactionResponse> response = client.transaction("e96cca381bf25f6dd4dfdf9f7009ff84ee6edaa3f47f9ccf06d2787482438f4b");
        Assert.assertNotNull(response);
        Assert.assertNull(response.getRpcErrorResponse());
    }
}
