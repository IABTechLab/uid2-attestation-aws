package com.uid2.attestation.aws;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NitroExceptionTest {
    @Test public void testInvalidArgument()  { assertEquals("Invalid Argument",  NitroException.fromErrorCode(-1).getMessage()); }
    @Test public void testInvalidIndex()     { assertEquals("Invalid Index",     NitroException.fromErrorCode(-2).getMessage()); }
    @Test public void testInvalidResponse()  { assertEquals("Invalid Response",  NitroException.fromErrorCode(-3).getMessage()); }
    @Test public void testReadonlyIndex()    { assertEquals("Readonly Index",    NitroException.fromErrorCode(-4).getMessage()); }
    @Test public void testInvalidOperation() { assertEquals("Invalid Operation", NitroException.fromErrorCode(-5).getMessage()); }
    @Test public void testBufferTooSmall()   { assertEquals("Buffer too small",  NitroException.fromErrorCode(-6).getMessage()); }
    @Test public void testInputTooLarge()    { assertEquals("Input too large",   NitroException.fromErrorCode(-7).getMessage()); }
    @Test public void testInternalError()    { assertEquals("Internal Error",    NitroException.fromErrorCode(-8).getMessage()); }
    @Test public void testUnknownPositive()  { assertEquals("Unknown Error",     NitroException.fromErrorCode(42).getMessage()); }
    @Test public void testUnknownNegative()  { assertEquals("Unknown Error",     NitroException.fromErrorCode(-9).getMessage()); }
}
