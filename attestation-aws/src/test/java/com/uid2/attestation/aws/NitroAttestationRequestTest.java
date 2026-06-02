package com.uid2.attestation.aws;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NitroAttestationRequestTest {
    @Test
    public void testGetters() {
        byte[] data = {10, 20, 30, 0, 0};
        NitroAttestationRequest r = new NitroAttestationRequest(data, 3);
        assertArrayEquals(data, r.getData());
        assertEquals(3, r.getLength());
    }
}
