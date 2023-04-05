package com.example.launchdarkly;

import com.launchdarkly.sdk.ContextKind;
import com.launchdarkly.sdk.EvaluationDetail;
import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.LDValue;
import com.launchdarkly.sdk.server.LDClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private final LDClient ldClient;

    private static final String KEY = "maintenanceFlag";

    private static final String KIND = "kioskMachine";

    @GetMapping
    public Map<String, LDValue> getKiosks() {
        LDContext context = LDContext.create(String.valueOf(ContextKind.of(KIND)));

        return ldClient.allFlagsState(context).toValuesMap();

    }

//    @GetMapping
//    public EvaluationDetail<Boolean> getDetail() {
//        LDContext context = LDContext.create(String.valueOf(ContextKind.of(KIND)));
//
//        return ldClient.boolVariationDetail(KEY, context, false);
//
//    }

    @PostMapping("/{name}")
    public boolean toggleMaintenance(@PathVariable String name, @RequestParam boolean isMaintenance) {
        LDContext context = LDContext.builder(name)
                .kind(ContextKind.of(KIND))
                .set("isMaintenance", isMaintenance)
                .build();

        return ldClient.boolVariation(KEY, context, false);

    }

    @PutMapping("/{name}")
    public void updateContext(@PathVariable String name, @RequestParam boolean isMaintenance) {
        LDContext context = LDContext.builder(name)
                .kind(ContextKind.of(KIND))
                .set("isMaintenance", isMaintenance)
                .build();

        ldClient.identify(context);

    }

    @GetMapping("/{name}")
    public boolean getStatus(@PathVariable String name) {
        LDContext context = LDContext.create(ContextKind.of(KIND), name);

        return ldClient.boolVariation(KEY, context, false);
    }

    @PostMapping
    public Boolean createContext(@RequestParam String name) {
        LDContext context = LDContext.builder(name)
                .kind(ContextKind.of(KIND))
                .set("isMaintenance", false)
                .build();
        return ldClient.boolVariation(KEY, context, false);

    }
}
