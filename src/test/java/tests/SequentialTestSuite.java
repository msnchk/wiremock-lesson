package tests;

import org.junit.platform.suite.api.*;
import tests.mock.MockUserTests;
import tests.proxy.ProxyUserTests;
import tests.real.RealApiUserTests;

@Suite
@SelectClasses({
        MockUserTests.class,
        ProxyUserTests.class,
        RealApiUserTests.class
})
public class SequentialTestSuite {
}
