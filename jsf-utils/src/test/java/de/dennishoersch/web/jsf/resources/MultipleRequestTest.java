/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dennishoersch.web.jsf.resources;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * @author hoersch
 *
 */
public class MultipleRequestTest {

    @Test
    public void testMultipleRequests() throws IOException, InterruptedException {
        final URL url = new URL("http://localhost:7070/kyoma");

        read(url);

        int count = 100;
        int requests = 101;

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(count);

        Thread[] threads = new Thread[count];
        for (int i = 0; i < count; i++) {
            threads[i] = new Thread(new RequestSimulator(url, requests, start, finished), "RequestSImulator" + i);
            threads[i].start();
        }

        System.out.println("Start requesting...");
        start.countDown();
        finished.await();
        System.out.println("Finished.");
    }

    static Object read(final URL url) throws IOException {
        Object content = url.openConnection().getContent();
        return content;
    }

    private static class RequestSimulator implements Runnable {

        private final SecureRandom _RANDOM = new SecureRandom();

        private final URL _url;
        private final CountDownLatch _start;
        private final CountDownLatch _finished;
        private final int _requests;

        public RequestSimulator(URL url, int requests, CountDownLatch start, CountDownLatch finished) {
            this._url = url;
            this._requests = requests;
            this._start = start;
            this._finished = finished;
        }

        @Override
        public void run() {
            try {
                // synchronize all requests to start simultaneously
                _start.await();
                for (int i = 0; i < _requests; i++) {
                    read(_url);

                    try {
                        int millisToWait = 1 * (_RANDOM.nextInt(100) + 10);
                        Thread.sleep(millisToWait);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                _finished.countDown();
            }
        }
    }
}
