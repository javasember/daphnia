package org.daphnia.test;

import lombok.Getter;
import lombok.Setter;

public class TestService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
    
    public void sitty() {
    }
    
    public String setTest(Test t) {
        return t == null ? "Fail" : "OK";
    }
    
    public static class Test {
        private @Getter @Setter Object name;
        private @Getter @Setter String version;
        private @Getter @Setter Test2[] members;
    }

    public static class Test2 {
        private @Getter @Setter String name;
        private @Getter @Setter Integer age;
    }

    
}
