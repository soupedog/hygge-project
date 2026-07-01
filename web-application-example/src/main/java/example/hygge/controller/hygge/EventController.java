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

package example.hygge.controller.hygge;

import example.hygge.event.MockEvent;
import example.hygge.service.HyggeEventService;
import hygge.web.template.definition.HyggeController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xavier
 * @date 2026/7/1
 */
@RestController
@Tag(name = "EventController", description = "演示 BaseHyggeEventListener 配套模板用法")
public class EventController implements HyggeController<ResponseEntity<?>> {
    private final HyggeEventService hyggeEventService;

    public EventController(HyggeEventService hyggeEventService) {
        this.hyggeEventService = hyggeEventService;
    }

    @GetMapping("/event")
    public ResponseEntity<?> sendEvent(@RequestParam(value = "resend", required = false, defaultValue = "50") Integer resend) {
        MockEvent event = hyggeEventService.buildEvent(resend, MockEvent::new)
                .source(resend)
                .build();

        hyggeEventService.fireEvent(event);

        return success();
    }
}
