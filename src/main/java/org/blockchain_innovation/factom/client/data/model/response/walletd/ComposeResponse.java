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

package org.blockchain_innovation.factom.client.data.model.response.walletd;

public class ComposeResponse {

    private Commit commit;
    private Reveal reveal;

    public Commit getCommit() {
        return commit;
    }

    public Reveal getReveal() {
        return reveal;
    }

    public class Commit {
        private String jsonrpc;
        private int id;
        private Params params;
        private String method;

        public String getJsonRpc() {
            return jsonrpc;
        }

        public int getId() {
            return id;
        }

        public Params getParams() {
            return params;
        }

        public String getMethod() {
            return method;
        }

        public class Params {
            private String message;

            public String getMessage() {
                return message;
            }
        }
    }

    public class Reveal {
        private String jsonrpc;
        private int id;
        private Params params;
        private String method;

        public String getJsonRpc() {
            return jsonrpc;
        }

        public int getId() {
            return id;
        }

        public Params getParams() {
            return params;
        }

        public String getMethod() {
            return method;
        }

        public class Params {
            private String entry;

            public String getEntry() {
                return entry;
            }
        }
    }
}
