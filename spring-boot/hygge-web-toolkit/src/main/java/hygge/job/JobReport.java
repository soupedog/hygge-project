/*
 * Copyright 2022-2024 the original author or authors.
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

package hygge.job;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public class JobReport<IUI> {
    protected String title;
    protected ConcurrentLinkedQueue<JobReportItem<IUI>> queue = new ConcurrentLinkedQueue<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void add(JobReportItem<IUI> item) {
        queue.add(item);
    }

    public ConcurrentLinkedQueue<JobReportItem<IUI>> getQueue() {
        return queue;
    }

    public void setQueue(ConcurrentLinkedQueue<JobReportItem<IUI>> queue) {
        this.queue = queue;
    }
}
